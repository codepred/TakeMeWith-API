package codepred.ride.repository;

import codepred.ride.model.Ride;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RideRepository  extends JpaRepository<Ride, Integer> {

    @Query(value = "SELECT * FROM ride order by start_date asc", nativeQuery = true)
    List<Ride> getAllMain();

}
