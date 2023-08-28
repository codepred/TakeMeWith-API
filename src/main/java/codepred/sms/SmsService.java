package codepred.sms;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.telesign.MessagingClient;
import com.telesign.RestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;


@Service
public class SmsService {

    @Value("${sms.token}")
    private String smsToken;
    public String sendSms(String phone) {
        String code = "9" + getRandomNumberString();
        String url = "https://api.smsapi.pl/sms.do?";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + smsToken);
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("from", "Takemewith");
        map.add("to", "48" + phone);
        map.add("message", "Kod weryfikacyjny: " + code);
        map.add("format", "json");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        System.out.println("Response from bullet api:");
        System.out.println(response.toString());
        return code;
    }

    public static String getRandomNumberString() {
        // It will generate 5 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(99999);

        // this will convert any number sequence into 5 character.
        return String.format("%05d", number);
    }
}
