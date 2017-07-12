package dao;

import java.util.List;

import model.Posto;

public interface PostoDao {

	public Integer save(Posto Posto);

	public void delete(Posto Posto);

	public void update(Posto Posto);

	public Posto getPostoById(int id);

	public List<Posto> getAllPostos();

}