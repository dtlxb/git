package dao.impl;

import java.util.List;

import model.Posto;
import model.Route;
import model.Route;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;





import dao.RouteDao;

public class RouteDaoImpl extends HibernateDaoSupport implements RouteDao {

	public Integer save(Route route) {
		return (Integer) getHibernateTemplate().save(route);
	}

	public void delete(Route route) {
		getHibernateTemplate().delete(route);
	}

	public void update(Route route) {
		getHibernateTemplate().merge(route);
	}

	public Route getRouteById(int id) {
		@SuppressWarnings("unchecked")
		List<Route> Routes = (List<Route>) getHibernateTemplate().find(
				"from Routeas b where b.rid=?", id);
		Route route= Routes.size() > 0 ? Routes.get(0) : null;
		return route;
	}



	public List<Route> getAllRoutes() {
		@SuppressWarnings("unchecked")
		List<Route> Routes = (List<Route>) getHibernateTemplate()
				.find("from Route");
		return Routes;
	}



 

}
