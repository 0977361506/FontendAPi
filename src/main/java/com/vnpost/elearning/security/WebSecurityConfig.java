package com.vnpost.elearning.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author:Nguyen Anh Tuan
 *     <p>May 14,2020
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  @Autowired private CustomUserDetailsService userDetailsService;

  @Autowired private JwtAuthenticationEntryPoint unauthorizedHandler;

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Autowired
  public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
  }

  @Bean
  public JwtAuthenticationFilter authenticationTokenFilterBean() throws Exception {
    return new JwtAuthenticationFilter();
  }


  @Override
  protected void configure(HttpSecurity http) throws Exception {

   // http.authorizeRequests().antMatchers("/static/**","/public/**","/web-view/**","/**"
  //  ).permitAll();

    http.authorizeRequests().antMatchers("/api/login/user").permitAll();
    http.authorizeRequests().antMatchers("/api/course-ware-process").permitAll();

    http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/v2/user").permitAll();

    http.cors()
            .and()
            .csrf()
            .disable()
            .authorizeRequests()
            .antMatchers("/api/profile/password")
            .permitAll();
    http.cors()
            .and()
            .csrf()
            .disable()
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, "/api/home/index")
            .permitAll()
            .antMatchers(HttpMethod.GET,"/api/param/**").permitAll();
    http.authorizeRequests().antMatchers("/api/comment**").authenticated();

    http.cors()
            .and()
            .csrf()
            .disable()
            .authorizeRequests()
            .antMatchers("/api/authentication")
            .permitAll()
            .and().authorizeRequests().antMatchers("/api/**")
            .authenticated()
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(unauthorizedHandler)
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

    http.addFilterBefore(
            authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

  }






  @Bean
  public BCryptPasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/api/home/course", "/api/competition/home/hight-light","/api/home/**","/api/news/**","/api/new/**","/api/slideshow/**","/api/news/**");

  }
}
