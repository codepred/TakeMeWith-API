package codepred.ride.model;

import codepred.customer.model.AppUser;
import java.time.LocalDate;
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
    private AppUser appUser;

    private String start;

    private String destination;

    private LocalDate startDate;

    private String startHour;

}
