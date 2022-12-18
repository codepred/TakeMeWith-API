package codepred.driver.dto;


import lombok.Data;

@Data
public class DriverRequest {

    private String vin;

    private String carModel;

    private String carColor;

    private int numberOfSeats;

    private String name;

    private String lastname;
}
