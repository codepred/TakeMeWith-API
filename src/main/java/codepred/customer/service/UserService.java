package codepred.customer.service;

import codepred.customer.dto.NewPasswordRequest;
import codepred.customer.dto.PhoneNumberRequest;
import codepred.customer.dto.SignInRequest;
import codepred.customer.dto.SignUpRequest;
import codepred.customer.dto.VerifyUserRequest;
import codepred.security.JwtTokenProvider;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

import codepred.customer.dto.ResponseObj;
import codepred.enums.ResponseStatus;
import codepred.customer.model.AppUserRole;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import codepred.customer.model.AppUser;
import codepred.customer.repository.UserRepository;
import codepred.sms.SmsService;

import static codepred.enums.ResponseStatus.BAD_REQUEST;
import static codepred.enums.ResponseStatus.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    private final SmsService smsService;

    @Transactional
    public ResponseObj signin(SignInRequest signInRequest) {
        AppUser appUser = userRepository.findByPhoneNumber(signInRequest.phoneNumber().toString());
        if (appUser == null) {
            return new ResponseObj(BAD_REQUEST, "USER_NOT_EXISTS", null);
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.phoneNumber(),
                                                                                       signInRequest.password()));
        } catch (Exception e) {
            return new ResponseObj(UNAUTHORIZED, "WRONG_DATA", null);
        }

        ResponseObj responseObj = new ResponseObj();
        responseObj.setCode(ResponseStatus.ACCEPTED);
        responseObj.setMessage("CORRECT_LOGIN_DATA");
        responseObj.setToken(jwtTokenProvider.createToken(signInRequest.phoneNumber().toString(),
                                                          userRepository.findByPhoneNumber(signInRequest.phoneNumber().toString())
                                                              .getAppUserRoles()));
        return responseObj;
    }

    @Transactional
    public AppUser createNewUser(SignUpRequest signUpRequest) {
        AppUser appUser = new AppUser();
        appUser.setPhoneNumber(signUpRequest.phoneNumber().toString());
        appUser.setActive(false);
        appUser.setName(signUpRequest.name());
        appUser.setLastName(signUpRequest.lastname());
        appUser.setEmail(signUpRequest.email());
        List<AppUserRole> list = new ArrayList<>();
        list.add(AppUserRole.ROLE_NONE);
        appUser.setCreatedAt(new Date());
        appUser.setAppUserRoles(list);
        String code = smsService.sendSms(signUpRequest.phoneNumber().toString());
        appUser.setPassword(passwordEncoder.encode(signUpRequest.password()));
        appUser.setVerificationCode(code);
        userRepository.save(appUser);
        return appUser;
    }

    @Transactional
    public ResponseObj verifyCode(VerifyUserRequest verifyUserRequest) {
        ResponseObj responseObj = new ResponseObj();
        AppUser appUser = userRepository.findByPhoneNumber(verifyUserRequest.phoneNumber().toString());
        // CASE 0: SMS WAS NOT SENT TO PHONE NUMBER
        if (appUser == null || appUser.getVerificationCode() == null) {
            responseObj.setMessage("INVALID_CODE");
            responseObj.setCode(ResponseStatus.BAD_REQUEST);
            return responseObj;
        }
        // CASE 1: SMS WAS SEND BUT CODE IS NOT CORRECT
        String verificationCode = appUser.getVerificationCode();
        if (!verificationCode.equals(verifyUserRequest.code())) {
            responseObj.setMessage("INVALID_CODE");
            responseObj.setCode(ResponseStatus.BAD_REQUEST);
            return responseObj;
        }

        // CASE 2: SMS WAS SEND AND CODE IS CORRECT
        responseObj.setCode(ResponseStatus.ACCEPTED);
        responseObj.setMessage("REGISTRATION_CONFIRMED");
        responseObj.setToken(jwtTokenProvider.createToken(verifyUserRequest.phoneNumber().toString(),
                                                          userRepository.findByPhoneNumber(verifyUserRequest.phoneNumber().toString())
                                                              .getAppUserRoles()));
        // activate user (only registration)
        if (!appUser.isActive()) {
            appUser.setActive(true);
        }
        appUser.setVerificationCode(null);
        userRepository.save(appUser);

        return responseObj;
    }

    public ResponseObj requestNewPassword(PhoneNumberRequest phoneNumberRequest) {
        ResponseObj responseObj = new ResponseObj();
        AppUser appUser = userRepository.findByPhoneNumber(phoneNumberRequest.phoneNumber().toString());
        if (appUser != null) {
            String code = smsService.sendSms(phoneNumberRequest.phoneNumber().toString());
            appUser.setVerificationCode(code);
            userRepository.save(appUser);
            responseObj.setMessage("EMAIL_EXISTS");
            responseObj.setCode(ResponseStatus.ACCEPTED);
            return responseObj;
        }
        responseObj.setMessage("INVALID_PHONE_NUMBER");
        responseObj.setCode(BAD_REQUEST);
        return responseObj;
    }

    public ResponseObj setNewPassword(NewPasswordRequest newPasswordRequest) {
        ResponseObj responseObj = new ResponseObj();
        AppUser appUser = userRepository.findByPhoneNumber(newPasswordRequest.phoneNumber().toString());
        if (appUser != null) {
            if (appUser.getVerificationCode() != null && appUser.getVerificationCode().equals(newPasswordRequest.code())) {
                appUser.setPassword(passwordEncoder.encode(newPasswordRequest.password()));
                appUser.setVerificationCode(null);
                userRepository.save(appUser);
                responseObj.setCode(ResponseStatus.ACCEPTED);
                responseObj.setMessage("CORRECT_DATA");
                return responseObj;
            } else {
                responseObj.setCode(BAD_REQUEST);
                responseObj.setMessage("INVALID_CODE");
                return responseObj;
            }
        }
        responseObj.setMessage("INVALID_PHONE_NUMBER");
        responseObj.setCode(BAD_REQUEST);
        return responseObj;
    }


    public Integer getResponseCode(ResponseStatus code) {
        if (code.equals(ResponseStatus.ACCEPTED)) {
            return 200;
        } else if (code.equals(ResponseStatus.BAD_REQUEST)) {
            return 400;
        } else if (code.equals(UNAUTHORIZED)) {
            return 401;
        }
        return 500;
    }

    public AppUser whoami(HttpServletRequest req) {
        return userRepository.findByPhoneNumber(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));
    }

    public String refresh(String username) {
        return jwtTokenProvider.createToken(username, userRepository.findByPhoneNumber(username).getAppUserRoles());
    }

}
