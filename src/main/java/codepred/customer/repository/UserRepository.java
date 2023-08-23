package codepred.customer.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import codepred.customer.model.AppUser;

public interface UserRepository extends JpaRepository<AppUser, Integer> {

  boolean existsByPhone(String phone);

  AppUser findByPhone(String phone);

  @Transactional
  void deleteByPhone(String phone);

}
