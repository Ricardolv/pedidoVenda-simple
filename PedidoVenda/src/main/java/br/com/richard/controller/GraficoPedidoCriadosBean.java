package br.com.richard.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

import br.com.richard.model.Usuario;
import br.com.richard.repository.PedidoRepository;
import br.com.richard.security.UsuarioSistema;
import br.com.richard.util.cdi.UsuarioLogado;

@Named
@RequestScoped
public class GraficoPedidoCriadosBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private static DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM");

	@Inject
	private PedidoRepository pedidoRepository;

	@Inject
	@UsuarioLogado
	private UsuarioSistema usuarioLogado; 

	private LineChartModel model;

	public void preRender() {
		this.model = new LineChartModel();
		this.model.setAnimate(true);
		this.model.setTitle("Pedidos criados");
		this.model.setLegendPosition("e");
		this.model.setShowPointLabels(true);

		adicinarSerie("Todos os pedidos ", null);
		adicinarSerie("Meus pedidos ", usuarioLogado.getUsuario());

		this.model.getAxes().put(AxisType.X, new CategoryAxis("Dias"));
		Axis yAxis = this.model.getAxis(AxisType.Y);
		yAxis.setLabel("Valores");
		yAxis.setMin(0);
	}

	private void  adicinarSerie(String rotulo, Usuario criadoPor) {
		Map<Date, BigDecimal> valoresPorData = this.pedidoRepository.valoresTotaisPorData(15, criadoPor);

		ChartSeries series = new ChartSeries();
		series.setLabel(rotulo);
		for (Date data : valoresPorData.keySet()) {
			series.set(DATE_FORMAT.format(data), valoresPorData.get(data));
		}

		this.model.addSeries(series);
	}

	public LineChartModel getModel() {
		return model;
	}

}