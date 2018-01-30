package ru.intabia.authenticator;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jboss.logging.Logger;
import org.jboss.resteasy.spi.HttpResponse;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.common.util.ServerCookie;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;

import javax.ws.rs.core.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

public class SmsOtpAuthenticator implements Authenticator {

    private static final Logger logger = Logger.getLogger(SmsOtpAuthenticator.class);

    public static final String SMS_ENDPOINT_URL = "http://localhost:8080/sendsms";
    public static final String MAX_COOKIE_AGE = "3600"; // 1 hour

    public static final String CREDENTIAL_TYPE = "sms_otp";

    protected boolean hasCookie(AuthenticationFlowContext context) {
        Cookie cookie = context.getHttpRequest().getHttpHeaders().getCookies().get("SMS_OTP_CONFIRMED");
        boolean result = cookie != null;
        if (result) {
            System.out.println("Bypassing sms code because cookie is set");
        }
        return result;
    }

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        if (hasCookie(context)) {
            context.success();
            return;
        }
        Response challenge = context.form().createForm("sms-otp.ftl");
        context.challenge(challenge);

        sendSmsCode(context);
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
        if (formData.containsKey("cancel")) {
            context.cancelLogin();
            return;
        }
        if (formData.containsKey("resend")) {
            sendSmsCode(context);
            return;
        }
        boolean validated = validateAnswer(context);
        if (!validated) {
            Response challenge =  context.form()
                    .setError("badSecret")
                    .createForm("sms-otp.ftl");
            context.failureChallenge(AuthenticationFlowError.INVALID_CREDENTIALS, challenge);
            return;
        }
        setCookie(context);
        context.success();
    }

    protected void sendSmsCode(AuthenticationFlowContext context) {
        // read settings
        AuthenticatorConfigModel config = context.getAuthenticatorConfig();
        String url = SMS_ENDPOINT_URL;
        if (config != null) {
            url = config.getConfig().get("custom.sms.url");
        }

        // set code in user credential
        UserCredentialModel input = new UserCredentialModel();
        input.setType(SmsOtpCredentialProvider.SMS_OTP);
        input.setValue("123");
        context.getSession().userCredentialManager().updateCredential(context.getRealm(), context.getUser(), input);

        // send sms code
        try {
            HttpClient client = HttpClientBuilder.create().build();

            logger.info("POST code 123 to " + url);

            URI uri = new URIBuilder(url)
                    .addParameter("code", "123")
                    .build();
            HttpPost request = new HttpPost(uri);
            client.execute(request);
        }
        catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void setCookie(AuthenticationFlowContext context) {
        AuthenticatorConfigModel config = context.getAuthenticatorConfig();
        int maxCookieAge = Integer.valueOf(MAX_COOKIE_AGE);
        if (config != null) {
            try {
                maxCookieAge = Integer.valueOf(config.getConfig().get("cookie.max.age"));
            }
            catch (NumberFormatException e) {}
        }
        URI uri = context.getUriInfo().getBaseUriBuilder().path("realms").path(context.getRealm().getName()).build();
        addCookie("SMS_OTP_CONFIRMED", "true",
                uri.getRawPath(),
                null, null,
                maxCookieAge,
                false, true);
    }

    public static void addCookie(String name, String value, String path, String domain, String comment, int maxAge, boolean secure, boolean httpOnly) {
        HttpResponse response = ResteasyProviderFactory.getContextData(HttpResponse.class);
        StringBuffer cookieBuf = new StringBuffer();
        ServerCookie.appendCookieValue(cookieBuf, 1, name, value, path, domain, comment, maxAge, secure, httpOnly);
        String cookie = cookieBuf.toString();
        response.getOutputHeaders().add(HttpHeaders.SET_COOKIE, cookie);
    }


    protected boolean validateAnswer(AuthenticationFlowContext context) {
        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
        String secret = formData.getFirst("secret_answer");
        UserCredentialModel input = new UserCredentialModel();
        input.setType(SmsOtpCredentialProvider.SMS_OTP);
        input.setValue(secret);
        return context.getSession().userCredentialManager().isValid(context.getRealm(), context.getUser(), input);
    }

    @Override
    public boolean requiresUser() {
        return true;
    }

    @Override
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        // it is configured itself
        return true;
    }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
        // not required
    }

    @Override
    public void close() {
    }
}
