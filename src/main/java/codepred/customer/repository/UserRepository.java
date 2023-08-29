package codepred.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import codepred.customer.model.AppUser;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Integer> {

  AppUser findByPhoneNumber(String phone);

}
