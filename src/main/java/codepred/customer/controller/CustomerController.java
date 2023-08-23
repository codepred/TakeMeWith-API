package codepred.customer.controller;

import codepred.customer.dto.ResponseObj;
import codepred.customer.dto.SignInRequest;
import codepred.customer.dto.SignUpRequest;
import codepred.customer.dto.Status;
import codepred.customer.dto.VerifyUserRequest;
import io.swagger.annotations.ApiModel;
import javax.servlet.http.HttpServletRequest;

import codepred.driver.repository.DriverRepository;
import codepred.passenger.repository.PassengerRepository;
import codepred.customer.dto.UserVerifyDto;
import codepred.customer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import codepred.customer.dto.UserResponseDTO;
import codepred.customer.service.UserService;

@RestController
@RequestMapping("/customer")
@Api(tags = "users")
@RequiredArgsConstructor
@CrossOrigin
public class CustomerController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    PassengerRepository passengerRepository;

    @Autowired
    DriverRepository driverRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/sign-up")
    @ApiOperation(value = "${UserController.login}")
    @ApiResponses(value = {//
        @ApiResponse(code = 400, message = "Something went wrong"), //
        @ApiResponse(code = 422, message = "Invalid username/password supplied")})
    public ResponseEntity<Object> register(@RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByPhone(signUpRequest.phoneNumber())) {
            return ResponseEntity.status(400)
                .body(new ResponseObj(Status.BAD_REQUEST, "PHONE_NUMBER_IS_TAKEN", null));
        }
        userService.createNewUser(signUpRequest);
        return ResponseEntity.status(200).body(new ResponseObj(Status.ACCEPTED, "DATA_SUCCESFULY_ADDED", null));
    }

    @PostMapping("/sign-in")
    @ApiOperation(value = "${UserController.login}")
    @ApiResponses(value = {//
        @ApiResponse(code = 400, message = "Something went wrong"), //
        @ApiResponse(code = 422, message = "Invalid username/password supplied")})
    public ResponseEntity<Object> login(@RequestBody SignInRequest signUpRequest) {
        return ResponseEntity.status(200).body(userService.signin(signUpRequest));
    }

    @PostMapping("/verify-user")
    @ApiOperation(value = "${UserController.verifyCode}")
    @ApiResponses(value = {//
        @ApiResponse(code = 400, message = "Something went wrong"), //
        @ApiResponse(code = 403, message = "Access denied"), //
        @ApiResponse(code = 422, message = "Invalid username/password supplied")})
    public ResponseEntity<Object> verifyCode(@RequestBody VerifyUserRequest verifyUserRequest) {
        ResponseObj responseObj = userService.verifyCode(verifyUserRequest);
        return ResponseEntity.status(userService.getResponseCode(responseObj.getCode())).body(responseObj);
    }

    @GetMapping(value = "/me")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_NONE')")
    @ApiOperation(value = "${UserController.me}", response = UserResponseDTO.class, authorizations = {
        @Authorization(value = "apiKey")})
    @ApiResponses(value = {//
        @ApiResponse(code = 400, message = "Something went wrong"), //
        @ApiResponse(code = 403, message = "Access denied"), //
        @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public UserResponseDTO whoami(HttpServletRequest req) {
        return modelMapper.map(userService.whoami(req), UserResponseDTO.class);
    }


}
