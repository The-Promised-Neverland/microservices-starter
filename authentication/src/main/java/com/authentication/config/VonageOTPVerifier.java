package com.authentication.config;

import com.vonage.client.VonageClient;
import com.vonage.client.verify.CheckResponse;
import com.vonage.client.verify.VerifyClient;
import com.vonage.client.verify.VerifyStatus;
import org.springframework.stereotype.Component;

@Component
public class VonageOTPVerifier {
    String apiKey = "72725217";
    String apiSecret = "8etSHJbMIV4b2sYv";

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
