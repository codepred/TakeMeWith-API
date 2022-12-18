package codepred.user.dto;


import lombok.Data;

@Data
public class ResponseObj {

    Status code;
    String message;
    String token;
}
