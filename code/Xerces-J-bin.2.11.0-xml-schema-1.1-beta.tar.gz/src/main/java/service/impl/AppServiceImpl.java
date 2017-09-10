package service.impl;

import java.util.ArrayList;
import java.util.List;

import model.route_location_list;
import model.Route;
import model.Posto;
import model.Book;
import model.Order;
import model.Friend;
import model.Orderitem;
import model.Praise;
import model.Comment;
import model.User;
import service.AppService;
import dao.BookDao;
import dao.FriendDao;
import dao.OrderDao;
import dao.CommentDao;
 
import dao.OrderitemDao;
import dao.PostoDao;
import dao.PraiseDao;
import dao.RouteDao;
import dao.UserDao;
import dao.route_location_listDao;

/**
 * @author seniyuting
 * @version 1.0
 * 
 */
public class AppServiceImpl implements AppService {

	private BookDao bookDao;
	private OrderDao orderDao;
	private OrderitemDao orderitemDao;
	private CommentDao commentDao;
	private FriendDao friendDao;
	public void setFriendDao(FriendDao friendDao) {
		this.friendDao = friendDao;
	}

	private PraiseDao praiseDao;

	public void setPraiseDao(PraiseDao praiseDao) {
		this.praiseDao = praiseDao;
	}

	private UserDao userDao;
	private PostoDao postoDao;
	private RouteDao routeDao;
	private route_location_listDao route_location_listDao;
	public void setroute_location_listDao(route_location_listDao route_location_listDao) {
		this.route_location_listDao = route_location_listDao;
	}
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
	
