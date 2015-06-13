package br.com.richard.util.cdi.event;

import br.com.richard.model.Pedido;

public class PedidoAlteradoEvent {
	
	private Pedido pedido;
	
	public PedidoAlteradoEvent(Pedido pedido) {
		this.pedido = pedido;
	}

	public Pedido getPedido() {
		return pedido;
	}
}