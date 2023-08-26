package codepred.customer.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import codepred.customer.model.AppUser;

public interface UserRepository extends JpaRepository<AppUser, Integer> {

  boolean existsByPhoneNumber(String phone);

  AppUser findByPhoneNumber(String phone);

  @Transactional
  void deleteByPhoneNumber(String phone);

}
