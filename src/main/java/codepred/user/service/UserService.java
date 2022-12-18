package codepred.user.service;

import javax.servlet.http.HttpServletRequest;

import codepred.driver.model.DriverEntity;
import codepred.driver.repository.DriverRepository;
import codepred.passenger.model.PassengerData;
import codepred.passenger.model.PassengerEntity;
import codepred.passenger.repository.PassengerRepository;
import codepred.user.dto.CreateResponse;
import codepred.user.dto.CreateStatus;
import codepred.user.dto.ResponseObj;
import codepred.user.dto.Status;
import codepred.user.model.AppUserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import codepred.user.exception.CustomException;
import codepred.user.model.AppUser;
import codepred.user.repository.UserRepository;
import codepred.user.security.JwtTokenProvider;
import codepred.sms.SmsService;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collections;

import static codepred.user.dto.CreateStatus.*;

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

    public ResponseObj signin(String phone) {
        AppUser appUser = userRepository.findByPhone(phone);
        if (appUser == null) {
            appUser = createNewUser(phone);
        }

        String code = smsService.sendSms(phone);
        appUser.setPassword(passwordEncoder.encode(code));
        appUser.setVerificationCode(code);
        userRepository.save(appUser);

        ResponseObj responseObj = new ResponseObj();
        responseObj.setCode(Status.ACCEPTED);
        responseObj.setMessage("SMS_WITH_VERIFICATION_CODE_WAS_SENT");
        return responseObj;
    }

    public AppUser createNewUser(String phone) {
        AppUser appUser = new AppUser();
        appUser.setPhone(phone);
        appUser.setActive(false);
        appUser.setFirstName("not active");
        appUser.setLastName("not active");
        List<AppUserRole> list = new ArrayList<>();
        list.add(AppUserRole.ROLE_NONE);
        appUser.setAppUserRoles(list);
        return appUser;
    }

    public ResponseObj verifyCode(String phone, String password) {
        ResponseObj responseObj = new ResponseObj();
        AppUser appUser = userRepository.findByPhone(phone);
        // CASE 0: SMS WAS NOT SENT TO PHONE NUMBER
        if (appUser == null || appUser.getVerificationCode() == null) {
            responseObj.setMessage(CreateStatus.SMS_WAS_NOT_SENT.toString());
            responseObj.setCode(Status.BAD_REQUEST);
            return responseObj;
        }
        // CASE 1: SMS WAS SEND BUT CODE IS NOT CORRECT
        String verificationCode = appUser.getVerificationCode();
        if (!verificationCode.equals(password)) {
            responseObj.setMessage(WRONG_CODE.toString());
            responseObj.setCode(Status.BAD_REQUEST);
            return responseObj;
        }

        // CASE 2: SMS WAS SEND AND CODE IS CORRECT
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(phone, password));
        responseObj.setCode(Status.ACCEPTED);
        responseObj.setMessage("CODE_WAS_CORRECT");
        responseObj.setToken(jwtTokenProvider.createToken(phone, userRepository.findByPhone(phone).getAppUserRoles()));
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
