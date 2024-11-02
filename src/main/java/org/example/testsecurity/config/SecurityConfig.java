package org.example.testsecurity.config;


import lombok.extern.slf4j.Slf4j;
import org.example.testsecurity.filter.CustomGenericFilter;
import org.example.testsecurity.filter.CustomOnceFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(); // 패스워드 단방향 암호화 진행 시 사용
    }

    @Bean
    public SecurityFilterChain filterChain0(HttpSecurity http) throws Exception {
        http.
                authorizeHttpRequests((auth) -> auth.anyRequest().permitAll());

        http
                .addFilterAfter(new CustomGenericFilter(), LogoutFilter.class);

        http
                .addFilterAfter(new CustomOnceFilter(), LogoutFilter.class);

        return http.build();
    }

//    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        log.info("########### Security Filter Chain 등록1!!!!!!!!!!!!");
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/", "/login", "/loginProc","join", "joinProc").permitAll()
                .requestMatchers("/my/**").hasAnyRole("USER")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated() //로그인한 사용자 접근 가능
        );

        http.formLogin((auth) -> auth
                .loginPage("/login")
                .loginProcessingUrl("/loginProc")
                .permitAll()
        );
//        http.httpBasic(Customizer.withDefaults()); //form 로그인이 아니라 헤더에 사용자 정보를 넣는 방식

//        http.csrf((auth) -> auth.disable());

        http.sessionManagement((auth) -> auth
                .maximumSessions(1) //다중 로그인 허용 개수
                .maxSessionsPreventsLogin(true) //다중 로그인 개수 초과 시 처리 방법
        );

        http.sessionManagement((auth)->auth
                .sessionFixation().changeSessionId() //세션 고정 보호
        );

        log.info("########### Security Filter Chain 등록3!!!!!!!!!!!!");
        return http.build();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        return roleHierarchy;
    }
}
