package nextstep.auth;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import nextstep.auth.application.UserDetailsService;
import nextstep.auth.authentication.AuthenticationInterceptor;
import nextstep.auth.authentication.after.SessionAfterAuthentication;
import nextstep.auth.authentication.after.TokenAfterAuthentication;
import nextstep.auth.authentication.converter.SessionAuthenticationConverter;
import nextstep.auth.authentication.converter.TokenAuthenticationConverter;
import nextstep.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.authorization.SecurityContextPersistenceInterceptor;
import nextstep.auth.authorization.converter.SessionSecurityContextConverter;
import nextstep.auth.authorization.converter.TokenSecurityContextConverter;
import nextstep.auth.token.JwtTokenProvider;

@RequiredArgsConstructor
@Configuration
public class AuthConfig implements WebMvcConfigurer {
    public static final String SESSION_LOGIN_REQUEST_URI = "/login/session";
    public static final String TOKEN_LOGIN_REQUEST_URI = "/login/token";


    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionInterceptor()).addPathPatterns(SESSION_LOGIN_REQUEST_URI);
        registry.addInterceptor(tokenInterceptor()).addPathPatterns(TOKEN_LOGIN_REQUEST_URI);
        registry.addInterceptor(securityContextPersistenceInterceptor());
    }

    private AuthenticationInterceptor sessionInterceptor() {
        return new AuthenticationInterceptor(
            userDetailsService, new SessionAuthenticationConverter(), new SessionAfterAuthentication()
        );
    }

    private AuthenticationInterceptor tokenInterceptor() {
        return new AuthenticationInterceptor(
            userDetailsService, new TokenAuthenticationConverter(), new TokenAfterAuthentication(new ObjectMapper(), jwtTokenProvider)
        );
    }

    private SecurityContextPersistenceInterceptor securityContextPersistenceInterceptor() {
        return new SecurityContextPersistenceInterceptor(Arrays.asList(
            new SessionSecurityContextConverter(),
            new TokenSecurityContextConverter(jwtTokenProvider)
        ));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}