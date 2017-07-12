package service.impl;

import java.util.List;

import model.Route;
import model.Posto;
import model.Book;
import model.Order;
import model.Orderitem;
import model.User;
import service.AppService;
import dao.BookDao;
import dao.OrderDao;
import dao.OrderitemDao;
import dao.PostoDao;
import dao.RouteDao;
import dao.UserDao;

/**
 * @author seniyuting
 * @version 1.0
 * 
 */
public class AppServiceImpl implements AppService {

	private BookDao bookDao;
	private OrderDao orderDao;
	private OrderitemDao orderitemDao;
	private UserDao userDao;
	private PostoDao postoDao;
	private RouteDao routeDao;
	
	public void setRouteDao(RouteDao routeDao) {
		this.routeDao = routeDao;
	}
	public void setPostoDao(PostoDao postoDao) {
		this.postoDao= postoDao;
	}
	public void setBookDao(BookDao bookDao) {
		this.bookDao = bookDao;
	}

	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}

	public void setOrderitemDao(OrderitemDao orderitemDao) {
		this.orderitemDao = orderitemDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	/**
	 * book
	 * 
	 */
	public Integer addBook(Book book) {
		return bookDao.save(book);
	}

	public void deleteBook(Book book) {
		bookDao.delete(book);
	}
	
	public void deleteBookById(int id) {
		Book thibook=bookDao.getBookById(id);
		bookDao.delete(thibook);
	}

	public void updateBook(Book book) {
		bookDao.update(book);
	}

	public Book getBookById(int id) {
		return bookDao.getBookById(id);
	}

	public List<Book> getAllBooks() {
		return bookDao.getAllBooks();
	}

	/**
	 * order
	 * 
	 */
	public Integer addOrder(Order order) {
		return orderDao.save(order);
	}

	public void deleteOrder(Order order) {
		orderDao.delete(order);
	}

	public void updateOrder(Order order) {
		orderDao.update(order);
	}

	public Order getOrderById(int id) {
		return orderDao.getOrderById(id);
	}

	public List<Order> getAllOrders() {
		return orderDao.getAllOrders();
	}

	/**
	 * order item
	 * 
	 */
	public Integer addOrderitem(Orderitem orderitem) {
		return orderitemDao.save(orderitem);
	}

	public void deleteOrderitem(Orderitem orderitem) {
		orderitemDao.delete(orderitem);
	}

	public void updateOrderitem(Orderitem orderitem) {
		orderitemDao.update(orderitem);
	}

	public Orderitem getOrderitemById(int id) {
		return orderitemDao.getOrderitemById(id);
	}

	public List<Orderitem> getAllOrderitems() {
		return orderitemDao.getAllOrderitems();
	}

	/**
	 * user
	 * 
	 */
	public Integer addUser(User user) {
		return userDao.save(user);
	}
	public Integer checkUser (String username,String password){
		return userDao.check(username,password);
	}
	public void deleteUser(User user) {
		userDao.delete(user);
	}

	public void updateUser(User user) {
		userDao.update(user);
	}

	public User getUserById(int id) {
		return userDao.getUserById(id);
	}
	public Integer checkUsername (String username,String password){
		return userDao.checkUsername (username,password);
	}
	public List<User> getAllUsers() {
		return userDao.getAllUsers();
	}
	
	/**
	 * Posto
	 * 
	 */
	public Integer addPosto(Posto Posto) {
		return postoDao.save(Posto);
	}

	public void deletePosto(Posto Posto) {
		postoDao.delete(Posto);
	}
	
	public void deletePostoById(int id) {
		Posto thiPosto=postoDao.getPostoById(id);
		postoDao.delete(thiPosto);
	}

	public void updatePosto(Posto Posto) {
		postoDao.update(Posto);
	}

	public Posto getPostoById(int id) {
		return postoDao.getPostoById(id);
	}

	public List<Posto> getAllPostos() {
		return postoDao.getAllPostos();
	}
	/**
	 * Route
	 * 
	 */
	public Integer addRoute(Route Route) {
		return routeDao.save(Route);
	}

	public void deleteRoute(Route Route) {
		routeDao.delete(Route);
	}
	
	public void deleteRouteById(int id) {
		Route thiRoute=routeDao.getRouteById(id);
		routeDao.delete(thiRoute);
	}

	public void updateRoute(Route Route) {
		routeDao.update(Route);
	}

	public Route getRouteById(int id) {
		return routeDao.getRouteById(id);
	}

	public List<Route> getAllRoutes() {
		return routeDao.getAllRoutes();
	}

}
