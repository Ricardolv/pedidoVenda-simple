package br.com.richard.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import br.com.richard.model.Categoria;
import br.com.richard.model.Produto;
import br.com.richard.repository.CategoriaRepository;
import br.com.richard.service.CadastroProdutoService;
import br.com.richard.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroProdutoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private CategoriaRepository categoriaRepository;

	@Inject
	private CadastroProdutoService cadastroProdutoService ;

	private Produto produto;
	private List<Categoria> categoriaRaizes;
	private List<Categoria> subCategorias;

	private Categoria categoriaPai;

	public CadastroProdutoBean() {
		limpar();
	}
	
	public void inicializar() {
		if (this.produto == null) {
			limpar();
		}
		
		if (FacesUtil.isNotPostBack()) {
			categoriaRaizes = categoriaRepository.raizes();

			if (this.categoriaPai != null) {
				carregarSubCategoria();
			}
		} 
	}

	public void carregarSubCategoria() {
		subCategorias = categoriaRepository.subCategoriasDe(categoriaPai);
	}

	public void salvar() {
		this.produto = cadastroProdutoService.salvar(produto);
		FacesUtil.addInfoMessage("Produto salvo com sucesso.");
		limpar();
	}

	private void limpar() {
		produto = new Produto();
		categoriaPai = null;
		subCategorias = new ArrayList<>();
	}
	
	public boolean isEditando() {
		return this.produto.getId() != null;
	}
	
	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
		if (this.produto != null) {
			this.categoriaPai = produto.getCategoria().getCategoriaPai();
		}
	}

	@NotNull
	public List<Categoria> getCategoriaRaizes() {
		return categoriaRaizes;
	}


	public Categoria getCategoriaPai() {
		return categoriaPai;
	}

	public void setCategoriaPai(Categoria categoriaPai) {
		this.categoriaPai = categoriaPai;
	}

	public List<Categoria> getSubCategorias() {
		return subCategorias;
	}
}