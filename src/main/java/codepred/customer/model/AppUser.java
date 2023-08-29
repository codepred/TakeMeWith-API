package codepred.customer.model;

import java.util.Date;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;

@Table(name = "USERS")
@Entity
@Data
@NoArgsConstructor
public class AppUser {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  private Date createdAt;

  @Size(min = 4, max = 255, message = "Minimum username length: 4 characters")
  @Column(name = "name", nullable = false)
  private String name;

  @Size(min = 4, max = 255, message = "Minimum username length: 4 characters")
  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Size(min = 9, message = "Minimum phone length: 9 characters")
  @Column(name = "phone_number", unique = true, nullable = false)
  private String phoneNumber;

  @Column(name = "email", unique = false, nullable = false)
  private String email;

  @Size(min = 6, message = "Minimum password length: 6 characters")
  @Column(name = "password"  ,unique = true, nullable = false)
  private String password;

  @Column(name = "is_active")
  private boolean isActive;

  @Column(name = "verification_code")
  private String  verificationCode;

  @ElementCollection(fetch = FetchType.EAGER)
  @Column(name = "app_user_app_user_roles")
  List<AppUserRole> appUserRoles;

}
