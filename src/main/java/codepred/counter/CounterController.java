package codepred.counter;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/counter")
@Api(tags = "counter")
@RequiredArgsConstructor
@CrossOrigin
public class CounterController {

    private final CounterRepository counterRepository;

    @GetMapping()
    @ApiOperation(value = "${UserController.verifyCode}")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Something went wrong"),
        @ApiResponse(code = 403, message = "Access denied"),
        @ApiResponse(code = 422, message = "Invalid username/password supplied")})
    public ResponseEntity<Object> numberOfPages() {
        return ResponseEntity.status(200).body(counterRepository.findAll().get(0).getCounter());
    }
}
