package com.charlesluxinger.money.api.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.charlesluxinger.money.api.config.token.CustomTokenEnhancer;
import com.charlesluxinger.money.api.security.utils.PasswordGenerator;

@Profile("oauth-security")
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
		.withClient("angular")
		.secret(PasswordGenerator.getNewEncodedPassword("@ngul@r"))
		.scopes("read", "write")
		.authorizedGrantTypes("password", "refresh_token")
		.accessTokenValiditySeconds(1800)
		.refreshTokenValiditySeconds(3600)
		.and()
		.withClient("mobile")
		.secret(PasswordGenerator.getNewEncodedPassword("m0b1l3"))
		.scopes("read")
		.authorizedGrantTypes("password", "refresh_token")
		.accessTokenValiditySeconds(1800)
		.refreshTokenValiditySeconds(3600);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));

		endpoints.tokenStore(tokenStore())
		.tokenEnhancer(tokenEnhancerChain)
		.accessTokenConverter(this.accessTokenConverter())
		.reuseRefreshTokens(false)
		.authenticationManager(this.authenticationManager);
	}


	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
		accessTokenConverter.setSigningKey("algaworks");
		return accessTokenConverter;
	}

	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	private TokenEnhancer tokenEnhancer() {
		return new CustomTokenEnhancer();
	}

}
