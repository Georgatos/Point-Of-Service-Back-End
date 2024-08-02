package dev.andreasgeorgatos.pointofservice.configuration;

import dev.andreasgeorgatos.pointofservice.configuration.filters.TokenExtractionFilter;
import dev.andreasgeorgatos.pointofservice.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JWTUtil jwtUtil;

    @Autowired
    public SecurityConfig(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/api/v1/user-type").denyAll();

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/users/{id}").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/users/register").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/users/login").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/users/forgotPassword").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/users/resetPassword").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/users/verify").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/users/{id}").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/users/{id}").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/item").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/item/{id}").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/item").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/item/{id}").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/item/{id}").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN");

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/review").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/review/{id}").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/review").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/review/{id}").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/review/{id}").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN");

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/notification").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/notification/{id}").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/notification").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/notification/{id}").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/notification/{id}").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/payment-status").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/payment-status/{id}").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/payment-status").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/payment-status/{id}").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/payment-status/{id}").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN");

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/payment-methods").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/payment-methods/{id}").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/payment-methods").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/payment-methods/{id}").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/payment-methods/{id}").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN");

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/payment").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/payment/{id}").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/payment").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/payment/{id}").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/payment/{id}").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN");

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/order-status").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/order-status/{id}").hasAnyAuthority("ROLE_USER", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/order-status").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/order-status/{id}").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/order-status/{id}").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN");

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/order-types").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/order-types/{id}").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/order-types").hasAnyAuthority("ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/order-types/{id}").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/order-types/{id}").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN");

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/order").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/order/{id}").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/order").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/order/{id}").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/order/{id}").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN");

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/delivery-status").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/delivery-status/{id}").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/delivery-status").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/delivery-status/{id}").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/delivery-status/{id}").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN");

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/delivery-history").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/delivery-history/{id}").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/delivery-history").hasAnyAuthority("ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/delivery-history/{id}").hasAnyAuthority("ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/delivery-history/{id}").hasAnyAuthority("ROLE_ADMIN");

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/addresses").hasAnyAuthority("ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/addresses/{id}").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/addresses").hasAnyAuthority("ROLE_USER", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/addresses/{id}").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/addresses/{id}").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN");

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/referral-source").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/referral-source/{id}").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/referral-source").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/referral-source/{id}").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/referral-source/{id}").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN");

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/points-used").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/points-used/{id}").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/points-used").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/points-used/{id}").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/points-used/{id}").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN");

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/points-to-euro-ratio").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/points-to-euro-ratio/{id}").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/points-to-euro-ratio").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/points-to-euro-ratio/{id}").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/points-to-euro-ratio/{id}").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN");

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/points-earned").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/points-earned/{id}").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/points-earned").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/points-earned/{id}").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/points-earned/{id}").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN");

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/membership-card").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/membership-card/{id}").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/membership-card").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/membership-card/{id}").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/membership-card/{id}").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN");

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/item/OrderItem").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/item/OrderItem/{id}").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/item/OrderItem").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "//api/v1/item/OrderItem/{id}").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/item/OrderItem/{id}").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN");

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/order/DineIn").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/order/DineIn/{id}").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/order/DineIn").hasAnyAuthority("ROLE_USER", "ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/order/DineIn/{id}").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/order/DineIn/{id}").hasAnyAuthority("ROLE_MANAGER", "ROLE_ADMIN");
                })
                .addFilterBefore(new TokenExtractionFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public ApplicationListener<AuthenticationSuccessEvent> authenticationSuccessEventApplicationListener(PasswordEncoder passwordEncoder) {
        return (AuthenticationSuccessEvent -> {
            Authentication auth = AuthenticationSuccessEvent.getAuthentication();

            if (auth instanceof UsernamePasswordAuthenticationToken && auth.getCredentials() != null) {
                CharSequence clearTextPass = (CharSequence) auth.getCredentials();
                String newPasswordHash = passwordEncoder.encode(clearTextPass);

                User user = (User) auth.getPrincipal();

                user.setPassword(newPasswordHash);
                ((UsernamePasswordAuthenticationToken) auth).eraseCredentials();
            }
        });
    }

    @Bean
    public PasswordEncoder delegatingPasswordEncoder() {
        Map<String, PasswordEncoder> encoders = new HashMap<>();

        encoders.put("bcrypt", new BCryptPasswordEncoder());
        encoders.put("scrypt", new SCryptPasswordEncoder(3, 8, 1, 32, 16));

        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("bcrypt", encoders);

        passwordEncoder.setDefaultPasswordEncoderForMatches(encoders.get("bcrypt"));

        return passwordEncoder;
    }
}
