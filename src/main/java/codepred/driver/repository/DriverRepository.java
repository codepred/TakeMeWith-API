package codepred.driver.repository;

import codepred.driver.model.DriverEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<DriverEntity, Integer> {

    @Query(value = "SELECT * FROM take.driver_entity where app_user_id = :app_user_id", nativeQuery = true)
    DriverEntity getByAppUser(@Param("app_user_id") Integer  app_user_id);
}
