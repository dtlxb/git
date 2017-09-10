package dao;

import java.util.List;

import model.route_location_list;

public interface route_location_listDao {

	public Integer save(route_location_list route_location_list);

	public void delete(List<route_location_list> thiroute_location_list);

	public void update(route_location_list route_location_list);

	public List<route_location_list>  getroute_location_listById(int id);

	public List<route_location_list> getAllroute_location_lists();

}