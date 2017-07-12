package dao.impl;

import java.util.List;

import model.Posto;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import dao.PostoDao;

public class PostoDaoImpl extends HibernateDaoSupport implements PostoDao {

	public Integer save(Posto Posto) {
		return (Integer) getHibernateTemplate().save(Posto);
	}

	public void delete(Posto Posto) {
		getHibernateTemplate().delete(Posto);
	}

	public void update(Posto Posto) {
		getHibernateTemplate().merge(Posto);
	}

	public Posto getPostoById(int id) {
		@SuppressWarnings("unchecked")
		List<Posto> Postos = (List<Posto>) getHibernateTemplate().find(
				"from Posto as b where b.rid=?", id);
		Posto Posto = Postos.size() > 0 ? Postos.get(0) : null;
		return Posto;
	}

	public List<Posto> getAllPostos() {
		@SuppressWarnings("unchecked")
		List<Posto> Postos = (List<Posto>) getHibernateTemplate()
				.find("from Posto");
		return Postos;
	}

}
