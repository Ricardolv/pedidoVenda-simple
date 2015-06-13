package br.com.richard.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import br.com.richard.model.Cliente;
import br.com.richard.repository.ClienteRepository;

@FacesConverter(forClass = Cliente.class)
public class ClienteConverter implements Converter {
	
	@Inject 
	private ClienteRepository clienteRepository;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		
		Cliente retorno = null;
		
		if (StringUtils.isNotEmpty(value)) {
			Long id = new Long(value);
			retorno = clienteRepository.porId(id);
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
	
		if (value != null) {
			Cliente cliente = (Cliente) value;
			return cliente.getId() == null ? null : cliente.getId().toString();
		}
		
		return "";
	}

}
