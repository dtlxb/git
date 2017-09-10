package dao;

import java.util.List;

import model.Posto;

public interface PostoDao {

	public Integer save(Posto Posto);

	public void delete(Posto Posto);

	public void update(Posto Posto);
	public List<Posto> getPostoByLocation(Posto Posto);
	public Posto getPostoById(int id);
	public List<Posto> getPostoByBelong_id(int belong_id);
	public List<Posto> getAllPostos();
	public  List<Posto>  getPostoByUsername(String username);
	public List<Posto> getPostoByName(Posto Posto);
}