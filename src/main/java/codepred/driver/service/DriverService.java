package codepred.driver.service;

import codepred.driver.dto.DriverRequest;
import codepred.driver.model.DriverEntity;
import codepred.driver.repository.DriverRepository;
import codepred.customer.model.AppUser;
import codepred.customer.repository.UserRepository;
import codepred.customer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DriverService {

    @Autowired
    DriverRepository driverRepository;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    public DriverEntity addDriver(DriverRequest driverRequest, String phone){
        AppUser appUser = userService.getUserByPhone(phone);
        DriverEntity driverEntity = new DriverEntity();
        driverEntity.setVin(driverRequest.getVin());
        driverEntity.setCarColor(driverRequest.getCarColor());
        driverEntity.setNumberOfSeats(driverRequest.getNumberOfSeats());
        driverEntity.setCarModel(driverRequest.getCarModel());
        driverEntity.setAppUser(appUser);
        userService.setUserNames(appUser, driverRequest.getName(),driverRequest.getLastname());
        return driverRepository.save(driverEntity);
    }



    public DriverEntity getDriver(AppUser appUser){
        return driverRepository.getByAppUser(appUser.getId());
    }
}
