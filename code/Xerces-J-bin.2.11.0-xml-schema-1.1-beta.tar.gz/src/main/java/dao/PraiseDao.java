package dao;

import java.util.List;

import model.Praise;

public interface PraiseDao {

	public Integer save(Praise Praise);

	public void delete(Praise Praise);

	public void update(Praise Praise);
	public List<Integer> findBest( );
	public List<Praise> getPraiseByUsername(String Username);
	public List<Praise>  getPraisesByPid(int pid);
	public List<Praise> getAllPraises();
	public List<Praise>  getPraisesByRid(int rid);
}