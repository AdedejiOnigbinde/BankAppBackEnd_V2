package com.base.BaseDependencies.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

        private AuthenticationProvider authenticationProvider;
        private JwtAuthenticationFilter jwtAuthenticationFilter;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

                http.csrf().disable().cors().and()
                                .authorizeHttpRequests()
                                .antMatchers(HttpMethod.POST, Endpoints.AUTH).permitAll()
                                .antMatchers(HttpMethod.GET, Endpoints.ADMINACCOUNT).hasAuthority("ADMIN")
                                .antMatchers(HttpMethod.GET, Endpoints.ADMINCLIENT).hasAuthority("ADMIN")
                                .antMatchers(HttpMethod.POST, Endpoints.CREATEACCOUNTALL).hasAuthority("USER")
                                .antMatchers(HttpMethod.POST, Endpoints.CREATETRANSACTIONALL).hasAuthority("USER")
                                .antMatchers(HttpMethod.DELETE, Endpoints.DELETEACCOUNTALL).hasAuthority("USER")
                                .antMatchers(HttpMethod.DELETE, Endpoints.DELETECLIENTALL).hasAuthority("USER")
                                .antMatchers(HttpMethod.GET, Endpoints.RETRIEVEACCOUNTALL)
                                .hasAnyAuthority("ADMIN", "USER")
                                .antMatchers(HttpMethod.GET, Endpoints.RETRIEVETRANSACTIONALL)
                                .hasAnyAuthority("ADMIN", "USER")
                                .antMatchers(HttpMethod.GET, Endpoints.CLIENTBENEFICIARIES).hasAnyAuthority("USER")
                                .antMatchers(HttpMethod.PATCH, Endpoints.CLIENTPROFILE).hasAnyAuthority("USER")
                                .anyRequest().authenticated()
                                .and()
                                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                                .and()
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
                        throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
        }

        public class Endpoints {
                public static final String AUTH = "/auth/**";
                public static final String ADMINACCOUNT = "/account/allaccounts";
                public static final String ADMINCLIENT = "/client/allclients";
                public static final String CREATEACCOUNTALL = "/account/create";
                public static final String[] CREATETRANSACTIONALL = {
                                "/transaction/outer-bank-transfer", "/transaction/inner-bank-transfer",
                                "/transaction/paybill" };
                public static final String DELETEACCOUNTALL = "/account/deleteaccount/**";
                public static final String DELETECLIENTALL[] = { "/client/removeclient", "/client/removebeneficiary/**",
                                "/client/removebill/**" };
                public static final String[] RETRIEVEACCOUNTALL = { "/account/alluseraccounts",
                                "/account/useraccount/{accountId}" };
                public static final String RETRIEVETRANSACTIONALL[] = { "/transaction/accounttransactions/**",
                                "/transaction/accountrecenttransactions", "/transaction/allpaidbills" };
                public static final String CLIENTBENEFICIARIES[] = { "/client/allbeneficiaries", "client/allbills",
                                "/client/profile" };
                public static final String CLIENTPROFILE[] = { "/client/password", "/client/profile" };

        }

}
