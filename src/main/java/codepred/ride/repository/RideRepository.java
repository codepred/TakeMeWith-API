package codepred.ride.repository;

import codepred.driver.model.DriverEntity;
import codepred.ride.model.RideEntity;
import codepred.user.model.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RideRepository  extends JpaRepository<RideEntity, Integer> {

    @Query(value = "SELECT * FROM ride_entity order by created_at desc", nativeQuery = true)
    Page<RideEntity> getAllMain(Pageable pageable);

}
