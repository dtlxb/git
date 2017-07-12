package dao;

import java.util.List;

import model.User;

public interface UserDao {

	public Integer save(User user);

	public void delete(User user);

	public void update(User user);
	public Integer check (String username,String password);
	
	public User getUserById(int id);
	public Integer checkUsername(String username,String password);
	public List<User> getAllUsers();

}