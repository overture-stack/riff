/*
 * Copyright (c) 2018. The Ontario Institute for Cancer Research. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package bio.overture.riff.config;

import bio.overture.riff.jwt.JWTAuthorizationFilter;
import bio.overture.riff.jwt.JWTTokenConverter;
import lombok.SneakyThrows;
import lombok.val;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;

@Slf4j
@EnableWebSecurity
@EnableResourceServer
public class WebSecurityConfig extends ResourceServerConfigurerAdapter {

  @Autowired
  private ResourceLoader resourceLoader;

  @Value("${auth.jwt.publicKeyUrl}")
  private String publicKeyUrl;

  @Override
  @SneakyThrows
  public void configure(HttpSecurity http) {
    http
      .authorizeRequests()
        .antMatchers(HttpMethod.OPTIONS, "/riff/*").permitAll()
        .antMatchers(HttpMethod.GET, "/riff/*").permitAll()
        .antMatchers("/health").permitAll()
        .antMatchers("/isAlive").permitAll()
        .antMatchers("/upload/**").permitAll()
        .antMatchers("/download/**").permitAll()
        .antMatchers("/entities/**").permitAll()
        .antMatchers("/swagger**", "/swagger-resources/**", "/v2/api**", "/webjars/**").permitAll()
    .and()
      .authorizeRequests()
        .anyRequest().authenticated()
    .and()
      .addFilterAfter(new JWTAuthorizationFilter(), BasicAuthenticationFilter.class);
  }

  @Bean
  public FilterRegistrationBean simpleCorsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.setAllowedOrigins(Collections.singletonList("*"));
    config.setAllowedMethods(Collections.singletonList("*"));
    config.setAllowedHeaders(Collections.singletonList("*"));
    source.registerCorsConfiguration("/**", config);
    FilterRegistrationBean bean = new FilterRegistrationBean(new org.springframework.web.filter.CorsFilter(source));
    bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
    return bean;
  }

/*  @Bean
  @Primary
  public CorsFilter corsFilter() {
    return new CorsFilter();
  }*/

  @Override
  public void configure(ResourceServerSecurityConfigurer config) {
    config.tokenServices(tokenServices());
  }

  @Bean
  public TokenStore tokenStore() {
    return new JwtTokenStore(accessTokenConverter());
  }

  @Bean
  @SneakyThrows
  public JwtAccessTokenConverter accessTokenConverter() {
    return new JWTTokenConverter(fetchJWTPublicKey());
  }


  @Bean
  public DefaultTokenServices tokenServices() {
    val defaultTokenServices = new DefaultTokenServices();
    defaultTokenServices.setTokenStore(tokenStore());
    return defaultTokenServices;
  }

  /**
   * Call EGO server for public key to use when verifying JWTs
   * Pass this value to the JWTTokenConverter
   */
  @SneakyThrows
  private String fetchJWTPublicKey() {
    val publicKeyResource = resourceLoader.getResource(publicKeyUrl);

    val stringBuilder = new StringBuilder();
    val reader = new BufferedReader(
      new InputStreamReader(publicKeyResource.getInputStream()));

    reader.lines().forEach(stringBuilder::append);
    return stringBuilder.toString();
  }

}
