package codepred.ride.controller;


import codepred.passenger.model.PassengerEntity;
import codepred.passenger.service.PassengerService;
import codepred.ride.dto.Pagination;
import codepred.ride.dto.RideRequest;
import codepred.ride.model.RideEntity;
import codepred.ride.service.RideService;
import codepred.user.dto.ResponseObj;
import codepred.user.dto.Status;
import codepred.user.dto.UserResponseDTO;
import codepred.user.model.AppUserRole;
import codepred.user.service.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/ride")
@Api(tags = "ride")
@RequiredArgsConstructor
@CrossOrigin
public class RideController {

    private final UserService userService;

    private final PassengerService passengerService;
    private final ModelMapper modelMapper;

    private final RideService rideService;

    @PostMapping(value = "/add")
    @PreAuthorize("hasRole('ROLE_NONE')")
    @ApiOperation(value = "${UserController.me}", response = UserResponseDTO.class, authorizations = { @Authorization(value="apiKey") })
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public ResponseEntity<Object> addRide(HttpServletRequest req, @RequestBody RideRequest rideRequest) {
        UserResponseDTO appUser = modelMapper.map(userService.whoami(req), UserResponseDTO.class);
        ResponseObj responseObj = new ResponseObj();
        if(appUser == null){
            responseObj.setCode(Status.BAD_REQUEST);
            responseObj.setMessage("USER_NOT_FOUND");
            return ResponseEntity.status(400).body(responseObj);
        }
        RideEntity ride = rideService.addRide(appUser.getPhone(),rideRequest);
        responseObj.setCode(Status.ACCEPTED);
        responseObj.setMessage("RIDE_WAS_ADDED");
        return ResponseEntity.status(200).body(responseObj);
    }

    @PostMapping("/list")
    public ResponseEntity<Object> getAllRides(@RequestBody Pagination pagination){
        return ResponseEntity.status(200).body(rideService.getRideList(pagination.getPageNumber()));
    }

    @PostMapping("/delete")
    public ResponseEntity<Object> deleteALl(){
        rideService.deleteALl();
        return ResponseEntity.status(200).body("Deleted");
    }
}
