package codepred.customer.dto;

import codepred.customer.model.AppUser;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import codepred.customer.model.AppUserRole;
import org.hibernate.annotations.Source;

@Data
public class UserResponseDTO {

  @ApiModelProperty(position = 0)
  private String name;

  @ApiModelProperty(position = 1)
  private String lastName;

  @ApiModelProperty(position = 2)
  private String phoneNUmber;

  @ApiModelProperty(position = 3)
  private String email;

  @ApiModelProperty(position = 4)
  private List<AppUserRole> appUserRoles;

}
