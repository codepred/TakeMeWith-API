package codepred.passenger.controller;


import codepred.passenger.model.PassengerData;
import codepred.passenger.model.PassengerEntity;
import codepred.passenger.service.PassengerService;
import codepred.customer.dto.ResponseObj;
import codepred.customer.dto.Status;
import codepred.customer.dto.UserResponseDTO;
import codepred.customer.model.AppUser;
import codepred.customer.model.AppUserRole;
import codepred.customer.service.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/passenger")
@Api(tags = "driver")
@RequiredArgsConstructor
@CrossOrigin
public class PassengerController {

    private final UserService userService;

    private final PassengerService passengerService;
    private final ModelMapper modelMapper;

    @PostMapping(value = "/add")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_NONE')")
    @ApiOperation(value = "${UserController.me}", response = UserResponseDTO.class, authorizations = { @Authorization(value="apiKey") })
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public ResponseEntity<Object> addDriver(HttpServletRequest req, @RequestBody PassengerData passengerData) {
        ResponseObj responseObj = new ResponseObj();
        AppUser appUser = userService.whoami(req);
        if(appUser == null){
            responseObj.setCode(Status.BAD_REQUEST);
            responseObj.setMessage("USER_NOT_FOUND");
            return ResponseEntity.status(400).body(responseObj);
        }
        if(appUser.getAppUserRoles().contains(AppUserRole.ROLE_DRIVER)){
            responseObj.setCode(Status.BAD_REQUEST);
            responseObj.setMessage("DRIVER_ACCOUNT_ALREADY_ADDED");
            return ResponseEntity.status(400).body(responseObj);
        }

        if(appUser.getAppUserRoles().contains(AppUserRole.ROLE_PASSENGER)){
            responseObj.setCode(Status.BAD_REQUEST);
            responseObj.setMessage("PASSENGER_ACCOUNT_ALREADY_ADDED");
            return ResponseEntity.status(400).body(responseObj);
        }
        userService.addUserRole(appUser.getPhone(), AppUserRole.ROLE_PASSENGER);
        userService.setUserNames(appUser,passengerData.getName(), passengerData.getLastname());
        PassengerEntity passengerEntity = passengerService.addPassenger(appUser.getPhone());
        responseObj.setCode(Status.ACCEPTED);
        responseObj.setMessage("PASSENGER_WAS_ADDED");
        return ResponseEntity.status(200).body(responseObj);
    }
}
