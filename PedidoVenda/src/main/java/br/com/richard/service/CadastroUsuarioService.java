package br.com.richard.service;

import java.io.Serializable;

import javax.inject.Inject;
import javax.transaction.Transactional;

import br.com.richard.model.Usuario;
import br.com.richard.repository.UsuarioRepository;

public class CadastroUsuarioService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioRepository usuarioRepository;
	
	@Transactional 
	public Usuario salvar(Usuario usuario) {
		return usuarioRepository.guardar(usuario);
	}
	
}