package codepred.user.controller;

import javax.servlet.http.HttpServletRequest;

import codepred.driver.model.DriverEntity;
import codepred.driver.repository.DriverRepository;
import codepred.passenger.model.PassengerEntity;
import codepred.passenger.repository.PassengerRepository;
import codepred.user.dto.UserVerifyDto;
import codepred.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import codepred.user.model.AppUser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import codepred.user.dto.UserDataDTO;
import codepred.user.dto.UserResponseDTO;
import codepred.user.service.UserService;

@RestController
@RequestMapping("/users")
@Api(tags = "users")
@RequiredArgsConstructor
@CrossOrigin
public class UserLoginController {

  private final UserService userService;
  private final ModelMapper modelMapper;

  @Autowired
  PassengerRepository passengerRepository;

  @Autowired
  DriverRepository driverRepository;

  @Autowired
  UserRepository userRepository;

  // LOGIN / REGISTER
  @PostMapping("/signin")
  @ApiOperation(value = "${UserController.login}")
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 422, message = "Invalid username/password supplied")})
  public ResponseEntity<Object> login(@ApiParam("phone") @RequestParam String phone) {
      return ResponseEntity.status(200).body(userService.signin(phone));
  }

  // VERIFY
  @PostMapping("/verify")
  @ApiOperation(value = "${UserController.verifyCode}")
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 403, message = "Access denied"), //
      @ApiResponse(code = 422, message = "Invalid username/password supplied")})
  public ResponseEntity<Object> verifyCode(@ApiParam("phone") @RequestBody UserVerifyDto userVerifyDto) {
      return ResponseEntity.status(200).body(userService.verifyCode(userVerifyDto.getPhone(), userVerifyDto.getCode()));
  }

  @GetMapping(value = "/me")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_NONE')")
  @ApiOperation(value = "${UserController.me}", response = UserResponseDTO.class, authorizations = { @Authorization(value="apiKey") })
  @ApiResponses(value = {//
          @ApiResponse(code = 400, message = "Something went wrong"), //
          @ApiResponse(code = 403, message = "Access denied"), //
          @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  public UserResponseDTO whoami(HttpServletRequest req) {
    return modelMapper.map(userService.whoami(req), UserResponseDTO.class);
  }


}
