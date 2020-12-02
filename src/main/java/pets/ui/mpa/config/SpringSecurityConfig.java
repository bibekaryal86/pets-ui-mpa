package pets.ui.mpa.config;

import static pets.ui.mpa.util.ConstantUtils.HOME_ACTION_INVALID;
import static pets.ui.mpa.util.ConstantUtils.HOME_ACTION_PARAM;
import static org.springframework.http.HttpMethod.POST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import pets.ui.mpa.service.AppUserService;;

@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AppUserService userService;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationTrustResolver getAuthenticationTrustResolver() {
		return new AuthenticationTrustResolverImpl();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.anyRequest()
			.authenticated()
			.and()
				.formLogin()
					.loginPage("/home.pets")
					.failureUrl("/home.pets?" + HOME_ACTION_PARAM + "=" + HOME_ACTION_INVALID)
			.defaultSuccessUrl("/main.pets", true)
				.permitAll()
			.and()
			.logout()
				.logoutSuccessUrl("/logoff.pets")
				.permitAll()
			.and()
			.csrf()
				.disable();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/static/**")
			.and()
			.ignoring().mvcMatchers(POST, "/servlet/InitPetsHome");
	}
}
