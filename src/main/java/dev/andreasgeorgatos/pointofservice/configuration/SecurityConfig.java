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

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/users/{id}").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/users/register").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/users/login").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/users/forgotPassword").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/users/resetPassword").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/users/getPermissions").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/users/verify").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/users/{id}").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/users/{id}").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/item").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/item/{id}").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/item").hasAnyRole("MANAGER", "ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/item/{id}").hasAnyRole("MANAGER", "ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/item/{id}").hasAnyRole("MANAGER", "ADMIN");

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/review").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/review/{id}").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/review").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/review/{id}").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/review/{id}").hasAnyRole("USER", "ADMIN");

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/notification").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/notification/{id}").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/notification").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/notification/{id}").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/notification/{id}").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/payment-status").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/payment-status/{id}").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/payment-status").hasAnyRole("MANAGER", "ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/payment-status/{id}").hasAnyRole("MANAGER", "ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/payment-status/{id}").hasAnyRole("MANAGER", "ADMIN");

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/payment-methods").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/payment-methods/{id}").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/payment-methods").hasAnyRole("MANAGER", "ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/payment-methods/{id}").hasAnyRole("MANAGER", "ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/payment-methods/{id}").hasAnyRole("MANAGER", "ADMIN");

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/payment").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/payment/{id}").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/payment").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/payment/{id}").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/payment/{id}").hasAnyRole("MANAGER", "ADMIN");

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/order-status").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/order-status/{id}").hasAnyRole("USER", "MANAGER", "ADMIN");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/order-status").hasAnyRole("MANAGER", "ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/order-status/{id}").hasAnyRole("MANAGER", "ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/order-status/{id}").hasAnyRole("MANAGER", "ADMIN");

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/order-types").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/order-types/{id}").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/order-types").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/order-types/{id}").hasAnyRole("MANAGER", "ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/order-types/{id}").hasAnyRole("MANAGER", "ADMIN");

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/order").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/order/{id}").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/order").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/order/{id}").hasAnyRole("MANAGER", "ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/order/{id}").hasAnyRole("MANAGER", "ADMIN");

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/delivery-status").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/delivery-status/{id}").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/delivery-status").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/delivery-status/{id}").hasAnyRole("MANAGER", "ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/delivery-status/{id}").hasAnyRole("MANAGER", "ADMIN");

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/delivery-history").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/delivery-history/{id}").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/delivery-history").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/delivery-history/{id}").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/delivery-history/{id}").hasRole("ADMIN");

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/addresses").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/addresses/{id}").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/addresses").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/addresses/{id}").hasAnyRole("MANAGER", "ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/addresses/{id}").hasAnyRole("MANAGER", "ADMIN");

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/referral-source").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/referral-source/{id}").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/referral-source").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/referral-source/{id}").hasAnyRole("MANAGER", "ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/referral-source/{id}").hasAnyRole("MANAGER", "ADMIN");

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/points-used").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/points-used/{id}").hasAnyRole("USER", "EMPLOYEE", "MANAGER", "ADMIN");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/points-used").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/points-used/{id}").hasAnyRole("MANAGER", "ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/points-used/{id}").hasAnyRole("MANAGER", "ADMIN");

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/points-to-euro-ratio").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/points-to-euro-ratio/{id}").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/points-to-euro-ratio").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/points-to-euro-ratio/{id}").hasAnyRole("MANAGER", "ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/points-to-euro-ratio/{id}").hasAnyRole("MANAGER", "ADMIN");

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/points-earned").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/points-earned/{id}").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/points-earned").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/points-earned/{id}").hasAnyRole("MANAGER", "ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/points-earned/{id}").hasAnyRole("MANAGER", "ADMIN");

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/membership-card").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/membership-card/{id}").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/membership-card").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/membership-card/{id}").hasAnyRole("MANAGER", "ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/membership-card/{id}").hasAnyRole("MANAGER", "ADMIN");

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/item/OrderItem").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/item/OrderItem/{id}").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/item/OrderItem").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/item/OrderItem/{id}").hasAnyRole("MANAGER", "ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/item/OrderItem/{id}").hasAnyRole("MANAGER", "ADMIN");

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/order/DineIn").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/order/DineIn/{id}").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/order/DineIn").hasAnyRole("CUSTOMER", "COOK", "COOK_HELPER", "SERVER", "MANAGER", "ADMIN", "SYSTEM");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/order/DineIn/{id}").hasAnyRole("MANAGER", "ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/order/DineIn/{id}").hasAnyRole("MANAGER", "ADMIN");
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
