package codepred.ride.repository;

import codepred.ride.model.RideEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RideRepository  extends JpaRepository<RideEntity, Integer> {

    @Query(value = "SELECT * FROM ride order by start_date asc", nativeQuery = true)
    List<RideEntity> getAllMain();

}
