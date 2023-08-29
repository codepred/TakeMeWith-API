package codepred.customer.dto;

public record NewPasswordRequest(Integer phoneNumber, Integer code, String password) {

}