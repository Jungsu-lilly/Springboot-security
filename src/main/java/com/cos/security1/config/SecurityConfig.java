package com.cos.security1.config;

import com.cos.security1.config.oauth.PrincipalOauth2UserService;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

// 구글 로그인이 완료된 뒤의 후처리가 필요함. 1.코드받기(인증) 2.엑세스토큰(권한),
// 3.사용자 프로필 정보를 가져옴 4-1.그 정보를 토대로 회원가입을 자동으로 진행시키기도 함
// 4-2 (이메일, 전화번호, 이름, 아이디) 쇼핑몰 -> (집주소), 백화점몰 -> (vip 등급, 일반등급)
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 스프릥 시큐리티 필터가 스프링 필터체인에 등록된다.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
// secured 어노테이션 활성화, preAuthorize, postAuthorize 어노테이션 활성화
public class SecurityConfig{

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    @Autowired
    private UserRepository userRepository;

    // 해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다.
    @Bean
    public BCryptPasswordEncoder encodePwd(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()// 세션을 사용하지 않고 JWT 토큰을 활용하여 진행, csrf토큰검사를 비활성화
                .authorizeRequests() // 인증절차에 대한 설정 진행
                .antMatchers("/user/**").authenticated()
                // 다음 권한이 있는 사람만 들어가게 할 것임
                .antMatchers("/manager/**").access("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")

                .anyRequest().permitAll() // 나머지 url은 인증되지 않더라도 누구든 접근 가능
                .and()

                .formLogin().loginPage("/loginForm")  // 접근이 차단된 페이지 클릭시 이동할 url
                .loginProcessingUrl("/login") // '/login' 주소가 호출되면 시큐리티가 낚아채서 대신 로그인을 진행해줌.
                .defaultSuccessUrl("/")
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(principalOauth2UserService);

        return http.build();
    }
}
