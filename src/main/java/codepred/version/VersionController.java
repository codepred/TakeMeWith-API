package codepred.version;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/version")
@Api(tags = "version")
@RequiredArgsConstructor
@CrossOrigin
public class VersionController {

    @Value("${app.version}")
    private String appVersion;

    @GetMapping
    @ApiOperation(value = "${VersionController.appVersion}", response = String.class, authorizations = {
        @Authorization(value = "apiKey")})
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Something went wrong"),
        @ApiResponse(code = 403, message = "Access denied"), @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public String getAppVersion() {
        return appVersion;
    }

}
