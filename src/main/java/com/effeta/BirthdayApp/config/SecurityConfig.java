package com.effeta.BirthdayApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                // Rutas públicas (invitaciones)
                .requestMatchers("/invitacion.html", "/api/invitacion", "/api/confirmar").permitAll()
                // Página de login debe ser pública
                .requestMatchers("/login.html", "/login").permitAll()
                // Archivos estáticos públicos
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                // Rutas de administración protegidas
                .requestMatchers("/admin.html", "/api/invitado", "/api/invitados").authenticated()
                // Cualquier otra ruta requiere autenticación
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login.html")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/admin.html", true)
                .failureUrl("/login.html?error")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login.html?logout")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable()); // Deshabilitar CSRF temporalmente para simplificar

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Usuario: admin
        // Password: Admin2024!
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("Effeta.Admin2771"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
