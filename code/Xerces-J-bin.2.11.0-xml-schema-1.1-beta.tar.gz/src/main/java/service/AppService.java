package service;

import java.util.List;

import model.Book;
import model.Friend;
import model.Order;
import model.Praise;
import model.Comment;
import model.Friend;
import model.Orderitem;
import model.route_location_list;
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
	public User getUserByUsername(String username);
	public List<User> getAllUsers();
	/**
	 * posto
	 * 
	 */
	public List<Posto> getPostoByLocation(Posto Posto);
	public Integer addPosto(Posto Posto);
	public  List<Posto>  getPostoByUsername(String username);
	public List<Posto> getPostoByBelong_id(int belong_id);
	public List<Posto> AdmingetAllPostos();
	public void deletePosto(Posto Posto);
	public List<Posto> getPostoByName(Posto Posto);
	public void deletePostoById(int id);

	public void updatePosto(Posto Posto);

	public Posto getPostoById(int id);

	public List<Posto> getAllPostos(Posto posto);
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
	
	public List<Route> getRouteByLocation(Posto Posto);
	public List<Route> getRouteByUsername(String username);
	/**
	 * route_location_list
	 * 
	 */
	public Integer addroute_location_list(route_location_list route_location_list);

	public void deleteroute_location_list(List<route_location_list> route_location_list);
	
	public void deleteroute_location_listById(int id);

	public void updateroute_location_list(route_location_list route_location_list);

	public List<route_location_list>  getroute_location_listById(int id);

	public List<route_location_list> getAllroute_location_lists();
	/**
	 * Comment
	 * 
	 */
	public Integer addComment(Comment Comment);

	public void deleteComment(Comment Comment);

	public void updateComment(Comment Comment);

	public Comment getCommentById(int id);
	public List<Comment>  getCommentsByPid(int pid);
	public List<Comment> getAllComments();
	public List<Comment>  getCommentsByRid(int rid);
	/**
	 * Praise
	 * 
	 */
	public Integer addPraise(Praise Praise);

	public void deletePraise(Praise Praise);

	public void updatePraise(Praise Praise);
	public List<Praise>  getPraisesByPid(int pid);
	public List<Praise> getPraiseByUsername(String Username);
	public List<Praise>  getPraisesByRid(int rid);
	public List<Praise> getAllPraises();
	public List<Integer> findBest( );
	/**
	 * Friend
	 * 
	 */
	
	public Integer addFriend(Friend Friend);

	public void deleteFriend(Friend Friend);

	public void updateFriend(Friend Friend);
	public Friend getFriendByUids(int uid1,int uid2,int state);
	public List<Friend> getFriendById(int id);
	public int check(int uid1,int uid2);
	public List<Friend> getAllFriends();
}
