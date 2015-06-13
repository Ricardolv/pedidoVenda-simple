package br.com.richard.controller;

import java.io.Serializable;
import java.util.Locale;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.velocity.tools.generic.NumberTool;

import com.outjected.email.api.MailMessage;
import com.outjected.email.impl.templating.velocity.VelocityTemplate;

import br.com.richard.model.Pedido;
import br.com.richard.util.cdi.PedidoEdicao;
import br.com.richard.util.jsf.FacesUtil;
import br.com.richard.util.mail.Mailer;

@Named
@RequestScoped
public class EnvioPedidoEmailBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Mailer mailer;
	
	@Inject
	@PedidoEdicao
	private Pedido pedido;
	
	public void enviarPedido() {
		MailMessage message = mailer.novaMensagem();
		
		message.to(this.pedido.getCliente().getEmail())
			   .subject("Pedido " + this.pedido.getId())
			   .bodyHtml(new VelocityTemplate(getClass().getResourceAsStream("/emails/pedido.template")))
			   .put("pedido", this.pedido)
			   .put("numberTool", new NumberTool())
			   .put("locale", new Locale("pt", "BR"))
			   .send();
		
		FacesUtil.addInfoMessage("Pedido enviado por email com sucesso!");
	}
}
