package br.com.richard.service;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;
import javax.transaction.Transactional;

import br.com.richard.model.Pedido;
import br.com.richard.model.StatusPedido;
import br.com.richard.repository.PedidoRepository;

public class CadastroPedidoService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private PedidoRepository pedidoRepository;

	@Transactional
	public Pedido salvar(Pedido pedido) {
		
		if (pedido.isNovo()) {
			pedido.setDataCriacao(new Date());
			pedido.setStatus(StatusPedido.ORCAMENTO);
		}
		
		pedido.recalcularValorTotal();
		
		if (pedido.isNaoAlteravel()) {
			throw new NegocioException("Pedido não pode ser alterado no status "+ pedido.getStatus().getDescricao() +".");
		}
		
		if (pedido.getItens().isEmpty()) {
			throw new NegocioException("O pedido deve possuir pelo menos um item.");
		}
		
		if (pedido.isValorTotalNegativo()) {
			throw new NegocioException("O valor total do pedido não pode ser negativo");
		}
		
		pedido = this.pedidoRepository.guardar(pedido);
		return pedido;
	}
	
}