package by.vtu.vtuproject.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;

@Configuration
@EnableResourceServer
@EnableAuthorizationServer
@EnableWebSecurity
public class OAuth2Configuration extends AuthorizationServerConfigurerAdapter {
    private String appName = "bookmarks";

    @Autowired
    private AuthenticationManagerBuilder authenticationManager;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory().withClient("android-" + appName)
                .authorizedGrantTypes("password","authorization_code", "refresh_token")
                .authorities("ROLE_USER").scopes("write").resourceIds(appName)
                .secret("123456");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
       endpoints.authenticationManager(new AuthenticationManager() {
           @Override
           public Authentication authenticate(Authentication authentication) throws AuthenticationException {
               return authenticationManager.getOrBuild().authenticate(authentication);
           }
       });
    }


}
