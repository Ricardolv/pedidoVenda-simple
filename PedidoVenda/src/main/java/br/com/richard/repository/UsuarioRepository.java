package br.com.richard.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import br.com.richard.model.Usuario;

public class UsuarioRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Usuario guardar(Usuario usuario) {
		return manager.merge(usuario);
	}

	public Usuario porId(long id) {
		return manager.find(Usuario.class, id);
	}

	public List<Usuario> vendedores() {
		return this.manager.createQuery("from Usuario order by nome", Usuario.class).getResultList();
	}

	public Usuario porEmail(String email) {
		Usuario usuario = null;

		try {
			usuario = this.manager.createQuery("from Usuario where lower(email) = :email", Usuario.class)
					.setParameter("email", email.toLowerCase()).getSingleResult();
			
		} catch (NoResultException e) {
			// nenhum usuario encontrado com o email informado
		}

		return usuario;
	}

}