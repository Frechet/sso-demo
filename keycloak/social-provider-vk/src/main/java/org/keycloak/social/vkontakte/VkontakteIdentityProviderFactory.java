package org.keycloak.social.vkontakte;

import org.jboss.logging.Logger;
import org.keycloak.broker.oidc.OAuth2IdentityProviderConfig;
import org.keycloak.broker.provider.AbstractIdentityProviderFactory;
import org.keycloak.models.IdentityProviderModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.broker.social.SocialIdentityProviderFactory;

/**
 * @author Borovlev Victor
 */
public class VkontakteIdentityProviderFactory
        extends AbstractIdentityProviderFactory<VkontakteIdentityProvider>
        implements SocialIdentityProviderFactory<VkontakteIdentityProvider> {

    private static final Logger logger = Logger.getLogger(VkontakteIdentityProviderFactory.class);

    public static final String PROVIDER_ID = "vkontakte";

    @Override
    public String getName() {
        return "Vkontakte";
    }

    @Override
    public VkontakteIdentityProvider create(KeycloakSession session, IdentityProviderModel model) {
        return new VkontakteIdentityProvider(session, new OAuth2IdentityProviderConfig(model));
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }
}
