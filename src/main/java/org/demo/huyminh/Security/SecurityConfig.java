package org.demo.huyminh.Security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;


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
    private final String[] UNSECURED_ENDPOINTS = new String[]{
            "/api/v1/auth/**",
            "/api/v1/users",
            "/api/v1/payment/**",
            "/api/v1/pets/**",
    };


    @Value("${jwt.signerKey}")
    private String SECRET_KEY;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        httpSecurity.authorizeHttpRequests(request ->
                request.requestMatchers(UNSECURED_ENDPOINTS).permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/v1/users").hasAuthority("ROLE_ADMIN")
//                        .requestMatchers(HttpMethod.GET, "/api/v1/users").hasRole(Roles.ADMIN.name())
                        // Quyền cho USER - chỉ có thể xem lịch sử donate của chính họ
                        .requestMatchers(HttpMethod.GET, "/donations/user/**").hasRole("USER")

                        // Quyền cho ADMIN - có thể xem tất cả donate và tổng donate
                        .requestMatchers(HttpMethod.GET, "/donations/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
        );

        //Provide JWT Authentication Provider a decoder to verify JWT signature
        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder())
                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        //When OAuth2 authentication fails, return 401
                        .authenticationEntryPoint(new JWTAuthenticationEntryPoint())
        );

        return httpSecurity.build();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "HS512");
        return NimbusJwtDecoder
                .withSecretKey(keySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
//        converter.setAuthorityPrefix("ROLE_");
        converter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(converter);
        return jwtAuthenticationConverter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
