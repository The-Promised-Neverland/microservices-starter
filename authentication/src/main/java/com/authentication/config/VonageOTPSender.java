package com.authentication.config;

import com.vonage.client.VonageClient;
import com.vonage.client.VonageClientException;
import com.vonage.client.verify.VerifyClient;
import com.vonage.client.verify.VerifyRequest;
import com.vonage.client.verify.VerifyResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class VonageOTPSender {

    @Value("${vonage.api.key}")
    private String apiKey;
    @Value("${vonage.api.secret}")
    private String apiSecret;

    public String  sendOtpSms(String userPhoneNumber) {
        VonageClient client = new VonageClient.Builder()
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .build();

        VerifyClient verifyClient = client.getVerifyClient();

        try {
            VerifyRequest request = VerifyRequest.builder(userPhoneNumber, "Microservices_").build();
            VerifyResponse response = verifyClient.verify(request);
            return response.getRequestId();
        } catch (VonageClientException e) {
            e.printStackTrace();
            return null;
        }
    }
}
