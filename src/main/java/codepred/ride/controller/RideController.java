package codepred.ride.controller;


import codepred.customer.model.AppUser;
import codepred.ride.dto.RideDataRequest;
import codepred.ride.dto.SubmitRideRequest;
import codepred.ride.repository.RideRepository;
import codepred.ride.service.RideService;
import codepred.customer.dto.ResponseObj;
import codepred.customer.service.UserService;
import io.swagger.annotations.*;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
@Api(tags = "ride")
@RequiredArgsConstructor
@CrossOrigin
public class RideController {

    private final UserService userService;
    private final RideService rideService;

    @PostMapping("/submit-ride'")
    @ApiOperation(value = "${UserController.verifyCode}")
    @ApiResponses(value = {//
        @ApiResponse(code = 400, message = "Something went wrong"), //
        @ApiResponse(code = 403, message = "Access denied"), //
        @ApiResponse(code = 422, message = "Invalid username/password supplied")})
    public ResponseEntity<Object> submitRide(@RequestBody SubmitRideRequest submitRideRequest, HttpServletRequest req) {
        AppUser appUser = userService.whoami(req);
        return ResponseEntity.status(200).body(rideService.submitRide(submitRideRequest, appUser));
    }

    @GetMapping("/number-of-pages")
    @ApiOperation(value = "${UserController.verifyCode}")
    @ApiResponses(value = {//
        @ApiResponse(code = 400, message = "Something went wrong"), //
        @ApiResponse(code = 403, message = "Access denied"), //
        @ApiResponse(code = 422, message = "Invalid username/password supplied")})
    public ResponseEntity<Object> numberOfPages(HttpServletRequest req) {
        AppUser appUser = userService.whoami(req);
        return ResponseEntity.status(200).body(1);
    }

    @PostMapping("/ride-list")
    @ApiOperation(value = "${UserController.verifyCode}")
    @ApiResponses(value = {//
        @ApiResponse(code = 400, message = "Something went wrong"), //
        @ApiResponse(code = 403, message = "Access denied"), //
        @ApiResponse(code = 422, message = "Invalid username/password supplied")})
    public ResponseEntity<Object> rideList(@RequestBody RideDataRequest rideDataRequest, HttpServletRequest req) {
        AppUser appUser = userService.whoami(req);
        return ResponseEntity.status(200).body(rideService.getRideList(rideDataRequest));
    }

}
