package com.authentication.config;

import com.vonage.client.VonageClient;
import com.vonage.client.VonageClientException;
import com.vonage.client.verify.VerifyClient;
import com.vonage.client.verify.VerifyRequest;
import com.vonage.client.verify.VerifyResponse;
import org.springframework.stereotype.Component;

@Component
public class VonageOTPSender {
    public String  sendOtpSms(String userPhoneNumber) {
        String apiKey;
        String apiSecret;

        VonageClient client = new VonageClient.Builder()
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .build();

        VerifyClient verifyClient = client.getVerifyClient();

        try {
            VerifyRequest request = VerifyRequest.builder(userPhoneNumber, "RainX Microservices Project").build();
            VerifyResponse response = verifyClient.verify(request);
            return response.getRequestId();
        } catch (VonageClientException e) {
            e.printStackTrace();
            return null;
        }
    }
}
