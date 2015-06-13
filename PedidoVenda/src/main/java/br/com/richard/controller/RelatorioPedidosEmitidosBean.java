package br.com.richard.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.hibernate.Session;

import br.com.richard.util.jsf.FacesUtil;
import br.com.richard.util.report.ExecutorRelatorio;

@Named
@RequestScoped
public class RelatorioPedidosEmitidosBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static final String PEDIDOS_EMITIDOS_PDF = "Pedidos emitidos.pdf";
	private static final String DATA_INICIO = "data_inicio";
	private static final String DATA_FIM = "data_fim";
	private static final String CAMINHO_PEDIDOS_EMITIDOS_JASPER = "/relatorios/relatorio_pedidos_emitidos.jasper";

	@NotNull
	private Date dataInicio;
	
	@NotNull
	private Date dataFim;

	@Inject
	private FacesContext facesContext;

	@Inject
	private HttpServletResponse response;

	@Inject
	private EntityManager manager;

	public void emitir() {
		
		Map<String, Object> parametros = new HashMap<>();
		parametros.put(DATA_INICIO, this.dataInicio);
		parametros.put(DATA_FIM, this.dataFim);

		ExecutorRelatorio executor = new ExecutorRelatorio(CAMINHO_PEDIDOS_EMITIDOS_JASPER, this.response, parametros, PEDIDOS_EMITIDOS_PDF);

		Session session = manager.unwrap(Session.class);
		session.doWork(executor);

		if (executor.isRelatorioGerado()) {
			facesContext.responseComplete();
		} else {
			FacesUtil.addErrorMessage("A execução do relatório não retornou dados.");
		}
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

}