package ru.intabia.authenticator;

import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.authentication.ConfigurableAuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.ArrayList;
import java.util.List;

public class SmsOtpAuthenticatorFactory implements AuthenticatorFactory, ConfigurableAuthenticatorFactory {

    public static final String PROVIDER_ID = "sms-otp-authenticator";
    private static final SmsOtpAuthenticator SINGLETON = new SmsOtpAuthenticator();

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public Authenticator create(KeycloakSession session) {
        return SINGLETON;
    }

    private static AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
            AuthenticationExecutionModel.Requirement.REQUIRED,
            AuthenticationExecutionModel.Requirement.DISABLED
    };
    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return REQUIREMENT_CHOICES;
    }

    @Override
    public boolean isUserSetupAllowed() {
        return true;
    }

    @Override
    public boolean isConfigurable() {
        return true;
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return configProperties;
    }

    private static final List<ProviderConfigProperty> configProperties = new ArrayList<ProviderConfigProperty>();

    static {
        ProviderConfigProperty property;
        property = new ProviderConfigProperty();
        property.setName("cookie.max.age");
        property.setLabel("Cookie Max Age");
        property.setType(ProviderConfigProperty.STRING_TYPE);
        property.setHelpText("Max age in seconds of the SMS_OTP_COOKIE.");
        property.setDefaultValue(SmsOtpAuthenticator.MAX_COOKIE_AGE);
        configProperties.add(property);

        ProviderConfigProperty demoUrlProperty;
        demoUrlProperty = new ProviderConfigProperty();
        demoUrlProperty.setName("custom.sms.url");
        demoUrlProperty.setLabel("Custom SMS URL");
        demoUrlProperty.setType(ProviderConfigProperty.STRING_TYPE);
        demoUrlProperty.setHelpText("Endpoint for POST ?code={code}.");
        demoUrlProperty.setDefaultValue(SmsOtpAuthenticator.SMS_ENDPOINT_URL);
        configProperties.add(demoUrlProperty);
    }


    @Override
    public String getHelpText() {
        return "A secret question that a user has to answer. i.e. What is your mother's maiden name.";
    }

    @Override
    public String getDisplayType() {
        return "Sms OTP";
    }

    @Override
    public String getReferenceCategory() {
        return "Sms OTP";
    }

    @Override
    public void init(Config.Scope config) {

    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {

    }

    @Override
    public void close() {

    }


}
