package codepred.customer.controller;

import codepred.customer.dto.NewPasswordRequest;
import codepred.customer.dto.PhoneNumberRequest;
import codepred.customer.dto.ResponseObj;
import codepred.customer.dto.SignInRequest;
import codepred.customer.dto.SignUpRequest;
import codepred.enums.ResponseStatus;
import codepred.customer.dto.VerifyUserRequest;
import javax.servlet.http.HttpServletRequest;

import codepred.customer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import codepred.customer.dto.UserResponseDTO;
import codepred.customer.service.UserService;

@RestController
@RequestMapping("/customer")
@Api(tags = "customers")
@RequiredArgsConstructor
@CrossOrigin
public class CustomerController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    private final UserRepository userRepository;

    @PostMapping("/sign-up")
    @ApiOperation(value = "${UserController.login}")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Something went wrong"),
        @ApiResponse(code = 422, message = "Invalid username/password supplied")})
    public ResponseEntity<Object> register(@RequestBody SignUpRequest signUpRequest) {
        final var appUser = userRepository.findByPhoneNumber(signUpRequest.phoneNumber().toString());
        if (appUser != null && appUser.isActive()) {
            return ResponseEntity.status(400).body(new ResponseObj(ResponseStatus.BAD_REQUEST, "PHONE_NUMBER_IS_TAKEN", null));
        }
        if(appUser != null){
            userService.updateUser(signUpRequest, appUser);
        }
        else{
            userService.createNewUser(signUpRequest);
        }
        return ResponseEntity.status(200).body(new ResponseObj(ResponseStatus.ACCEPTED, "DATA_SUCCESFULY_ADDED", null));
    }

    @PostMapping("/sign-in")
    @ApiOperation(value = "${UserController.login}")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Something went wrong"),
        @ApiResponse(code = 422, message = "Invalid username/password supplied")})
    public ResponseEntity<Object> login(@RequestBody SignInRequest signUpRequest) {
        final var responseObj = userService.signin(signUpRequest);
        return ResponseEntity.status(userService.getResponseCode(responseObj.getCode())).body(responseObj);
    }

    @PostMapping("/verify-user")
    @ApiOperation(value = "${UserController.verifyCode}")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Something went wrong"),
        @ApiResponse(code = 403, message = "Access denied"),
        @ApiResponse(code = 422, message = "Invalid username/password supplied")})
    public ResponseEntity<Object> verifyCode(@RequestBody VerifyUserRequest verifyUserRequest) {
        final var responseObj = userService.verifyCode(verifyUserRequest);
        return ResponseEntity.status(userService.getResponseCode(responseObj.getCode())).body(responseObj);
    }

    @PostMapping("/request-new-password")
    @ApiOperation(value = "${UserController.verifyCode}")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Something went wrong"),
        @ApiResponse(code = 403, message = "Access denied"),
        @ApiResponse(code = 422, message = "Invalid username/password supplied")})
    public ResponseEntity<Object> requestNewPassword(@RequestBody PhoneNumberRequest phoneNumberRequest) {
        final var responseObj = userService.requestNewPassword(phoneNumberRequest);
        return ResponseEntity.status(userService.getResponseCode(responseObj.getCode())).body(responseObj);
    }

    @PostMapping("/set-new-password")
    @ApiOperation(value = "${UserController.verifyCode}")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Something went wrong"),
        @ApiResponse(code = 403, message = "Access denied"),
        @ApiResponse(code = 422, message = "Invalid username/password supplied")})
    public ResponseEntity<Object> setNewPassword(@RequestBody NewPasswordRequest newPasswordRequest) {
        final var responseObj = userService.setNewPassword(newPasswordRequest);
        return ResponseEntity.status(userService.getResponseCode(responseObj.getCode())).body(responseObj);
    }

    @GetMapping(value = "/get-user-data")
    @ApiOperation(value = "${UserController.me}", response = UserResponseDTO.class, authorizations = {
        @Authorization(value = "apiKey")})
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Something went wrong"),
        @ApiResponse(code = 403, message = "Access denied"), @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public UserResponseDTO whoami(HttpServletRequest req) {
        return modelMapper.map(userService.whoami(req), UserResponseDTO.class);
    }

}
