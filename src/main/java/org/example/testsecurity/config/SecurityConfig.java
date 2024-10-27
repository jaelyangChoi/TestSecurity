package org.example.testsecurity.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(); // 패스워드 단방향 암호화 진행 시 사용
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/", "/login", "/loginProc","join", "joinProc").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER")
                .anyRequest().authenticated() //로그인한 사용자 접근 가능
        );

        http.formLogin((auth) -> auth
                .loginPage("/login")
                .loginProcessingUrl("/loginProc")
                .permitAll()
        );

//        http.csrf((auth) -> auth.disable());

        http.sessionManagement((auth) -> auth
                .maximumSessions(1) //다중 로그인 허용 개수
                .maxSessionsPreventsLogin(true) //다중 로그인 개수 초과 시 처리 방법
        );

        http.sessionManagement((auth)->auth
                .sessionFixation().changeSessionId() //세션 고정 보호
        );

        return http.build();
    }
}
