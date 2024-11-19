package com.novidades.gestaodeprojetos.view.model.usuario;

import com.novidades.gestaodeprojetos.model.Usuario;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
	
	private String token;
	private Usuario usuario;
	
	public LoginResponse() {
	}

	public LoginResponse(String token, Usuario usuario) {
		super();
		this.token = token;
		this.usuario = usuario;
	}
	
	

}
