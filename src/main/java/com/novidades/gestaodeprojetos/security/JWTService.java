package com.novidades.gestaodeprojetos.security;

import java.util.Date;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.novidades.gestaodeprojetos.model.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTService {
	
	// Chave secreta utilizada pelo JWT para codificar e decodificar o token.
    private static final String CHAVE_PRIVADA_JWT = "secretKey";
    private static final int TEMPO_EXPIRACAO = 86400000; // 1 dia em milissegundos
	
    /**
     * Método para gerar o token
     * @param authentication Autenticação do usuário.
     * @return Token.
     */
	public String gerarToken(Authentication authentication) {
		
		// Aqui pegamos o usuário atual da autenticação.
		Usuario usuario = (Usuario) authentication.getPrincipal();
        
        // Data de expiração = data atual + tempo de expiração
        Date dataExpiracao = new Date(System.currentTimeMillis() + TEMPO_EXPIRACAO);
		
		// Pega todos os dados e retorna o token JWT
		return Jwts.builder()
				.setSubject(usuario.getId().toString())
				.setIssuedAt(new Date())
				.setExpiration(dataExpiracao)
				.signWith(SignatureAlgorithm.HS512, CHAVE_PRIVADA_JWT)
				.compact();

	}
	
	/**
	 * Método para retornar o id do usuário dono do token
	 * @param token Token do Usuário
	 * @return Retorna o id do usuário
	 */
	public Optional<Long> obterIdDoUsuario(String token){
		
		try {
			// Uma Claim é como se fosse um objeto de autenticação do usuário.
			// Retorna as permissões do token
			Claims claims = parse(token).getBody();
			
			// Retorna o id de dentro do token, caso contrário retorna null.
			return Optional.ofNullable(Long.parseLong(claims.getSubject()));
		} catch (Exception e) {
			return Optional.empty();
		}
		
	}
	
	// Método que descobre dentro do token com base na chave privada, quais são as permissões do usuário.
	private Jws<Claims> parse(String token){
		return Jwts.parser().setSigningKey(CHAVE_PRIVADA_JWT).parseClaimsJws(token);
	}
	
	
}
