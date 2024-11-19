package com.novidades.gestaodeprojetos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // Aqui informo que é uma classe de seguranaça do WebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	private JWTAuthenticationFilter jwtAuthenticationFilter;

    /*
     Método que devolve uma instância do objeto que sabe devolver o nosso padrão de codificação.
     Isso não tem nada haver com JWT.
     Aqui será usado para codificar a senha do usuário gerando um hash.     
    */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Usando BCrypt para criptografia de senhas
    }
    
    // Método padrão para configurar nosso custom com o nosso método de codificar senha.
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
    	authenticationManagerBuilder
    		.userDetailsService(customUserDetailsService)
    		.passwordEncoder(passwordEncoder());
    }
    
    // Método para trabalhar com autenticação no login.
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }    
    
    // Método que tem a configuração global de acessos e permissões por rotas.
    @Override
    public void configure(HttpSecurity http) throws Exception {
    	// Parte padrão da configuração, por enquanto ignorar
    	http
    		.cors().and().csrf().disable()
    		.exceptionHandling()
    		.and()
    		.sessionManagement()
    		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    		.and()
    		.authorizeRequests()
    		//	Daqui para baixo é onde vamos fazer as validações
    		//	Onde diremos quais rotas não vão precisar de autenticação
    		//	acesso permitido a todos sem autenticação
    		.antMatchers(HttpMethod.POST, "/api/usuarios", "/api/usuarios/login")
    		.permitAll()
    		// Demais requisições devem ser autenticadas.
    		.anyRequest()
    		.authenticated();
    	
    	// Aqui informo que antes de qualquer requisição http, o sistema deve usar o nosso filtro jwtAuthenticationFilter.
    	http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
