package codepred.customer.service;

import codepred.customer.dto.SignInRequest;
import codepred.customer.dto.SignUpRequest;
import codepred.customer.dto.VerifyUserRequest;
import codepred.user.security.JwtTokenProvider;
import javax.servlet.http.HttpServletRequest;

import codepred.driver.model.DriverEntity;
import codepred.driver.repository.DriverRepository;
import codepred.passenger.model.PassengerEntity;
import codepred.passenger.repository.PassengerRepository;
import codepred.customer.dto.CreateStatus;
import codepred.customer.dto.ResponseObj;
import codepred.customer.dto.Status;
import codepred.customer.model.AppUserRole;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import codepred.customer.model.AppUser;
import codepred.customer.repository.UserRepository;
import codepred.sms.SmsService;

import static codepred.customer.dto.CreateStatus.*;
import static codepred.customer.dto.Status.BAD_REQUEST;
import static codepred.customer.dto.Status.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    private final SmsService smsService;

    @Autowired
    PassengerRepository passengerRepository;

    @Autowired
    DriverRepository driverRepository;

    public ResponseObj signin(SignInRequest signInRequest) {
        AppUser appUser = userRepository.findByPhone(signInRequest.phoneNumber());
        if(appUser == null){
            return new ResponseObj(BAD_REQUEST,"USER_NOT_EXISTS",null);
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.phoneNumber(),
                                                                                       signInRequest.password()));
        } catch (Exception e){
            return new ResponseObj(UNAUTHORIZED,"WRONG_DATA",null);
        }

        ResponseObj responseObj = new ResponseObj();
        responseObj.setCode(Status.ACCEPTED);
        responseObj.setMessage("CORRECT_LOGIN_DATA");
        responseObj.setToken(jwtTokenProvider.createToken(signInRequest.phoneNumber(),
                                                          userRepository.findByPhone(signInRequest.phoneNumber())
                                                              .getAppUserRoles()));
        return responseObj;
    }

    @Transactional
    public AppUser createNewUser(SignUpRequest signUpRequest) {
        AppUser appUser = new AppUser();
        appUser.setPhone(signUpRequest.phoneNumber());
        appUser.setActive(false);
        appUser.setFirstName(signUpRequest.name());
        appUser.setLastName(signUpRequest.lastname());
        List<AppUserRole> list = new ArrayList<>();
        list.add(AppUserRole.ROLE_NONE);
        appUser.setAppUserRoles(list);
        String code = smsService.sendSms(signUpRequest.phoneNumber());
        appUser.setPassword(passwordEncoder.encode(code));
        appUser.setVerificationCode(code);
        userRepository.save(appUser);
        return appUser;
    }

    public Integer getResponseCode(Status code){
        if(code.equals(Status.ACCEPTED)){
            return 200;
        }
        else if(code.equals(Status.BAD_REQUEST)){
            return 400;
        }
        return 500;
    }

    public ResponseObj verifyCode(VerifyUserRequest verifyUserRequest) {
        ResponseObj responseObj = new ResponseObj();
        AppUser appUser = userRepository.findByPhone(verifyUserRequest.phoneNumber());
        // CASE 0: SMS WAS NOT SENT TO PHONE NUMBER
        if (appUser == null || appUser.getVerificationCode() == null) {
            responseObj.setMessage(CreateStatus.INVALID_CODE.toString());
            responseObj.setCode(Status.BAD_REQUEST);
            return responseObj;
        }
        // CASE 1: SMS WAS SEND BUT CODE IS NOT CORRECT
        String verificationCode = appUser.getVerificationCode();
        if (!verificationCode.equals(verifyUserRequest.code())) {
            responseObj.setMessage(INVALID_CODE.toString());
            responseObj.setCode(Status.BAD_REQUEST);
            return responseObj;
        }

        // CASE 2: SMS WAS SEND AND CODE IS CORRECT
        responseObj.setCode(Status.ACCEPTED);
        responseObj.setMessage(REGISTRATION_CONFIRMED.toString());
        responseObj.setToken(jwtTokenProvider.createToken(verifyUserRequest.phoneNumber(),
                                                          userRepository.findByPhone(verifyUserRequest.phoneNumber())
                                                              .getAppUserRoles()));
        // activate user (only registration)
        if (!appUser.isActive()) {
            appUser.setActive(true);
        }
        appUser.setVerificationCode(null);
        userRepository.save(appUser);

        return responseObj;
    }


    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    public void delete(String phone) {
        AppUser appUser = getUserByPhone(phone);
        PassengerEntity passengerEntity = passengerRepository.getByAppUser(appUser.getId());
        DriverEntity driver = driverRepository.getByAppUser(appUser.getId());
        if(driver != null)
        driverRepository.delete(driver);
        if(passengerEntity != null)
        passengerRepository.delete(passengerEntity);
        if(appUser != null)
        userRepository.delete(appUser);
    }


    public void addUserRole(String phone, AppUserRole appUserRole) {
        AppUser appUser = getUserByPhone(phone);
        List<AppUserRole> list = appUser.getAppUserRoles();
        list.add(appUserRole);
        appUser.setAppUserRoles(list);
        appUser.setFirstName("null");
        appUser.setLastName("null");
        userRepository.save(appUser);
    }

    public void setUserNames(AppUser appUser, String name, String lastName){
        appUser.setFirstName(name);
        appUser.setLastName(lastName);
        userRepository.save(appUser);
    }

    public AppUser getUserByPhone(String phone) {
        AppUser appUser = userRepository.findByPhone(phone);
        if (appUser == null) {
            return null;
//      throw new CustomException("The user doesn't exist", HttpStatus.NOT_FOUND);
        }
        return appUser;
    }

    public AppUser whoami(HttpServletRequest req) {
        return userRepository.findByPhone(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));
    }

    public String refresh(String username) {
        return jwtTokenProvider.createToken(username, userRepository.findByPhone(username).getAppUserRoles());
    }

}
