package codepred.ride.dto;

import java.time.LocalDate;

public record RideDataRequest(String start, String destination, LocalDate startDate) {


}
