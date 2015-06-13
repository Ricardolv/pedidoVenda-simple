package br.com.richard.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import br.com.richard.model.Pedido;
import br.com.richard.repository.PedidoRepository;

@FacesConverter(forClass = Pedido.class)
public class PedidoConverter implements Converter {
	
	@Inject 
	private PedidoRepository pedidoRepository;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		
		Pedido retorno = null;
		
		if (StringUtils.isNotEmpty(value)) {
			Long id = new Long(value);
			retorno = pedidoRepository.porId(id);
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
	
		if (value != null) {
			Pedido pedido = (Pedido) value;
			return pedido.getId() == null ? null : pedido.getId().toString();
		}
		
		return "";
	}

}
