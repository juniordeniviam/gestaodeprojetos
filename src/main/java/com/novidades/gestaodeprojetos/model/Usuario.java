package com.novidades.gestaodeprojetos.model;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter	
@Setter
@SequenceGenerator(name = "generator_usuario", sequenceName = "generator_usuario", initialValue = 1, allocationSize = 1)
public class Usuario implements UserDetails{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@Column(nullable = false)
	private String nome;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	@Column(nullable = false)
	private String senha;

	public Usuario() {
	}

	public Usuario(Long id, String nome, String email, String senha) {
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.senha = senha;
	}

	public Usuario(String nome, String email, String senha) {
		super();
		this.nome = nome;
		this.email = email;
		this.senha = senha;
	}
	
	// Daqui para baixo é implementação de UserDetails
	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	@JsonIgnore
	public String getPassword() {
		return senha;
	}

	@Override
	@JsonIgnore
	public String getUsername() {
		return email;
	}
	
	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return true;
	}
	
	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override
	@JsonIgnore
	public boolean isEnabled() {
		return true;
	}

	
}
