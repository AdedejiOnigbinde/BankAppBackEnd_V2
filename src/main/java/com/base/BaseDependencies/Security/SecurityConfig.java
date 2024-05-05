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
                                .antMatchers(HttpMethod.GET, Endpoints.GETREQUESTADMIN).hasAuthority("ADMIN")
                                .antMatchers(HttpMethod.PATCH, Endpoints.PATCHREQUESTADMIN).hasAuthority("ADMIN")
                                .antMatchers(HttpMethod.POST, Endpoints.POSTREQUESTCLIENT).hasAuthority("USER")
                                .antMatchers(HttpMethod.DELETE, Endpoints.DELETEREQUESTCLIENT).hasAuthority("USER")
                                .antMatchers(HttpMethod.GET, Endpoints.GETREQUESTBOTH)
                                .hasAnyAuthority("ADMIN", "USER")
                                .antMatchers(HttpMethod.GET, Endpoints.GETREQUESTCLIENT).hasAnyAuthority("USER")
                                .antMatchers(HttpMethod.PATCH, Endpoints.PATCHREQUESTCLIENT).hasAnyAuthority("USER")
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

        protected class Endpoints {
                protected static final String AUTH = "/auth/**";
                protected static final String GETREQUESTADMIN[] = { "/account/all", "/client/all", "/client/loan/admin" };
                protected static final String PATCHREQUESTADMIN[] = { "/client/loan/status" };
                protected static final String[] POSTREQUESTCLIENT = {
                                "/transaction/outer-bank", "/transaction/inner-bank",
                                "/transaction/bill", "/account/create", "/client/loan", "/transaction/loan" };
                protected static final String DELETEREQUESTCLIENT[] = { "/client/remove", "/client/beneficiary/**",
                                "/client/bill/**", "/account/{accountId}" };
                protected static final String GETREQUESTCLIENT[] = { "/client/beneficiary", "client/bill",
                                "/client/profile", "/client/loan", "/client/loan/{loanId}" };
                protected static final String PATCHREQUESTCLIENT[] = { "/client/password", "/client/profile" };
                protected static final String[] GETREQUESTBOTH = { "/account/client",
                                "/account/{accountId}", "/transaction/accounttransactions/**",
                                "/transaction/recent", "/transaction/bills" };

        }

}
