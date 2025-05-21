package dev.andreasgeorgatos.pointofservice.configuration;

import dev.andreasgeorgatos.pointofservice.configuration.filters.TokenExtractionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer; // Added for HSTS
import org.springframework.security.config.http.SessionCreationPolicy;
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

    // Role Constants
    private static final String ROLE_CUSTOMER = "CUSTOMER";
    private static final String ROLE_COOK = "COOK";
    private static final String ROLE_COOK_HELPER = "COOK_HELPER";
    private static final String ROLE_SERVER = "SERVER";
    private static final String ROLE_MANAGER = "MANAGER";
    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_SYSTEM = "SYSTEM";
    private static final String ROLE_USER = "USER"; // General user role, seems less specific than CUSTOMER

    // Common Role Combinations
    private static final String[] ROLES_ALL_STAFF_AND_CUSTOMER = {
            ROLE_CUSTOMER, ROLE_COOK, ROLE_COOK_HELPER, ROLE_SERVER, ROLE_MANAGER, ROLE_ADMIN, ROLE_SYSTEM
    };
    private static final String[] ROLES_MANAGEMENT_STAFF = {ROLE_MANAGER, ROLE_ADMIN};
    private static final String[] ROLES_USER_ADMIN = {ROLE_USER, ROLE_ADMIN};
    private static final String[] ROLES_USER_MANAGER_ADMIN = {ROLE_USER, ROLE_MANAGER, ROLE_ADMIN};


    @Autowired
    public SecurityConfig(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Add HSTS header configuration
                .headers(headers -> headers
                        .httpStrictTransportSecurity(hsts -> hsts
                                .includeSubDomains(true)
                                .maxAgeInSeconds(31536000) // 1 year
                        )
                )
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/api/v1/user-type").denyAll();

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/users/{id}").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/users/register").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/users/login").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/users/forgotPassword").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/users/resetPassword").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/users/getPermissions").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/users/verify").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/users/{id}").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/users/{id}").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/employees-controller").hasAnyRole(ROLES_MANAGEMENT_STAFF);
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/employees-controller/{id}").hasAnyRole(ROLES_MANAGEMENT_STAFF);

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/item").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/item/{id}").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/item").hasAnyRole(ROLES_MANAGEMENT_STAFF);
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/item/{id}").hasAnyRole(ROLES_MANAGEMENT_STAFF);
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/item/{id}").hasAnyRole(ROLES_MANAGEMENT_STAFF);

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/review").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/review/{id}").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/review").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/review/{id}").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/review/{id}").hasAnyRole(ROLES_USER_ADMIN);

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/notification").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/notification/{id}").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/notification").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/notification/{id}").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/notification/{id}").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/payment-status").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/payment-status/{id}").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/payment-status").hasAnyRole(ROLES_MANAGEMENT_STAFF);
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/payment-status/{id}").hasAnyRole(ROLES_MANAGEMENT_STAFF);
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/payment-status/{id}").hasAnyRole(ROLES_MANAGEMENT_STAFF);

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/payment-methods").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/payment-methods/{id}").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/payment-methods").hasAnyRole(ROLES_MANAGEMENT_STAFF);
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/payment-methods/{id}").hasAnyRole(ROLES_MANAGEMENT_STAFF);
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/payment-methods/{id}").hasAnyRole(ROLES_MANAGEMENT_STAFF);

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/payment").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/payment/{id}").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/payment").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/payment/{id}").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/payment/{id}").hasAnyRole(ROLES_MANAGEMENT_STAFF);

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/order-status").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/order-status/{id}").hasAnyRole(ROLES_USER_MANAGER_ADMIN);
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/order-status").hasAnyRole(ROLES_MANAGEMENT_STAFF);
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/order-status/{id}").hasAnyRole(ROLES_MANAGEMENT_STAFF);
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/order-status/{id}").hasAnyRole(ROLES_MANAGEMENT_STAFF);

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/order-types").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/order-types/{id}").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/order-types").hasRole(ROLE_ADMIN);
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/order-types/{id}").hasAnyRole(ROLES_MANAGEMENT_STAFF);
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/order-types/{id}").hasAnyRole(ROLES_MANAGEMENT_STAFF);

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/order").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/order/{id}").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/order").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/order/{id}").hasAnyRole(ROLES_MANAGEMENT_STAFF);
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/order/{id}").hasAnyRole(ROLES_MANAGEMENT_STAFF);

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/delivery-status").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/delivery-status/{id}").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/delivery-status").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/delivery-status/{id}").hasAnyRole(ROLES_MANAGEMENT_STAFF);
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/delivery-status/{id}").hasAnyRole(ROLES_MANAGEMENT_STAFF);

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/delivery-history").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/delivery-history/{id}").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/delivery-history").hasRole(ROLE_ADMIN);
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/delivery-history/{id}").hasRole(ROLE_ADMIN);
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/delivery-history/{id}").hasRole(ROLE_ADMIN);

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/addresses").hasRole(ROLE_ADMIN);
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/addresses/{id}").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/addresses").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/addresses/{id}").hasAnyRole(ROLES_MANAGEMENT_STAFF);
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/addresses/{id}").hasAnyRole(ROLES_MANAGEMENT_STAFF);

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/referral-source").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/referral-source/{id}").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/referral-source").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/referral-source/{id}").hasAnyRole(ROLES_MANAGEMENT_STAFF);
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/referral-source/{id}").hasAnyRole(ROLES_MANAGEMENT_STAFF);

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/points-used").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/points-used/{id}").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/points-used").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/points-used/{id}").hasAnyRole(ROLES_MANAGEMENT_STAFF);
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/points-used/{id}").hasAnyRole(ROLES_MANAGEMENT_STAFF);

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/points-to-euro-ratio").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/points-to-euro-ratio/{id}").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/points-to-euro-ratio").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/points-to-euro-ratio/{id}").hasAnyRole(ROLES_MANAGEMENT_STAFF);
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/points-to-euro-ratio/{id}").hasAnyRole(ROLES_MANAGEMENT_STAFF);

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/points-earned").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/points-earned/{id}").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/points-earned").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/points-earned/{id}").hasAnyRole(ROLES_MANAGEMENT_STAFF);
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/points-earned/{id}").hasAnyRole(ROLES_MANAGEMENT_STAFF);

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/membership-card").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/membership-card/{id}").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/membership-card").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/membership-card/{id}").hasAnyRole(ROLES_MANAGEMENT_STAFF);
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/membership-card/{id}").hasAnyRole(ROLES_MANAGEMENT_STAFF);

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/item/OrderItem").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/item/OrderItem/{id}").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/item/OrderItem").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/item/OrderItem/{id}").hasAnyRole(ROLES_MANAGEMENT_STAFF);
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/item/OrderItem/{id}").hasAnyRole(ROLES_MANAGEMENT_STAFF);

                    auth.requestMatchers(HttpMethod.GET, "/api/v1/order/DineIn").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/order/DineIn/getDineInTableByNumber").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/order/DineIn/{id}").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/order/DineIn").hasAnyRole(ROLES_ALL_STAFF_AND_CUSTOMER);
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/order/DineIn/deleteTableByNumber").hasAnyRole(ROLES_MANAGEMENT_STAFF);
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/order/DineIn/{id}").hasAnyRole(ROLES_MANAGEMENT_STAFF);
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/order/DineIn/{id}").hasAnyRole(ROLES_MANAGEMENT_STAFF);
                })
                .addFilterBefore(new TokenExtractionFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults()); // Chain build after this
        return http.build();
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
