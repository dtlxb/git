package dao;

import java.util.List;

import model.Posto;
import model.Route;

public interface RouteDao {

	public Integer save(Route route);

	public void delete(Route route);

	public void update(Route route);

	public Route getRouteById(int id);

	public List<Route> getAllRoutes();

}