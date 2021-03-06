package br.com.richard.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.richard.model.Pedido;
import br.com.richard.model.StatusPedido;
import br.com.richard.repository.PedidoRepository;
import br.com.richard.repository.filter.PedidoFilter;

@Named
@ViewScoped
public class PesquisaPedidosBean implements Serializable {
 
	private static final long serialVersionUID = 1L;
	
	@Inject
	private PedidoRepository pedidoRepository;

	private PedidoFilter filtro;
	private List<Pedido> pedidosFiltrados;
	
	public PesquisaPedidosBean() {
		filtro = new PedidoFilter();
		pedidosFiltrados = new ArrayList<>();
	}
	
	public void pesquisar() {
		pedidosFiltrados = pedidoRepository.filtrados(filtro);
	}
	
	public StatusPedido[] getStatuses(){
		return StatusPedido.values();
	}

	public List<Pedido> getPedidosFiltrados() {
		return pedidosFiltrados;
	}

	public PedidoFilter getFiltro() {
		return filtro;
	}
}