package codepred.customer.dto;


import lombok.Data;

@Data
public class ResponseObj {

    Status code;
    String message;
    String token;

    public ResponseObj() {
    }

    public ResponseObj(Status code, String message, String token) {
        this.code = code;
        this.message = message;
        this.token = token;
    }
}
