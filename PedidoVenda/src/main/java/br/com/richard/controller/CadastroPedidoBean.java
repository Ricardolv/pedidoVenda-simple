package br.com.richard.controller;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

import br.com.richard.model.Cliente;
import br.com.richard.model.EnderecoEntrega;
import br.com.richard.model.FormaPagamento;
import br.com.richard.model.ItemPedido;
import br.com.richard.model.Pedido;
import br.com.richard.model.Produto;
import br.com.richard.model.Usuario;
import br.com.richard.repository.ClienteRepository;
import br.com.richard.repository.ProdutoRepository;
import br.com.richard.repository.UsuarioRepository;
import br.com.richard.service.CadastroPedidoService;
import br.com.richard.util.cdi.PedidoEdicao;
import br.com.richard.util.cdi.event.PedidoAlteradoEvent;
import br.com.richard.util.cdi.validation.SKU;
import br.com.richard.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroPedidoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Produces
	@PedidoEdicao
	private Pedido pedido;
	private List<Usuario> vendedores;

	@Inject
	private UsuarioRepository usuarioRepository;

	@Inject
	private ClienteRepository clienteRepository;

	@Inject
	private CadastroPedidoService cadastroPedidoService;
	
	@Inject
	private ProdutoRepository produtoRepository;

	private Produto produtoLinhaEditavel;
	
	@SKU
	private String sku;

	public CadastroPedidoBean() {
		limpar();
	}

	public void inicializar() {
		if (pedido == null) {
			limpar();
		}
		
		if (FacesUtil.isNotPostBack()) {
			vendedores = usuarioRepository.vendedores();
			
			this.pedido.adicionarItemVazio();
			
			this.recalcularPedido();
		}

	}

	public void limpar() {
		pedido = new Pedido();
		pedido.setEnderecoEntrega(new EnderecoEntrega());
	}

	public List<Cliente> completarCliente(String nome) {
		return clienteRepository.porNome(nome);
	}

	public FormaPagamento[] getFormasPagamento() {
		return FormaPagamento.values();
	}

	public void salvar() {
		this.pedido.removerItemVazio();
		
		try {
			this.pedido = this.cadastroPedidoService.salvar(this.pedido);
			
			FacesUtil.addInfoMessage("Pedido salvo com sucesso.");
			
		} finally {
			this.pedido.adicionarItemVazio();
		}
		
	}

	public boolean isEditando() {
		if (pedido != null) {
			return	this.pedido.getId() != null;
		}
		return false;
	}
	
	public void recalcularPedido() {
		if (this.pedido != null) {
			this.pedido.recalcularValorTotal();
		}
	}
	
	public List<Produto> completarProduto(String nome) {
		return produtoRepository.porNome(nome);
	}
	
	public void carregarProdutoPorSku() {
		if (StringUtils.isNoneEmpty(this.sku)) {
			this.produtoLinhaEditavel = this.produtoRepository.porSKU(this.sku);
			this.carregarProdutoLinhaEditavel();
		}
	}
	
	public void carregarProdutoLinhaEditavel() {
		ItemPedido item = this.pedido.getItens().get(0);
		
		if (this.produtoLinhaEditavel != null) {
			if (this.existeItemComProduto(this.produtoLinhaEditavel)) {
				FacesUtil.addErrorMessage("Já existe um item no pedido com o produto informado.");
			} else {
				item.setProduto(this.produtoLinhaEditavel);
				item.setValorUnitario(this.produtoLinhaEditavel.getValorUnitario());

				this.pedido.adicionarItemVazio();
				this.produtoLinhaEditavel = null;
				this.sku = null;

				this.pedido.recalcularValorTotal();
			}
		}
		
	}
	
	private boolean existeItemComProduto(Produto produto) {
		boolean existeItem = false;
		
		for (ItemPedido item : this.getPedido().getItens()) {
			if (produto.equals(item.getProduto())) {
				existeItem = true;
				break;
			}
		}
		return existeItem;
	}
	
	public void atualizarQuantidade(ItemPedido item, int linha) {
		if (item.getQuantidade() < 1) {
			if (linha == 0) {
				item.setQuantidade(1);
			} else {
				this.getPedido().getItens().remove(linha);
			}
		}
		this.pedido.recalcularValorTotal();
	}
	
	public void pedidoAlterado(@Observes PedidoAlteradoEvent event) {
		this.pedido = event.getPedido();
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}
	public List<Usuario> getVendedores() {
		return vendedores;
	}

	public Produto getProdutoLinhaEditavel() {
		return produtoLinhaEditavel;
	}

	public void setProdutoLinhaEditavel(Produto produtoLinhaEditavel) {
		this.produtoLinhaEditavel = produtoLinhaEditavel;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}
}