package dao.impl;

import java.util.ArrayList;
import java.util.List;

import model.Posto;
import model.Route;
import model.Route;
import model.route_location_list;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;








import dao.RouteDao;

public class RouteDaoImpl extends HibernateDaoSupport implements RouteDao {

	public Integer save(Route route) {
		getHibernateTemplate().save(route);
		String hql = "select max(b.rid) from Route as b  ";
		List<Integer> lst = new ArrayList<Integer>();
		lst = getHibernateTemplate().find(hql);
		return lst.get(0);
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
				"from Route as b where b.rid=?", id);
		Route route= Routes.size() > 0 ? Routes.get(0) : null;
		return route;
	}

	public List<Route> getRouteByUsername(String username) {
		@SuppressWarnings("unchecked")
		List<Route> Routes = (List<Route>) getHibernateTemplate().find(
				"from Route as b where b.username=?", username);
 
		return Routes;
	}
	public List<Route> getRouteByLocation(Posto Posto) {
		double lat = Posto.getLatitude();
		double lon = Posto.getLongitude();
		System.out.print("lat"+lat+"lon"+lon);
		@SuppressWarnings("unchecked")
		List<Route> Routes = (List<Route>) getHibernateTemplate()
		.find("from Route");
		List<Route> nearRoutes = new ArrayList<Route>() ;
		for(int i = 0 ; i <  Routes.size();i++){
			List<route_location_list> locations = (List<route_location_list>) getHibernateTemplate().find(
					"from route_location_list as b where b.id = ?",Routes.get(i).getRid());
			System.out.print(Routes.get(i).getRid()+"size"+locations.size());
			
			
			if (locations.size()!=0){
				double max_lat = locations.get(0).getLatitude();
				double min_lat = locations.get(0).getLatitude();
				
				double max_lon = locations.get(0).getLongitude();
				double min_lon = locations.get(0).getLongitude();

			
				for(int j = 0 ; j <  locations.size();j++){
					System.out.print(locations.get(j).getIndex()+"latitude/n"+j);
					if( locations.get(j).getLatitude() > max_lat){
						 max_lat=locations.get(j).getLatitude();
					}
					
					if( locations.get(j).getLatitude() < min_lat){
						 min_lat=locations.get(j).getLatitude();
					}
					if( locations.get(j).getLongitude() > max_lon){
						 max_lon=locations.get(j).getLongitude();
					}
					if( locations.get(j).getLongitude() < min_lon){
						 min_lon=locations.get(j).getLongitude();
					}
 
				}
				
				System.out.print("lat"+max_lat+"lon"+max_lon+"minlat"+min_lat+"maxlon"+max_lon);
				if((min_lat-0.002)<lat&&lat<(max_lat+0.002)&&(min_lon-0.002)<lon&&lon<(max_lon+0.002)){
					nearRoutes.add(Routes.get(i));
				}
			}
		}

		 
		return nearRoutes;
	}
	public List<Route> getAllRoutes() {
		@SuppressWarnings("unchecked")
		List<Route> Routes = (List<Route>) getHibernateTemplate()
				.find("from Route");
		return Routes;
	}



 

}
