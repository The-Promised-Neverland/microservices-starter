package com.authentication.config;

import com.vonage.client.VonageClient;
import com.vonage.client.verify.CheckResponse;
import com.vonage.client.verify.VerifyClient;
import com.vonage.client.verify.VerifyStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class VonageOTPVerifier {
    @Value("${vonage.api.key}")
    private String apiKey;
    @Value("${vonage.api.secret}")
    private String apiSecret;

    public boolean verifyOtp(String requestID, String otp) {
        VonageClient client = VonageClient.builder()
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .build();

        VerifyClient verifyClient = client.getVerifyClient();

        CheckResponse checkResponse = verifyClient.check(requestID,otp);

        return checkResponse.getStatus() == VerifyStatus.OK;
    }
}
