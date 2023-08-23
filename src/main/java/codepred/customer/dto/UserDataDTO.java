package codepred.customer.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDataDTO {
  
  @ApiModelProperty(position = 0)
  private String firstName;
  @ApiModelProperty(position = 1)
  private String lastName;
  @ApiModelProperty(position = 2)
  private String phone;
//  @ApiModelProperty(position = 3)
//  private String password;
//  @ApiModelProperty(position = 4)
//  List<AppUserRole> appUserRoles;

}
