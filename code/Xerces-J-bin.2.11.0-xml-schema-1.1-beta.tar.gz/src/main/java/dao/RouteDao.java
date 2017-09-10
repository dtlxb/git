package dao;

import java.util.List;

import model.Posto;
import model.Route;

public interface RouteDao {

	public Integer save(Route route);

	public void delete(Route route);

	public void update(Route route);

	public Route getRouteById(int id);
	List<Route> getRouteByLocation(Posto Posto);
	public List<Route> getAllRoutes();
	public List<Route> getRouteByUsername(String username);
}