package br.com.richard.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.com.richard.model.Grupo;
import br.com.richard.model.Usuario;
import br.com.richard.repository.UsuarioRepository;
import br.com.richard.util.cdi.CDIServiceLocator;

public class AppUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		UsuarioRepository usuarioRepository = CDIServiceLocator.getBean(UsuarioRepository.class);
		Usuario usuario = usuarioRepository.porEmail(email);
		
		UsuarioSistema user = null;
		
		if (usuario != null) {
			user = new UsuarioSistema(usuario, getGrupos(usuario));
		}

		return user;
	}
	
	public Collection<? extends GrantedAuthority> getGrupos(Usuario usuario) {
		
		List<SimpleGrantedAuthority> authority = new ArrayList<>();
		
		for (Grupo grupo : usuario.getGrupos()) {
			authority.add(new SimpleGrantedAuthority(grupo.getNome().toUpperCase()));
		}
		
		return authority;
	}
}