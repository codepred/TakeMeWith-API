package codepred.ride.service;

import static codepred.util.DateUtil.addLeadingZerosToDate;

import codepred.customer.model.AppUser;
import codepred.ride.dto.RideResponse;
import codepred.ride.dto.SubmitRideRequest;
import codepred.ride.model.Ride;
import codepred.ride.repository.RideRepository;
import codepred.customer.service.UserService;
import codepred.util.DateUtil;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service
public class RideService {

    @Autowired
    UserService userService;

    @Autowired
    RideRepository rideRepository;

    public Ride submitRide(final SubmitRideRequest submitRideRequest, final AppUser appUser) {
        final var rideEntity = new Ride();
        rideEntity.setAppUser(appUser);

        rideEntity.setStart(submitRideRequest.start());
        rideEntity.setDestination(submitRideRequest.destination());
        rideEntity.setStartDate(DateUtil.convertStringToLocalDate(submitRideRequest.startDate()));
        rideEntity.setStartHour(submitRideRequest.startHour());
        rideEntity.setCreatedAt(new Date());
        return rideRepository.save(rideEntity);
    }

    public List<RideResponse> getRideList(final Integer pageNumber) {
        List<Ride> rideEntities = rideRepository.getAllMain();
        return rideEntities.stream()
            .map(r -> new RideResponse(r.getId(),
                                       r.getStart(),
                                       r.getDestination(),
                                       addLeadingZerosToDate(r.getStartDate()),
                                       r.getStartHour(),
                                       r.getAppUser().getName(),
                                       r.getAppUser().getPhoneNumber(),
                                       "photo",
                                       null))
            .collect(Collectors.toList());
    }

}
