package codepred.ride.model;

import codepred.customer.model.AppUser;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Table(name = "RIDE")
@Entity
@Data
@NoArgsConstructor
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @ManyToOne(cascade = CascadeType.ALL)
    private AppUser appUser;

    @Column(name = "start")
    private String start;

    @Column(name = "destination")
    private String destination;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "start_hour")
    private String startHour;

}
