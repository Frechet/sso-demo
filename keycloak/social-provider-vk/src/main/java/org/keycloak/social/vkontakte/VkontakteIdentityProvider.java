package org.keycloak.social.vkontakte;

import com.fasterxml.jackson.databind.JsonNode;
import org.jboss.logging.Logger;
import org.keycloak.broker.oidc.AbstractOAuth2IdentityProvider;
import org.keycloak.broker.oidc.OAuth2IdentityProviderConfig;
import org.keycloak.broker.oidc.mappers.AbstractJsonUserAttributeMapper;
import org.keycloak.broker.provider.BrokeredIdentityContext;
import org.keycloak.broker.provider.IdentityBrokerException;
import org.keycloak.broker.provider.util.SimpleHttp;
import org.keycloak.broker.social.SocialIdentityProvider;
import org.keycloak.models.KeycloakSession;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Borovlev Victor
 */
public class VkontakteIdentityProvider
        extends AbstractOAuth2IdentityProvider
        implements SocialIdentityProvider {

    private static final Logger logger = Logger.getLogger(VkontakteIdentityProvider.class);

    public static final String AUTH_URL = "https://oauth.vk.com/authorize";
    public static final String TOKEN_URL = "https://oauth.vk.com/access_token";
    public static final String USER_URL = "https://api.vk.com/method/users.get";
    public static final String DEFAULT_SCOPE = "email";

    public static final String AUTH2_PARAMETER_USER_ID = "user_id";
    public static final String AUTH2_PARAMETER_EMAIL = "email";

    public VkontakteIdentityProvider(KeycloakSession session, OAuth2IdentityProviderConfig config) {
        super(session, config);
        config.setAuthorizationUrl(AUTH_URL);
        config.setTokenUrl(TOKEN_URL);
    }

    @Override
    protected boolean supportsExternalExchange() {
        return true;
    }

    @Override
    public BrokeredIdentityContext getFederatedIdentity(String response) {
        logger.info(response);

        String accessToken = extractTokenFromResponse(response, getAccessTokenResponseParameter());
        String userId = extractNumberFromResponse(response, getUserIdResponseParameter());
        String email = extractTokenFromResponse(response, getEmailResponseParameter());

        if (accessToken == null) {
            throw new IdentityBrokerException(
                    "No access token available in OAuth vkontakte server response: " + response);
        }

        if (userId == null) {
            throw new IdentityBrokerException(
                    "No user id available in OAuth vkontakte server response: " + response);
        }

        BrokeredIdentityContext context = doGetFederatedIdentity(accessToken, userId, email);
        context.getContextData().put(FEDERATED_ACCESS_TOKEN, accessToken);
        return context;
    }

    protected String extractNumberFromResponse(String response, String nodeName) {
        if(response == null)
            return null;

        if (response.startsWith("{")) {
            try {
                JsonNode node = mapper.readTree(response);
                if(node.has(nodeName)){
                    Number s = node.get(nodeName).numberValue();
                    if(s == null)
                        return null;
                    return s.toString();
                } else {
                    return null;
                }
            } catch (IOException e) {
                throw new IdentityBrokerException("Could not extract number [" + nodeName + "] from response [" +
                        response + "] due: " + e.getMessage(), e);
            }
        } else {
            Matcher matcher = Pattern.compile(nodeName + "=([^&]+)").matcher(response);

            if (matcher.find()) {
                return matcher.group(1);
            }
        }

        return null;
    }

    protected String getUserIdResponseParameter() {
        return AUTH2_PARAMETER_USER_ID;
    }

    protected String getEmailResponseParameter() {
        return AUTH2_PARAMETER_EMAIL;
    }

    protected BrokeredIdentityContext doGetFederatedIdentity(String accessToken, String userId, String email) {
        try {
            String userRequestURL = USER_URL + "?user_ids=" + userId + "&access_token=" + accessToken;
            JsonNode profile = SimpleHttp.doGet(userRequestURL, session)
                    .asJson().findValue("response").elements().next();

            if (profile == null) {
                throw new IdentityBrokerException("Could not obtain account information from vkontakte.");
            }
            return extractUserInfo(userId, email, profile);
        } catch (Exception e) {
            if (e instanceof IdentityBrokerException) throw (IdentityBrokerException)e;
            throw new IdentityBrokerException("Could not obtain user profile from vkontakte.", e);
        }
    }

    private BrokeredIdentityContext extractUserInfo(String accountId, String email, JsonNode profile) {
        BrokeredIdentityContext user = new BrokeredIdentityContext(accountId);

        String firstName = getJsonProperty(profile, "first_name");
        String lastName = getJsonProperty(profile, "last_name");
        String username = firstName + " " + lastName;
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setEmail(email);
        user.setIdpConfig(getConfig());
        user.setIdp(this);

        AbstractJsonUserAttributeMapper.storeUserProfileForMapper(user, profile, getConfig().getAlias());

        return user;
    }

    @Override
    protected String getDefaultScopes() {
        return DEFAULT_SCOPE;
    }
}
