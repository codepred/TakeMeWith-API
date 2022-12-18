package codepred.ride.dto;

import lombok.Data;

@Data
public class RideRequest {

    private String startPoint;

    private String endPoint;

    private String startDate;

    private int numberOfSeats;

}
