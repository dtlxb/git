package dao.impl;

import java.util.ArrayList;
import java.util.List;

import model.Posto;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import dao.PostoDao;

public class PostoDaoImpl extends HibernateDaoSupport implements PostoDao {

	public Integer save(Posto Posto) {
		  getHibernateTemplate().save(Posto);
		String hql = "select max(b.pid) from Posto as b  ";
		List<Integer> lst = new ArrayList<Integer>();
		lst = getHibernateTemplate().find(hql);
		return lst.get(0);
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
				"from Posto as b where b.pid=?", id);
		Posto Posto = Postos.size() > 0 ? Postos.get(0) : null;
		return Posto;
	}
	public  List<Posto>  getPostoByUsername(String username) {
		@SuppressWarnings("unchecked")
		List<Posto> Postos = (List<Posto>) getHibernateTemplate().find(
				"from Posto as b where b.username=?", username);
		 
		return Postos;
	}
	public List<Posto> getPostoByLocation(Posto Posto) {
		double lat = Posto.getLatitude();
		double lon = Posto.getLongitude();
		System.out.print(lat);
		@SuppressWarnings("unchecked")
		List<Posto> Postos = (List<Posto>) getHibernateTemplate().find(
				"from Posto as b where -0.002<?-b.latitude and ?-b.latitude<0.002 and -0.002<?-b.longitude and ?-b.longitude <0.002",lat,lat,lon,lon);

		return Postos;
		
		
	}
	public List<Posto> getPostoByName(Posto Posto) {
		String name = Posto.getName();
		System.out.print(name);
		@SuppressWarnings("unchecked")
		List<Posto> Postos = (List<Posto>) getHibernateTemplate().find(
				"from Posto as b where b.name like ? ","%"+name+"%");

		return Postos;
	}
	
	public List<Posto> getPostoByBelong_id(int belong_id) {
		@SuppressWarnings("unchecked")
		List<Posto> Postos = (List<Posto>) getHibernateTemplate().find(
				"from Posto as b where b.belong_rid=?", belong_id);
		return Postos;
	}
	public List<Posto> getAllPostos() {
		@SuppressWarnings("unchecked")
		List<Posto> Postos = (List<Posto>) getHibernateTemplate()
				.find("from Posto");
		return Postos;
	}

}
