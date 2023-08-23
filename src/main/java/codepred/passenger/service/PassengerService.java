package codepred.passenger.service;


import codepred.passenger.model.PassengerEntity;
import codepred.passenger.repository.PassengerRepository;
import codepred.customer.model.AppUser;
import codepred.customer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PassengerService {

    @Autowired
    UserService userService;

    @Autowired
    PassengerRepository passengerRepository;

    public PassengerEntity addPassenger(String phone){
        AppUser appUser = userService.getUserByPhone(phone);
        PassengerEntity passengerEntity = new PassengerEntity();
        passengerEntity.setAppUser(appUser);
        return passengerRepository.save(passengerEntity);
    }

    public PassengerEntity getPassenger(AppUser appUser){
        return passengerRepository.getByAppUser(appUser.getId());
    }

}
