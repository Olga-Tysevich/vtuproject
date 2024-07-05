package by.vtu.vtuproject;

import by.vtu.vtuproject.entity.Account;
import by.vtu.vtuproject.entity.Bookmark;
import by.vtu.vtuproject.repository.AccountRepository;
import by.vtu.vtuproject.repository.BookmarkRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Arrays;

@SpringBootApplication
public class VtuprojectApplication {

    @Bean
    public FilterRegistrationBean corsFilter(@Value("${tagit.origin:http://localhost:9000}") String origin) {
        return new FilterRegistrationBean(new Filter() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
                HttpServletRequest req = (HttpServletRequest) request;
                HttpServletResponse resp = (HttpServletResponse) response;
                String method = req.getMethod();
                resp.setHeader("Access-Control-Allow-Origin", origin);
                resp.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
                resp.setHeader("Access-Control-Max-Age", Long.toString(60 * 60));
                resp.setHeader("Access-Control-Allow-Credentials", "true");
                resp.setHeader("Access-Control-Allow-Headers", "Origin,Accept,X-Requested-With,ContentType,Access-Control-Request-Headers,Authorization");
                if ("OPTIONS".equals(method)) {
                    resp.setStatus(HttpStatus.OK.value());
                } else {
                    chain.doFilter(request, response);
                }
            }

            @Override
            public void init(FilterConfig filterConfig) throws ServletException {
                Filter.super.init(filterConfig);
            }

            @Override
            public void destroy() {
                Filter.super.destroy();
            }
        });
    }

    @Bean
    CommandLineRunner init(AccountRepository accountRepository,
                           BookmarkRepository bookmarkRepository) {
        return (evt) -> Arrays.asList(
                        "jhoeller,dsyer,pwebb,ogierke,rwinch,mfisher,mpollack,jlong".split(","))
                .forEach(
                        a -> {
                            Account account = accountRepository.save(new Account(a,
                                    "password"));
                            bookmarkRepository.save(new Bookmark(account,
                                    "http://bookmark.com/1/" + a, "A description"));
                            bookmarkRepository.save(new Bookmark(account,
                                    "http://bookmark.com/2/" + a, "A description"));
                        });
    }


    public static void main(String[] args) {
        SpringApplication.run(VtuprojectApplication.class, args);
    }

}
