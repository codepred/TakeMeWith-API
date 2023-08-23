package codepred.driver.model;

import codepred.customer.model.AppUser;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class DriverEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private AppUser appUser;

    private String vin;

    private String carModel;

    private String carColor;

    private int numberOfSeats;
}
