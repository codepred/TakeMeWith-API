package codepred.ride.dto;

import java.time.LocalDate;

public record SubmitRideRequest(String start, String destination, LocalDate startDate , String startHour) {


}


