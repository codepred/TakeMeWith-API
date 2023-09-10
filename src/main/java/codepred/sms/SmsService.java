package codepred.sms;

import static codepred.util.NumberUtil.getRandomNumberString;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class SmsService {

    @Value("${sms.token}")
    private String smsToken;

    public String sendSms(final String phone) {
        final var code = "9" + getRandomNumberString();
        final var url = "https://api.smsapi.pl/sms.do?";
        final var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + smsToken);
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("from", "Takemewith");
        map.add("to", "48" + phone);
        map.add("message", "Kod weryfikacyjny: " + code);
        map.add("format", "json");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        log.info("Response from bullet api: {}", response);
        return code;
    }

}
