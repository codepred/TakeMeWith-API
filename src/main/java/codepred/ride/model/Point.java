package codepred.ride.model;


import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class Point {

    private double startLat;
    private double startLng;
    private double endLat;
    private double endLng;
}
