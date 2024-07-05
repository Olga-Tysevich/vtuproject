package by.vtu.vtuproject.conf;

import by.vtu.vtuproject.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {
    private final AccountRepository accountRepository;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService());
    }

    @Bean
    public UserDetailsService userDetailService() {
        return username -> accountRepository
                .findAccountByUserName(username)
                .map(a -> new User(a.getUserName(), a.getPassword(), true, true, true, true,
                        AuthorityUtils.createAuthorityList("USER", "write")))
                .orElseThrow(() -> new UsernameNotFoundException("could not find the user '" + username + "'"));
    }
}
