package codepred.customer.dto;


import codepred.enums.ResponseStatus;
import lombok.Data;

@Data
public class ResponseObj {

    ResponseStatus code;
    String message;
    String token;

    public ResponseObj() {
    }

    public ResponseObj(ResponseStatus code, String message, String token) {
        this.code = code;
        this.message = message;
        this.token = token;
    }
}
