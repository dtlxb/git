package service;

import java.util.List;

import model.Book;
import model.Order;
import model.Orderitem;
import model.Route;
import model.Posto;
import model.User;

/**
 * @author seniyuting
 * @version 1.0
 * 
 */
public interface AppService {

	/**
	 * book
	 * 
	 */
	public Integer addBook(Book book);

	public void deleteBook(Book book);
	
	public void deleteBookById(int id);

	public void updateBook(Book book);

	public Book getBookById(int id);

	public List<Book> getAllBooks();

	/**
	 * order
	 * 
	 */
	public Integer addOrder(Order order);

	public void deleteOrder(Order order);

	public void updateOrder(Order order);

	public Order getOrderById(int id);

	public List<Order> getAllOrders();

	/**
	 * order item
	 * 
	 */
	public Integer addOrderitem(Orderitem orderitem);

	public void deleteOrderitem(Orderitem orderitem);

	public void updateOrderitem(Orderitem orderitem);

	public Orderitem getOrderitemById(int id);

	public List<Orderitem> getAllOrderitems();

	/**
	 * user
	 * 
	 */
	public Integer addUser(User user);

	public void deleteUser(User user);

	public void updateUser(User user);
	public Integer checkUser (String username,String password);
	public Integer checkUsername (String username,String password);
	public User getUserById(int id);

	public List<User> getAllUsers();
	/**
	 * posto
	 * 
	 */
	
	public Integer addPosto(Posto Posto);

	public void deletePosto(Posto Posto);
	
	public void deletePostoById(int id);

	public void updatePosto(Posto Posto);

	public Posto getPostoById(int id);

	public List<Posto> getAllPostos();
	/**
	 * route
	 * 
	 */
	public Integer addRoute(Route Route);

	public void deleteRoute(Route Route);
	
	public void deleteRouteById(int id);

	public void updateRoute(Route Route);

	public Route getRouteById(int id);

	public List<Route> getAllRoutes();
}
