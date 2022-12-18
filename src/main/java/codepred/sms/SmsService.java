package codepred.sms;

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

    public String sendSms(String phone) {
        String code = getRandomNumberString();
        String url = "https://api.smsapi.pl/sms.do?";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + "kk379QzDPO8A4bQxhiIdWVokdeeDs0YWjUGqxewG");
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
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }
}
