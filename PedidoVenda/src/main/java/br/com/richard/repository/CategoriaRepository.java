package br.com.richard.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.richard.model.Categoria;

public class CategoriaRepository implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public Categoria porId(long id) {
		return manager.find(Categoria.class, id);
	}

	public List<Categoria> raizes() {
		return manager.createQuery("from Categoria where categoriaPai is null" , Categoria.class).getResultList();
	}
	
	public List<Categoria> subCategoriasDe(Categoria categoria) {
		return manager.createQuery("from Categoria where categoriaPai = :raiz", Categoria.class)
					  .setParameter("raiz", categoria).getResultList();
	}
}