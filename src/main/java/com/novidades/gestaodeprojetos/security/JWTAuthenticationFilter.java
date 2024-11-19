package com.novidades.gestaodeprojetos.security;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter{
	
	@Autowired
	private JWTService jwtService;
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	// Método principal onde toda requisição bate antes de chegar no nosso endpoint.
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 	throws ServletException, IOException {
		
		// pego o token de dentro da requisição
		String token = obterToken(request);
		
		// Pego o id do usuário que está dentro do token
		Optional<Long> id = jwtService.obterIdDoUsuario(token);
		
		// Se não achou o id, é porque o usuário não mandou o token correto.
		if(id.isPresent()) {
		
			// Pego o usuario dono do token pelo seu id.
			UserDetails usuario = customUserDetailsService.obterUsuarioPorId(id.get());
			
			// Neste trecho verificamos se o usuário está autenticado ou não.
			// Neste trecho tambem poderia ser validado as permissões -> usuario.getAuthorities()
			UsernamePasswordAuthenticationToken autenticacao = new UsernamePasswordAuthenticationToken(usuario, null, Collections.emptyList());
			
			// Mudando a autenticação para a própria requisição - Adiciona os detalhes da requisição
			autenticacao.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			
			// Repasso a autenticação para o contexto do security.
			// A partir de agora o spring toma conta de tudo para mim :D
			SecurityContextHolder.getContext().setAuthentication(autenticacao);
		}
		
		// Passa a requisição para o próximo filtro
		filterChain.doFilter(request, response);
	}
	
	private String obterToken(HttpServletRequest request) {
		
		String token = request.getHeader("Authorization");
		
	    // Verifica se o token existe e começa com o prefixo "Bearer "
	    if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
	        return token.substring(7); // Remove o prefixo "Bearer "
	    }

	    return null; // Retorna null caso o token não esteja presente ou seja inválido
	}



}
