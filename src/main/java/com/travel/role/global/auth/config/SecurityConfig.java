package com.travel.role.global.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.travel.role.global.auth.exception.TokenExceptionHandlerFilter;
import com.travel.role.global.auth.oauth.filter.CustomOAuth2LogoutHandler;
import com.travel.role.global.auth.service.CustomAuthProvider;
import com.travel.role.global.auth.service.CustomOAuth2UserService;
import com.travel.role.global.auth.service.CustomUserDetailService;
import com.travel.role.global.auth.service.handler.OAuth2FailureHandler;
import com.travel.role.global.auth.service.handler.OAuth2SuccessHandler
import com.travel.role.global.auth.token.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final CustomUserDetailService customUserDetailsService;

	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	private final TokenExceptionHandlerFilter tokenExceptionHandlerFilter;

	private final OAuth2SuccessHandler oAuth2SuccessHandler;

	private final OAuth2FailureHandler oAuth2FailureHandler;

	private final CustomOAuth2UserService customOAuth2UserService;

	private final CustomOAuth2LogoutHandler logoutHandler;

	@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder
			.authenticationProvider(authenticationProvider())
			.userDetailsService(customUserDetailsService)
			.passwordEncoder(passwordEncoder());
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		return new CustomAuthProvider(customUserDetailsService, passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.cors().configurationSource(corsConfigurationSource())
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.csrf()
			.disable()
			.httpBasic()
			.disable()
			.authorizeRequests()
			.antMatchers("/auth/**", "/h2-console/**", "/oauth2/**").permitAll()
			.anyRequest()
			.authenticated()
			.and()
			.headers().frameOptions().disable() // h2 db 접속때문에 설정한것 //TODO: H2-DB 테스트 이후 삭제할것
			.and()
				.oauth2Login()
				.redirectionEndpoint()
				.baseUri("/oauth2/callback/*")
			.and()
				.userInfoEndpoint().userService(customOAuth2UserService)
			.and()
				.successHandler(oAuth2SuccessHandler)
				.failureHandler(oAuth2FailureHandler)
			.and()
				.exceptionHandling()
					.authenticationEntryPoint(new Http403ForbiddenEntryPoint())
			.and()
				.logout()
					.logoutUrl("/auth/logout")
						.invalidateHttpSession(true)
							.clearAuthentication(true)
								.deleteCookies("JSESSIONID", "refreshToken")
			.addLogoutHandler(logoutHandler)
									;

		http
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(tokenExceptionHandlerFilter, JwtAuthenticationFilter.class);
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedOriginPattern("*");
		configuration.addAllowedHeader("*");
		configuration.addAllowedMethod("*");
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
