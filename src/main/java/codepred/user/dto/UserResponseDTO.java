package codepred.user.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import codepred.user.model.AppUserRole;

@Data
public class UserResponseDTO {

  @ApiModelProperty(position = 0)
  private Integer id;
  @ApiModelProperty(position = 1)
  private String firstName;
  @ApiModelProperty(position = 2)
  private String lastName;
  @ApiModelProperty(position = 2)
  private String phone;
  @ApiModelProperty(position = 3)
  List<AppUserRole> appUserRoles;

}
