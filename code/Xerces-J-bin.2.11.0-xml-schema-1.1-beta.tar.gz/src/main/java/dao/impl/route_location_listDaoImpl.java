package dao.impl;

import java.util.ArrayList;
import java.util.List;

import model.route_location_list;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import dao.route_location_listDao;

public class route_location_listDaoImpl extends HibernateDaoSupport implements route_location_listDao {

	public Integer save(route_location_list route_location_list) {
		 getHibernateTemplate().save(route_location_list);
		String hql = "select max(b.id) from route_location_list as b  ";
		List<Integer> lst = new ArrayList<Integer>();
		lst = getHibernateTemplate().find(hql);
		return lst.get(0);
	}

	public void delete(List<route_location_list> route_location_list) {
		getHibernateTemplate().delete(route_location_list);
	}

	public void update(route_location_list route_location_list) {
		getHibernateTemplate().merge(route_location_list);
	}

	public List<route_location_list> getroute_location_listById(int id) {
		@SuppressWarnings("unchecked")
		List<route_location_list> route_location_lists = (List<route_location_list>) getHibernateTemplate().find(
				"from route_location_list as b where b.id=?", id);
		return route_location_lists;
		 
	}

	public List<route_location_list> getAllroute_location_lists() {
		@SuppressWarnings("unchecked")
		List<route_location_list> route_location_lists = (List<route_location_list>) getHibernateTemplate()
				.find("from route_location_list");
		return route_location_lists;
	}

}
