package br.com.richard.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.richard.model.Usuario;
import br.com.richard.service.CadastroUsuarioService;
import br.com.richard.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroUsuarioBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private CadastroUsuarioService cadastroUsuarioService;

	private Usuario usuario;

	public CadastroUsuarioBean() {
		limpar();
	}

	private void limpar() {
		this.usuario = new Usuario();
	}

	public void salvar() {
		this.usuario = cadastroUsuarioService.salvar(usuario);
		FacesUtil.addInfoMessage("Usuário salvo com sucesso.");
		limpar();
	}
	
	public boolean isEditando() {
		return this.usuario.getId() != null;
	}
	

	public Usuario getUsuario() {
		return usuario;
	}
}