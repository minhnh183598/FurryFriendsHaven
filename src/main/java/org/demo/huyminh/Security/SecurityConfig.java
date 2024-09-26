package org.demo.huyminh.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;


/**
 * @author Minh
 * Date: 9/24/2024
 * Time: 1:38 PM
 */

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final String[] SECURED_ENDPOINTS = {};
    private final String[] UNSECURED_ENDPOINTS = {
            "/api/v1/auth/login",
            "/api/v1/auth/introspect",
            "/api/v1/users",
            "/api/v1/auth/logout",
            "/api/v1/auth/refresh"
    };

    @Autowired
    private CustomJwtDecoder jwtDecoder;

    @Value("${jwt.signerKey}")
    private String SECRET_KEY;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors(AbstractHttpConfigurer::disable);
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        httpSecurity.authorizeHttpRequests(request ->
                request.requestMatchers(UNSECURED_ENDPOINTS).permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/v1/users").hasAuthority("ROLE_ADMIN")
//                        .requestMatchers(HttpMethod.GET, "/api/v1/users").hasRole(Roles.ADMIN.name())
                        .anyRequest().authenticated()
        );

        //Provide JWT Authentication Provider a decoder to verify JWT signature
        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder)
                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        //When OAuth2 authentication fails, return 401
                        .authenticationEntryPoint(new JWTAuthenticationEntryPoint())
        );

        return httpSecurity.build();
    }

//    @Bean
//    JwtDecoder jwtDecoder() {
//        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "HS512");
//        return NimbusJwtDecoder
//                .withSecretKey(keySpec)
//                .macAlgorithm(MacAlgorithm.HS512)
//                .build();
//    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
//        converter.setAuthorityPrefix("ROLE_");
        converter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(converter);
        return jwtAuthenticationConverter;
    }

}
