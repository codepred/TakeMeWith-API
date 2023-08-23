package codepred.ride.model;

import codepred.driver.model.DriverEntity;
import codepred.passenger.model.PassengerEntity;
import codepred.customer.model.AppUserRole;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class RideEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @ManyToOne
    private DriverEntity driverEntity;

    @ManyToOne
    private PassengerEntity passengerEntity;

    private String startPoint;

    private String startDate;

    private String endPoint;

    private Point point;

    private int numberOfSeats;

    private AppUserRole type;

}
