package ru.intabia.authenticator;

import org.keycloak.credential.CredentialProvider;
import org.keycloak.credential.CredentialProviderFactory;
import org.keycloak.models.KeycloakSession;

public class SmsOtpCredentialProviderFactory implements CredentialProviderFactory<SmsOtpCredentialProvider> {
    @Override
    public String getId() {
        return "sms-otp";
    }

    @Override
    public CredentialProvider create(KeycloakSession session) {
        return new SmsOtpCredentialProvider(session);
    }
}
