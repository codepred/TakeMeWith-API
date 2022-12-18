package codepred.ride.service;

import codepred.driver.model.DriverEntity;
import codepred.driver.service.DriverService;
import codepred.passenger.model.PassengerEntity;
import codepred.passenger.service.PassengerService;
import codepred.ride.dto.RideRequest;
import codepred.ride.model.Point;
import codepred.ride.model.RideEntity;
import codepred.ride.repository.RideRepository;
import codepred.user.model.AppUser;
import codepred.user.model.AppUserRole;
import codepred.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RideService {

    @Autowired
    UserService userService;

    @Autowired
    DriverService driverService;

    @Autowired
    PassengerService passengerService;

    @Autowired
    RideRepository rideRepository;

    public RideEntity addRide(String phone, RideRequest rideRequest){
        AppUser appUser = userService.getUserByPhone(phone);
        DriverEntity driverEntity = null;
        PassengerEntity passengerEntity = null;
        if(appUser.getAppUserRoles().contains(AppUserRole.ROLE_DRIVER)){
            driverEntity = driverService.getDriver(appUser);
        }
        if(appUser.getAppUserRoles().contains(AppUserRole.ROLE_PASSENGER)){
            passengerEntity = passengerService.getPassenger(appUser);
        }
        RideEntity rideEntity = new RideEntity();
        rideEntity.setDriverEntity(driverEntity);
        rideEntity.setPassengerEntity(passengerEntity);
        rideEntity.setStartPoint(rideRequest.getStartPoint());
        rideEntity.setEndPoint(rideRequest.getEndPoint());
        rideEntity.setStartDate(rideRequest.getStartDate().replace("T", " "));
        // TO DO - edit calculate point function
        Point point = calculatePoint(rideRequest.getStartPoint(),rideRequest.getEndPoint());
        rideEntity.setPoint(point);
        rideEntity.setNumberOfSeats(rideRequest.getNumberOfSeats());
        rideEntity.setCreatedAt(new Date());
        rideEntity.setType(appUser.getAppUserRoles().get(1));
        return rideRepository.save(rideEntity);
    }


    public Point calculatePoint(String startPoint, String endPoint){
        return new Point();
    }

    public List<RideEntity> getAllRides(){
        return rideRepository.findAll();
    }


    public Page<RideEntity> getRideList(int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber,20);
        Page<RideEntity> page = rideRepository.getAllMain(pageable);
        return page;
    }

    public void deleteALl(){
        rideRepository.deleteAll();;
    }
}
