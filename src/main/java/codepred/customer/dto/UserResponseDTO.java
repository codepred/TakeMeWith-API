package codepred.customer.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import codepred.customer.model.AppUserRole;

@Data
public class UserResponseDTO {

  @Schema(description = "code from backend", example = "1jbf31j4h3bt4jb12j4")
  private String name;

  @Schema(description = "code from backend", example = "1jbf31j4h3bt4jb12j4")
  private String lastName;

  @Schema(description = "code from backend", example = "1jbf31j4h3bt4jb12j4")
  private String phoneNUmber;

  @Schema(description = "code from backend", example = "1jbf31j4h3bt4jb12j4")
  private String email;

  @Schema(description = "code from backend", example = "1jbf31j4h3bt4jb12j4")
  private List<AppUserRole> appUserRoles;

}
