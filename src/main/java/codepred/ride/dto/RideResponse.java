package codepred.ride.dto;

import io.swagger.models.auth.In;
import java.time.LocalDate;

public record RideResponse(Integer id, String start, String destination, String startDate, String startHour, String driver,
                           String phoneNumber, String photo, Integer distanceFromStart) {


}
