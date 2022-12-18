package codepred.ride.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
public class Pagination {

    @NotNull
    private String token;

    @NotNull
    private int pageNumber;

}