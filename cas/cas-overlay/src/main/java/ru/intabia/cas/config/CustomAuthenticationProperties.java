package ru.intabia.cas.config;

import org.apereo.cas.configuration.model.support.pac4j.Pac4jGenericClientProperties;
import org.apereo.cas.configuration.support.RequiresModule;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Custom properties.
 */
@ConfigurationProperties(prefix = "custom.authn")
public class CustomAuthenticationProperties {

    private Vkontakte vkontakte = new CustomAuthenticationProperties.Vkontakte();

    @RequiresModule(name = "cas-server-support-pac4j-webflow")
    public static class Vkontakte extends Pac4jGenericClientProperties {
    }

    public Vkontakte getVkontakte() {
        return vkontakte;
    }

    public void setVkontakte(Vkontakte vkontakte) {
        this.vkontakte = vkontakte;
    }


}
