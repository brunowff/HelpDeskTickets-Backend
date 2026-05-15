package br.com.doubletelecom.help_desk_tickets.app.configurations;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;

/*
 * Configuração de segurança JWT com RS256.
 *
 * Decisões de design:
 * - OAuth2 Resource Server: o Spring Security valida automaticamente o JWT em cada requisição,
 *   extrai os claims e popula o SecurityContext — sem necessidade de filtro customizado.
 * - RSA (assimétrico): a chave pública pode ser distribuída para outros serviços validarem
 *   tokens sem expor a chave privada.
 * - STATELESS: nenhuma sessão HTTP é criada; toda autenticação vem do JWT no header.
 * - CSRF desabilitado: desnecessário em APIs stateless com JWT (sem cookies de sessão).
 * - CORS aberto (*): adequado para desenvolvimento; restringir origens em produção.
 */

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

        @Value("${jwt.public.key}")
        private RSAPublicKey publicKey;
        
        @Value("${jwt.private.key}")
        private RSAPrivateKey privateKey;

        @Value("${jwt.token.ttl.access}")
        private String accessTokenTTL;
        
        @Value("${jwt.token.ttl.refresh}")
        private String refreshTokenTTL;

    /*
     * Filtro principal de segurança.
     *
     * Rotas públicas:
     * - Swagger UI e OpenAPI docs (para facilitar integração e testes)
     * - POST /auth/login e /auth/refresh (fluxo de autenticação)
     * - /error (tratamento de erros do Spring)
     *
     * Todas as demais rotas exigem JWT válido no header Authorization.
     * A autorização granular por role é feita via @PreAuthorize nos controllers.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

            http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                            "/v3/api-docs/**",
                            "/v3/api-docs.yaml",
                            "/swagger-ui.html",
                            "/swagger-ui/**",
                            "/webjars/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/refresh").permitAll()
                        .requestMatchers(HttpMethod.POST, "/error").permitAll()
                        .requestMatchers(HttpMethod.GET, "/error").permitAll()
                        .anyRequest().authenticated())
                .csrf(csrf -> csrf.disable())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

            return http.build();
    }

    /** Decodifica e valida JWTs recebidos nas requisições usando a chave pública RSA. */
    @Bean
    public JwtDecoder jwtDecoder(){
            return NimbusJwtDecoder.withPublicKey(publicKey).build();
        }

    /**
     * Codifica (assina) JWTs gerados pela aplicação usando o par de chaves RSA.
     * O JWKSet expõe a chave pública para que outros serviços possam validar os tokens.
     */
    @Bean
    public JwtEncoder jwtEncoder(){
        JWK jwk = new RSAKey.Builder(this.publicKey).privateKey(privateKey).build();
        var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));

        return new NimbusJwtEncoder(jwks);
    }

    /** TTL do access token em segundos, injetado de {@code jwt.token.ttl.access}. */
    @Bean
    public Long accessTokenTTL(){
        return Long.parseLong(accessTokenTTL);
    }

    /** TTL do refresh token em segundos, injetado de {@code jwt.token.ttl.refresh}. */
    @Bean
    public Long refreshTokenTTL(){
        return Long.parseLong(refreshTokenTTL);
    }

    /** Encoder BCrypt para senhas. Fator de custo padrão (10 rounds). */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
	public CorsConfigurationSource corsConfigurationSource() {
		final CorsConfiguration configuration = new CorsConfiguration();

		configuration.setAllowedOrigins(Collections.singletonList("*"));
		configuration.setAllowedMethods(Collections.singletonList("*"));
		configuration.setAllowedHeaders(Collections.singletonList("*"));

		configuration.addAllowedOrigin("*");
		configuration.addAllowedHeader("*");
		configuration.addAllowedMethod("*");
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return source;
	}
}
