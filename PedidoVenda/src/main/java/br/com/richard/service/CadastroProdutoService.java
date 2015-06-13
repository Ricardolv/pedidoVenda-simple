package br.com.richard.service;

import java.io.Serializable;

import javax.inject.Inject;
import javax.transaction.Transactional;

import br.com.richard.model.Produto;
import br.com.richard.repository.ProdutoRepository;

public class CadastroProdutoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ProdutoRepository protudoRepository;
	
	@Transactional 
	public Produto salvar(Produto produto) {
		Produto produtosexistente = protudoRepository.porSKU(produto.getSku());
		
		if (produtosexistente != null && !produtosexistente.equals(produto)) {
			throw new NegocioException("Já existe um produto com o SKU informado.");
		}
		return protudoRepository.guardar(produto);
	}
	
}