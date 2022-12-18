package codepred.user.dto;


import lombok.Data;

@Data
public class CreateResponse {

    private CreateStatus status;
    private String message;
}
