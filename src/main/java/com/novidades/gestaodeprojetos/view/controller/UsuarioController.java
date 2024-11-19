package com.novidades.gestaodeprojetos.view.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.novidades.gestaodeprojetos.model.Usuario;
import com.novidades.gestaodeprojetos.service.UsuarioService;
import com.novidades.gestaodeprojetos.view.model.usuario.LoginRequest;
import com.novidades.gestaodeprojetos.view.model.usuario.LoginResponse;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;
	
	@GetMapping
	public List<Usuario> obterTodos(){
		return usuarioService.obterTodos();
	}
	
	@GetMapping("/{id}")
	public Optional<Usuario> obter(@PathVariable("id") Long id){
		return usuarioService.obterPorId(id);
	}
	
	@PostMapping
	public Usuario adicionar(@RequestBody Usuario usuario) {
		return usuarioService.adicionar(usuario);
	}
	
	public LoginResponse login(@RequestBody LoginRequest request) {
		return usuarioService.logar(request.getEmail(), request.getSenha());
	}
	
}
