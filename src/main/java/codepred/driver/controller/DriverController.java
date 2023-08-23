package codepred.driver.controller;


import codepred.driver.dto.DriverRequest;
import codepred.driver.model.DriverEntity;
import codepred.driver.service.DriverService;
import codepred.customer.dto.ResponseObj;
import codepred.customer.dto.Status;
import codepred.customer.dto.UserResponseDTO;
import codepred.customer.model.AppUserRole;
import codepred.customer.service.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/driver")
@Api(tags = "driver")
@RequiredArgsConstructor
@CrossOrigin
public class DriverController {

    private final UserService userService;

    private final DriverService driverService;
    private final ModelMapper modelMapper;


    @PostMapping(value = "/add")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_NONE')")
    @ApiOperation(value = "${UserController.me}", response = UserResponseDTO.class, authorizations = { @Authorization(value="apiKey") })
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public ResponseEntity<Object> addDriver(HttpServletRequest req, @RequestBody DriverRequest driverRequest) {
        ResponseObj responseObj = new ResponseObj();
        responseObj.setCode(Status.BAD_REQUEST);

        UserResponseDTO appUser = modelMapper.map(userService.whoami(req), UserResponseDTO.class);
        if(appUser == null){
            responseObj.setMessage("USER_NOT_FOUND");
            return ResponseEntity.status(400).body(responseObj);
        }
        if(appUser.getAppUserRoles().contains(AppUserRole.ROLE_PASSENGER)){
            responseObj.setCode(Status.BAD_REQUEST);
            responseObj.setMessage("PASSENGER_ACCOUNT_ALREADY_ADDED");
            return ResponseEntity.status(400).body(responseObj);
        }

        if(appUser.getAppUserRoles().contains(AppUserRole.ROLE_DRIVER)){
            responseObj.setMessage("DRIVER_ACCOUNT_ALREADY_ADDED");
            return ResponseEntity.status(400).body(responseObj);
        }

        userService.addUserRole(appUser.getPhone(), AppUserRole.ROLE_DRIVER);
        DriverEntity driverEntity = driverService.addDriver(driverRequest, appUser.getPhone());
        responseObj.setCode(Status.ACCEPTED);
        responseObj.setMessage("DRIVER_WAS_ADDED");

        return ResponseEntity.status(200).body(responseObj);
    }

}
