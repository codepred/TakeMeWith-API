package codepred.customer.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserVerifyDto {

    @ApiModelProperty(position = 0)
    private String phone;
    @ApiModelProperty(position = 1)
    private String code;

}
