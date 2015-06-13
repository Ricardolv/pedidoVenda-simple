package br.com.richard.service;

import java.io.Serializable;

import javax.inject.Inject;
import javax.transaction.Transactional;

import br.com.richard.model.Pedido;
import br.com.richard.model.StatusPedido;
import br.com.richard.repository.PedidoRepository;

public class EmissaoPedidoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private CadastroPedidoService cadastroPedidoService;
	
	@Inject
	private EstoqueService estoqueService;

	@Inject
	private PedidoRepository pedidoRepository;
	
	@Transactional
	public Pedido emitir(Pedido pedido) {
		
		pedido = cadastroPedidoService.salvar(pedido);
		
		if (pedido.isNaoEmissivel()) {
			throw new NegocioException("Pedido não pode ser emitido om status " + pedido.getStatus().getDescricao() + " .");
		}
		
		this.estoqueService.baixarEstoque(pedido);
		
		pedido.setStatus(StatusPedido.EMITIDO);
		pedido = this.pedidoRepository.guardar(pedido);
		
		return pedido;
	}

}
