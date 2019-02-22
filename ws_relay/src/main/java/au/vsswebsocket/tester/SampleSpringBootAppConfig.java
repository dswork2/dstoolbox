package au.vsswebsocket.tester;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@Configuration
@EnableOAuth2Client
public class SampleSpringBootAppConfig {
    @Value("${autonomic.oauth.tokenUrl}")
    private String tokenUrl;

    @Value("${autonomic.oauth.clientId}")
    private String clientId;

    @Value("${autonomic.oauth.clientSecret}")
    private String clientSecret;

    @Bean
    protected OAuth2RestTemplate oauth2RestTemplate() {
        AccessTokenRequest atr = new DefaultAccessTokenRequest();
        ClientCredentialsResourceDetails resource = new ClientCredentialsResourceDetails();
        resource.setAccessTokenUri(tokenUrl);
        resource.setClientId(clientId);
        resource.setClientSecret(clientSecret);
        return new OAuth2RestTemplate(resource,
                new DefaultOAuth2ClientContext(atr));
    }
}
