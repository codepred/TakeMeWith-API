package codepred.ride.controller;

import codepred.customer.dto.ResponseObj;
import codepred.customer.model.AppUser;
import codepred.enums.ResponseStatus;
import codepred.ride.dto.SubmitRideRequest;
import codepred.ride.service.RideService;
import codepred.customer.service.UserService;
import io.swagger.annotations.*;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ride")
@Api(tags = "rides")
@RequiredArgsConstructor
@CrossOrigin
public class RideController {

    private final UserService userService;
    private final RideService rideService;

    @PostMapping("/submit-ride")
    @ApiOperation(value = "${UserController.verifyCode}")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Something went wrong"),
        @ApiResponse(code = 403, message = "Access denied"),
        @ApiResponse(code = 422, message = "Invalid username/password supplied")})
    public ResponseEntity<Object> submitRide(@RequestBody final SubmitRideRequest submitRideRequest, final HttpServletRequest req) {
        final var appUser = userService.whoami(req);
        rideService.submitRide(submitRideRequest, appUser);
        return ResponseEntity.status(200).body(new ResponseObj(ResponseStatus.OK, "RIDE_SUCCESSFULY_CREATED", null));
    }

    @GetMapping("/number-of-pages")
    @ApiOperation(value = "${UserController.verifyCode}")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Something went wrong"),
        @ApiResponse(code = 403, message = "Access denied"),
        @ApiResponse(code = 422, message = "Invalid username/password supplied")})
    public ResponseEntity<Object> numberOfPages(final HttpServletRequest req) {
        final var appUser = userService.whoami(req);
        return ResponseEntity.status(200).body(1);
    }

    @PostMapping("/ride-list/{pageNumber}")
    @ApiOperation(value = "${UserController.verifyCode}")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Something went wrong"),
        @ApiResponse(code = 403, message = "Access denied"),
        @ApiResponse(code = 422, message = "Invalid username/password supplied")})
    public ResponseEntity<Object> rideList(@PathVariable final Integer pageNumber) {
        return ResponseEntity.status(200).body(rideService.getRideList(pageNumber));
    }

}
