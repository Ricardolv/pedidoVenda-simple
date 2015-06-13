package br.com.richard.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.richard.model.Produto;
import br.com.richard.repository.filter.ProdutoFilter;
import br.com.richard.service.NegocioException;

public class ProdutoRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Produto guardar(Produto produto) {
		return manager.merge(produto);
	}

	public Produto porSKU(String sku) {
		try {
			return manager.createQuery("from Produto where upper(sku) = :sku", Produto.class)
						  .setParameter("sku", sku.toUpperCase()) 
						  .getSingleResult();

		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Produto> filtrados(ProdutoFilter filtro) {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Produto.class);

		if (StringUtils.isNotBlank(filtro.getSku())) {
			criteria.add(Restrictions.eq("sku", filtro.getSku()));
		}

		if (StringUtils.isNotBlank(filtro.getNome())) {
			criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
		}

		return criteria.addOrder(Order.asc("nome")).list(); 
	}

	public Produto porId(Long id) {
		return manager.find(Produto.class, id);
	}
	
	@Transactional
	public void remover(Produto produto) {
		try {
			produto = porId(produto.getId());
			manager.remove(produto);
			manager.flush();
			
		} catch (PersistenceException e) {
			throw new NegocioException("Produto não pode ser excluído.");
		}
	}

	@SuppressWarnings("unchecked")
	public List<Produto> porNome(String nome) {
		//query criteria
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Produto.class);
		criteria.add(Restrictions.ilike("nome", nome, MatchMode.ANYWHERE));
		return criteria.addOrder(Order.asc("nome")).list(); 
		
		//query JPQL
		/*return manager.createQuery("from Produto where upper(nome) like :nome ", Produto.class)
				.setParameter("nome", nome.toLowerCase() + "%").getResultList();*/
	}
}