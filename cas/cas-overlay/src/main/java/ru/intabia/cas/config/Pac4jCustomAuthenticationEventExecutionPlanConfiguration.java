package ru.intabia.cas.config;

import org.apache.commons.lang3.StringUtils;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.configuration.model.support.pac4j.Pac4jGenericClientProperties;
import org.apereo.cas.configuration.support.RequiresModule;
import org.apereo.cas.support.pac4j.config.support.authentication.Pac4jAuthenticationEventExecutionPlanConfiguration;
import org.pac4j.core.client.BaseClient;
import org.pac4j.core.client.Client;
import org.pac4j.core.client.Clients;
import org.pac4j.oauth.client.VkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

/**
 * Extension of CAS social identity providers with Pac4j.
 */
@RequiresModule(
        name = "cas-server-support-pac4j-webflow"
)
@Configuration("pac4jCustomAuthenticationEventExecutionPlanConfiguration")
@EnableConfigurationProperties({CasConfigurationProperties.class, CustomAuthenticationProperties.class})
public class Pac4jCustomAuthenticationEventExecutionPlanConfiguration extends Pac4jAuthenticationEventExecutionPlanConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(Pac4jCustomAuthenticationEventExecutionPlanConfiguration.class);

    @Autowired
    private CasConfigurationProperties casProperties;

    @Autowired
    private CustomAuthenticationProperties customAuthenticationProperties;

    private void configureVkClient(final Collection<BaseClient> properties) {
        final Pac4jGenericClientProperties vkontakteProperties = customAuthenticationProperties.getVkontakte();
        final VkClient client = new VkClient(vkontakteProperties.getId(), vkontakteProperties.getSecret());
        if (StringUtils.isNotBlank(vkontakteProperties.getId()) && StringUtils.isNotBlank(vkontakteProperties.getSecret())) {
            setClientName(client, vkontakteProperties.getClientName());
            if (StringUtils.isNotBlank(VkClient.DEFAULT_SCOPE)) {
                client.setScope(VkClient.DEFAULT_SCOPE);
            }
            LOGGER.debug("Created client [{}] with identifier [{}]", client.getName(), client.getKey());
            properties.add(client);
        }
    }

    private void setClientName(final BaseClient client, final String clientName) {
        if (StringUtils.isNotBlank(clientName)) {
            client.setName(clientName);
        }
    }

    @Override
    @RefreshScope
    @Bean
    public Clients builtClients() {
        List<Client> clientList = new ArrayList<>();

        // built default clients
        Clients clients = super.builtClients();
        if (clients != null) {
            clientList.addAll(clients.getClients());
        }

        // add clients
        List<BaseClient> customClients = new ArrayList<>();
        configureVkClient(customClients);
        clientList.addAll(customClients);

        LOGGER.debug("The following clients are built additional: [{}]", customClients);
        if (customClients.isEmpty()) {
            LOGGER.warn("No additional delegated authentication clients are defined/configured");
        }

        LOGGER.info("Located and prepared [{}] delegated authentication client(s)", clientList.size());
        return new Clients(casProperties.getServer().getLoginUrl(), clientList);
    }
}
