package codepred.passenger.repository;


import codepred.driver.model.DriverEntity;
import codepred.passenger.model.PassengerEntity;
import codepred.user.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerRepository extends JpaRepository<PassengerEntity, Integer> {

    @Query(value = "SELECT * FROM take.passenger_entity where app_user_id = :app_user_id", nativeQuery = true)
    PassengerEntity getByAppUser(@Param("app_user_id") Integer  app_user_id);

}
