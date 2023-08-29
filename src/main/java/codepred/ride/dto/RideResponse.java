package codepred.ride.dto;

public record RideResponse(Integer id, String start, String destination, String startDate, String startHour, String driver,
                           String phoneNumber, String photo, Integer distanceFromStart) {


}
