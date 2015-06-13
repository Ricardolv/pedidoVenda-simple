package br.com.richard.controller;
import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.richard.model.Pedido;
import br.com.richard.service.CancelamentoService;
import br.com.richard.util.cdi.PedidoEdicao;
import br.com.richard.util.cdi.event.PedidoAlteradoEvent;
import br.com.richard.util.jsf.FacesUtil;

@Named
@RequestScoped
public class CancelamentoPedidoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private CancelamentoService cancelamentoService;
	
	@Inject
	private Event<PedidoAlteradoEvent> pedidoAlteradoEvent;
	
	@Inject
	@PedidoEdicao
	private Pedido pedido;
	
	public void cancelarPedido() {
		this.pedido = this.cancelamentoService.cancelar(this.pedido);
		this.pedidoAlteradoEvent.fire(new PedidoAlteradoEvent(this.pedido)); 
		
		FacesUtil.addInfoMessage("Pedido cancelado com sucesso!");
	}
}
