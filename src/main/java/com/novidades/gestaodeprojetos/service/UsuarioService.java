package com.novidades.gestaodeprojetos.service;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.novidades.gestaodeprojetos.model.Usuario;
import com.novidades.gestaodeprojetos.repository.UsuarioRepository;
import com.novidades.gestaodeprojetos.security.JWTService;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JWTService jwtService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	public List<Usuario> obterTodos(){
		return usuarioRepository.findAll();
	}
	
	public Optional<Usuario> obterPorId(Long id){
		return usuarioRepository.findById(id);
	}
	
	public Optional<Usuario> obterPorEmail(String email){
		return usuarioRepository.findByEmail(email);
	}
	
	public Usuario adicionar(Usuario usuario) {
		usuario.setId(null);
		
		if(obterPorEmail(usuario.getEmail()).isPresent()) {
			throw new InputMismatchException("Já existe um usuário cadastrado com este email: " + usuario.getEmail());
		}
		
		// criptografando a senha
		String senha = passwordEncoder.encode(usuario.getSenha());
		
		usuario.setSenha(senha);
		
		return usuarioRepository.save(usuario);
	}
	
}