	public void setCommentDao(CommentDao commentDao) {
		this.commentDao = commentDao;
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
	public User getUserByUsername(String username){
		return userDao.getUserByUsername(username);
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
	public  List<Posto>  getPostoByUsername(String username){
		return postoDao.getPostoByUsername(username);
	}
	public void deletePostoById(int id) {
		Posto thiPosto=postoDao.getPostoById(id);
		postoDao.delete(thiPosto);
	}
	public List<Posto> getPostoByBelong_id(int belong_id){
		return postoDao.getPostoByBelong_id(belong_id);
	}
	
	public void updatePosto(Posto Posto) {
		postoDao.update(Posto);
	}
	public List<Posto> getPostoByName(Posto Posto){
		
	 
		List<Posto> Postos= postoDao.getPostoByName(Posto);
		List<Posto> Result =new ArrayList<Posto>();
		User user1 = userDao.getUserByUsername(Posto.getUsername());
		  
		 int uid1= user1.getId();
		for(int i =0;i<Postos.size();i++){
			
			User user2 =userDao.getUserByUsername(Postos.get(i).getUsername());
			 int uid2= user2.getId();
		 
			if(Postos.get(i).getPath_local().equals("public")){
				Result.add(Postos.get(i));
			}
			else if (Postos.get(i).getPath_local().equals("friend") ){
				
				if(friendDao.check(user1.getId(),user2.getId())==0||uid1==uid2){
					Result.add(Postos.get(i));
				}
			}
			else if (Postos.get(i).getPath_local().equals("private")){
			   System.out.print(uid1+"private"+user2.getId());
			 	if( uid1==uid2){
			 		Result.add(Postos.get(i));
			 	}
			}
		}
		return Result;
	}
	public List<Posto> getPostoByLocation(Posto Posto){
		List<Posto> Postos= postoDao.getPostoByLocation(Posto);
		List<Posto> Result =new ArrayList<Posto>();
		User user1 = userDao.getUserByUsername(Posto.getUsername());
		  
		 int uid1= user1.getId();
		for(int i =0;i<Postos.size();i++){
			
			User user2 =userDao.getUserByUsername(Postos.get(i).getUsername());
			 int uid2= user2.getId();
		 
			if(Postos.get(i).getPath_local().equals("public")){
				Result.add(Postos.get(i));
			}
			else if (Postos.get(i).getPath_local().equals("friend") ){
				
				if(friendDao.check(user1.getId(),user2.getId())==0||uid1==uid2){
					Result.add(Postos.get(i));
				}
			}
			else if (Postos.get(i).getPath_local().equals("private")){
			   System.out.print(uid1+"private"+user2.getId());
			 	if( uid1==uid2){
			 		Result.add(Postos.get(i));
			 	}
			}
		}
		return Result;
	}
	public Posto getPostoById(int id) {
		return postoDao.getPostoById(id);
		
	}
	public List<Posto> AdmingetAllPostos(){
		return postoDao.getAllPostos();
	}
	public List<Posto> getAllPostos(Posto posto) {
		List<Posto> Postos= postoDao.getAllPostos();
		List<Posto> Result =new ArrayList<Posto>();
		User user1 = userDao.getUserByUsername(posto.getUsername());
		  
		 int uid1= user1.getId();
		for(int i =0;i<Postos.size();i++){
			
			User user2 =userDao.getUserByUsername(Postos.get(i).getUsername());
			 int uid2= user2.getId();
		 
			if(Postos.get(i).getPath_local().equals("public")){
				Result.add(Postos.get(i));
			}
			else if (Postos.get(i).getPath_local().equals("friend") ){
				
				if(friendDao.check(user1.getId(),user2.getId())==0||uid1==uid2){
					Result.add(Postos.get(i));
				}
			}
			else if (Postos.get(i).getPath_local().equals("private")){
			   System.out.print(uid1+"private"+user2.getId());
			 	if( uid1==uid2){
			 		Result.add(Postos.get(i));
			 	}
			}
		}
		return Postos;
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

	public List<Route> getRouteByUsername(String username){
		return routeDao.getRouteByUsername(username);
	}
	
	public List<Route> getAllRoutes() {
		return routeDao.getAllRoutes();
	}
	public List<Route> getRouteByLocation(Posto Posto){
		return routeDao.getRouteByLocation(Posto);
	}
	/**
	 * route_location_list
	 * 
	 */
	public Integer addroute_location_list(route_location_list route_location_list) {
		return route_location_listDao.save(route_location_list);
	}

	public void deleteroute_location_list(List<route_location_list> route_location_list) {
		route_location_listDao.delete(route_location_list);
	}
	
	public void deleteroute_location_listById(int id) {
		List<route_location_list> thiroute_location_list=route_location_listDao.getroute_location_listById(id);
		route_location_listDao.delete(thiroute_location_list);
	}

	public void updateroute_location_list(route_location_list route_location_list) {
		route_location_listDao.update(route_location_list);
	}

	public List<route_location_list>  getroute_location_listById(int id) {
		return route_location_listDao.getroute_location_listById(id);
	}

	public List<route_location_list> getAllroute_location_lists() {
		return route_location_listDao.getAllroute_location_lists();
	}

	/**
	 * comment
	 * 
	 */
	public Integer addComment(Comment Comment) {
		return commentDao.save(Comment);
	}

	public void deleteComment(Comment Comment) {
		commentDao.delete(Comment);
	}

	public void updateComment(Comment Comment) {
		commentDao.update(Comment);
	}
	
	public List<Comment>  getCommentsByRid(int rid){
		return commentDao.getCommentsByRid(rid);
	}
	
	public List<Comment>  getCommentsByPid(int pid){
		return commentDao.getCommentsByPid(pid);
	}

	public Comment getCommentById(int id) {
		return commentDao.getCommentById(id);
	}

	public List<Comment> getAllComments() {
		return commentDao.getAllComments();
	}
	/**
	 * praise
	 * 
	 */
	public Integer addPraise(Praise Praise) {
		return praiseDao.save(Praise);
	}
	public List<Integer> findBest(){
		return praiseDao.findBest();
	}
	public void deletePraise(Praise Praise) {
		praiseDao.delete(Praise);
	}

	public void updatePraise(Praise Praise) {
		praiseDao.update(Praise);
	}

	public List<Praise>  getPraisesByPid(int pid){
		return praiseDao.getPraisesByPid(pid);
	}
	public List<Praise>  getPraisesByRid(int rid){
		return praiseDao.getPraisesByRid(rid);
	}
	
	public List<Praise> getPraiseByUsername(String Username) {
		return praiseDao.getPraiseByUsername(Username);
	}

	public List<Praise> getAllPraises() {
		return praiseDao.getAllPraises();
	}
	/**
	 * Friend
	 * 
	 */
	public Integer addFriend(Friend Friend) {
		return friendDao.save(Friend);
	}

	public void deleteFriend(Friend Friend) {
		friendDao.delete(Friend);
	}

	public void updateFriend(Friend Friend) {
		friendDao.update(Friend);
	}
	public Friend getFriendByUids(int uid1,int uid2,int state){
		return friendDao.getFriendByUids(uid1, uid2, state);
	}
	public List<Friend> getFriendById(int id) {
		return friendDao.getFriendByUid(id);
	}
	public int check(int uid1,int uid2){
		return friendDao.check(uid1, uid2);
	}
	public List<Friend> getAllFriends() {
		return friendDao.getAllFriends();
	}
}
