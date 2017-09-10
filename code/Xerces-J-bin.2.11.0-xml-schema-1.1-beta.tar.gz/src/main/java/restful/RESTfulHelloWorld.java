package restful;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import model.Book;
import model.Comment;
import model.Friend;
import model.ImageZipUtil;
import model.Posto;
import model.Praise;
import model.Route;
import model.Route.MyLatlng;
import model.User;
import model.Photo;
import model.route_location_list;
import service.AppService;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import util.SpringContextUtil;

 
@Path("/")
public class RESTfulHelloWorld 
{

	private AppService appService=(AppService) SpringContextUtil.getBean("appService");

	@GET
	@Produces("text/html")
	public Response getStartingPage()
	{
		String output = "<h1>No Zuo No Die!<h1>";
		return Response.status(200).entity(output).build();
	}
	
	@GET
	@Path("/getBooks")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Book>  AllBooks()
	{
		System.out.println("getBooks");
		List<Book> books = appService.getAllBooks();
		return books;
	}

	
	 @POST
     @Path("/addPosto")
	 //@Produces(MediaType.APPLICATION_JSON)
	 //@Consumes(MediaType.APPLICATION_OCTET_STREAM)
	 //从app获取posto信息，并储存
	 @Consumes(MediaType.APPLICATION_JSON)
	 @Produces("text/html")
     public String addPosto(Posto posto){
		 
		 System.out.println("addUser");
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		// 转换成字符串
		String strTime = sdf.format(posto.getDate());
	
	//	 Date date=new java.sql.Date(System.currentTimeMillis());
		 System.out.print(strTime);
	//	posto.setDate((java.sql.Date) date);
		 int id = -1;

		try {
			String picurl = posto.getImage();
			 byte[] photoimg = Base64.decodeBase64(picurl.replace("data:image/png;base64,",""));
			 picurl = picurl.replace("base64,","");
            for (int i = 0; i < photoimg.length; ++i) {  
                if (photoimg[i] < 0) {  
                    // 调整异常数据  
                    photoimg[i] += 256;  
                }  
            }


    
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");//获取当前时间，进一步转化为字符串

          //  String str = format.format(date);
          //  posto.setPath_server("D://bzbp/"+ posto.getUsername()+"_"+date+".png");
            
            long time =System.currentTimeMillis();
            File file = new File("D://bzbp/", posto.getUsername()+"_"+time+".png");  
            
            if (!file.exists()) {  
                file.createNewFile();  
            }  
            FileOutputStream out = new FileOutputStream(file);  
	            out.write(photoimg);  
	            out.flush();  
	            out.close();  
	            posto.setPath_server("D:\\bzbp/"+ posto.getUsername()+"_"+time+".png");
	    		 id = appService.addPosto(posto);
	    		
	    		 ImageZipUtil a = new ImageZipUtil();
	    		 File file1 =new File("D://bzbp/", posto.getUsername()+"_"+time+"_small.png");  
	    		 a.zipImageFile(file,  file1,200, 200, 1);
			//  ImageIO.write(bi1,"jpg", w2);//不管输出什么格式图片，此处不需改动 
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 

		 return id+"";
     }

	
	@GET
	@Path("/getPhoto")
	@Produces(MediaType.APPLICATION_JSON)
	public Photo GetPhoto()
	{
		Photo photo =new Photo();
		 BASE64Encoder encoder = new sun.misc.BASE64Encoder(); 
		 
		  File f = new File("D://bzbp/wjb_1.png");
		  BufferedImage bi;  
		  try {
			bi = ImageIO.read(f);
			  ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
			  ImageIO.write(bi,"jpg", baos); 
			  byte[] bytes = baos.toByteArray(); 
			 String picurl= Base64.encodeBase64String(bytes);
	 
			 
			 photo .setname("a");
			 photo.setcontent(picurl );
			 return  photo;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		  

		return  photo;
	}
	

		@POST
		@Path("/getPostosBy")
		@Produces(MediaType.APPLICATION_JSON)
		public List<Posto>  getPostosBy(Posto posto)
		{

			List<Posto> Postos = appService.getAllPostos(posto);
			for(int i=0; i < Postos.size();i++){
				 BASE64Encoder encoder = new sun.misc.BASE64Encoder(); 
				 
				  File f = new File(Postos.get(i).getPath_server().replace(".png", "_small.png"));
				  System.out.print(Postos.get(i).getPath_server());
				  BufferedImage bi;  
				  try {
					bi = ImageIO.read(f);
					  ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
					  ImageIO.write(bi,"jpg", baos); 
					  byte[] bytes = baos.toByteArray(); 
					 String picurl= Base64.encodeBase64String(bytes);
			 
					 
		 
					 Postos.get(i).setImage(picurl);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
			System.out.println(Postos.size());
			return Postos;
		}
		@POST
		@Path("/getPostosByLocation")
		@Produces(MediaType.APPLICATION_JSON)
		public List<Posto>  getPostosByLocation(Posto posto)
		{

			List<Posto> Postos = appService.getPostoByLocation(posto);
			for(int i=0; i < Postos.size();i++){
				 BASE64Encoder encoder = new sun.misc.BASE64Encoder(); 
				 
				  File f = new File(Postos.get(i).getPath_server().replace(".png", "_small.png"));
				  System.out.print(Postos.get(i).getPath_server());
				  BufferedImage bi;  
				  try {
					bi = ImageIO.read(f);
					  ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
					  ImageIO.write(bi,"jpg", baos); 
					  byte[] bytes = baos.toByteArray(); 
					 String picurl= Base64.encodeBase64String(bytes);
			 
					 
		 
					 Postos.get(i).setImage(picurl);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
			System.out.println(Postos.size());
			return Postos;
		}
		@POST
		@Path("/getPostosByName")
		@Produces(MediaType.APPLICATION_JSON)
		public List<Posto>  getPostosByName(Posto posto)
		{

			List<Posto> Postos = appService.getPostoByName(posto);
			for(int i=0; i < Postos.size();i++){
				 BASE64Encoder encoder = new sun.misc.BASE64Encoder(); 
				 
				  File f = new File(Postos.get(i).getPath_server().replace(".png", "_small.png"));
				  System.out.print(Postos.get(i).getPath_server());
				  BufferedImage bi;  
				  try {
					bi = ImageIO.read(f);
					  ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
					  ImageIO.write(bi,"jpg", baos); 
					  byte[] bytes = baos.toByteArray(); 
					 String picurl= Base64.encodeBase64String(bytes);
			 
					 
		 
					 Postos.get(i).setImage(picurl);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
			System.out.println(Postos.size());
			return Postos;
		}		
		@POST
		@Path("/getPostosByRouteId")
		@Produces(MediaType.APPLICATION_JSON)
		public List<Posto>  getPostosByRouteid(Posto posto)
		{
 
			System.out.println(posto.getPid());
			 List<Posto> Postos=appService.getPostoByBelong_id(posto.getPid());
			for(int i=0; i < Postos.size();i++){
				 BASE64Encoder encoder = new sun.misc.BASE64Encoder(); 
				 
				  File f = new File(Postos.get(i).getPath_server().replace(".png", "_small.png"));
				  System.out.print(Postos.get(i).getPath_server());
				  BufferedImage bi;  
				  try {
					bi = ImageIO.read(f);
					  ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
					  ImageIO.write(bi,"jpg", baos); 
					  byte[] bytes = baos.toByteArray(); 
					 String picurl= Base64.encodeBase64String(bytes);
			 
					 
		 
					 Postos.get(i).setImage(picurl);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
			System.out.println(Postos.size());
			List<Posto> Result =new ArrayList<Posto>();
			for(int i =0;i<Postos.size();i++){
				User user1 =appService.getUserByUsername(posto.getUsername());
				User user2 =appService.getUserByUsername(Postos.get(i).getUsername());
				
				if(Postos.get(i).getPath_local().equals("public")){
					Result.add(Postos.get(i));
				}
				else if (Postos.get(i).getPath_local().equals("friend") ){

					if(appService.check(user1.getId(),user2.getId())==0||user1.getId()==user2.getId()){
						Result.add(Postos.get(i));
					}
				}
				else if (Postos.get(i).getPath_local().equals("private")){
					if( user1.getId()==user2.getId()){
						Result.add(Postos.get(i));
					}
				}
			}
			return Result ;
		}
		
		
		@POST
		@Path("/getPostoById")
		@Produces(MediaType.APPLICATION_JSON)
		public  List<Posto>   getPostoById(Posto posto)
		{
			
			Posto postoFind = appService.getPostoById(posto.getPid()) ;
		//	List<Posto> Postos = appService.getAllPostos();
		//	for(int i=0; i < Postos.size();i++){
				 BASE64Encoder encoder = new sun.misc.BASE64Encoder(); 
				 
				  File f = new File(postoFind.getPath_server().replace(".png", "_small.png"));
				  System.out.print(postoFind.getPath_server());
				  BufferedImage bi;  
				  try {
					bi = ImageIO.read(f);
					  ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
					  ImageIO.write(bi,"jpg", baos); 
					  byte[] bytes = baos.toByteArray(); 
					 String picurl= Base64.encodeBase64String(bytes);
			 
					 
		 
					 postoFind.setImage(picurl);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			//}
			System.out.println("postoFind"+postoFind.getName());
			List<Posto> Postos = new ArrayList<Posto>();
			Postos.add(postoFind);
			List<Posto> Result =new ArrayList<Posto>();
			for(int i =0;i<Postos.size();i++){
				User user1 =appService.getUserByUsername(posto.getUsername());
				User user2 =appService.getUserByUsername(Postos.get(i).getUsername());
				
				if(Postos.get(i).getPath_local().equals("public")){
					Result.add(Postos.get(i));
				}
				else if (Postos.get(i).getPath_local().equals("friend") ){

					if(appService.check(user1.getId(),user2.getId())==0||user1.getId()==user2.getId()){
						Result.add(Postos.get(i));
					}
				}
				else if (Postos.get(i).getPath_local().equals("private")){
					if( user1.getId()==user2.getId()){
						Result.add(Postos.get(i));
					}
				}
			}
			return Result ;
		}
	
		@POST
		@Path("/getPostoByPraiseNumber")
		@Produces(MediaType.APPLICATION_JSON)
		public  List<Posto>   getPostoByPraiseNumber(Posto posto)
		{
			List<Integer> best = appService.findBest();
			List<Posto> Postos = new ArrayList<Posto>();
			
			for(int i =0;i<best.size();i++){
				Posto postoFind = appService.getPostoById(best.get(i)) ;
				 BASE64Encoder encoder = new sun.misc.BASE64Encoder(); 
				 
				  File f = new File(postoFind.getPath_server().replace(".png", "_small.png"));
				  System.out.print(postoFind.getPath_server());
				  BufferedImage bi;  
				  try {
					bi = ImageIO.read(f);
					  ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
					  ImageIO.write(bi,"jpg", baos); 
					  byte[] bytes = baos.toByteArray(); 
					 String picurl= Base64.encodeBase64String(bytes);
			 
					 
		 
					 postoFind.setImage(picurl);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				  Postos.add(postoFind);
			}
 
			return Postos;
		}
		
		@POST
		@Path("/getPostoByUsername")
		@Produces(MediaType.APPLICATION_JSON)
		public  List<Posto>   getPostoByUsername(Posto posto)
		{
			
			List<Posto> Postos = appService.getPostoByUsername(posto.getUsername()) ;
			for(int i=0; i < Postos.size();i++){
				 BASE64Encoder encoder = new sun.misc.BASE64Encoder(); 
				 
				  File f = new File(Postos.get(i).getPath_server().replace(".png", "_small.png"));
				  System.out.print(Postos.get(i).getPath_server());
				  BufferedImage bi;  
				  try {
					bi = ImageIO.read(f);
					  ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
					  ImageIO.write(bi,"jpg", baos); 
					  byte[] bytes = baos.toByteArray(); 
					 String picurl= Base64.encodeBase64String(bytes);
			 
					 
		 
					 Postos.get(i).setImage(picurl);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
			System.out.println(Postos.size());
			List<Posto> Result =new ArrayList<Posto>();  
			for(int i =0;i<Postos.size();i++){
				User user1 =appService.getUserByUsername(posto.getUsername());
				User user2 =appService.getUserByUsername(Postos.get(i).getUsername());
				
				if(Postos.get(i).getPath_local().equals("public")){
					Result.add(Postos.get(i));
				}
				else if (Postos.get(i).getPath_local().equals("friend") ){

					if(appService.check(user1.getId(),user2.getId())==0||user1.getId()==user2.getId()){
						Result.add(Postos.get(i));
					}
				}
				else if (Postos.get(i).getPath_local().equals("private")){
					if( user1.getId()==user2.getId()){
						Result.add(Postos.get(i));
					}
				}
			}
			return Result ;
		}
	
		
		/*--------------------------------------------------------------------------------------*/
	//route
		
		 @POST
	     @Path("/addRoute")
		 //@Produces(MediaType.APPLICATION_JSON)
		 //@Consumes(MediaType.APPLICATION_OCTET_STREAM)
		 @Consumes(MediaType.APPLICATION_JSON)
		 @Produces("text/html")
	     public String addRoute(Route route){
			 int rid = -1;
			 System.out.print(route.getRid()+"rid");
			 if(route.getRid()==0){
				// route.setRid(null);
				 route.setEnd_time(0.0);
				 route.setStart_time(0.0);
				  rid =appService.addRoute(route);
				 
			}
			else{
				rid = route.rid;
				 appService.updateRoute(route);
				for (int i = 1 ;i < route.location_list.size()+1;i++){
					 

					route_location_list rll=new route_location_list();
					rll.setId(route.getRid());
					rll.setIndex(i-1);
					rll.setLatitude(route.location_list.get(i-1).latitude);
					rll.setLongitude( route.location_list.get(i-1).longitude);
					appService.addroute_location_list(rll);

				}
					for (int i = 0 ;i < route.pids.size();i++){
						 System.out.print("rid="+route.rid);
						 	Posto postotemp = appService.getPostoById(route.pids.get(i));
						 	postotemp.setBelong_rid(rid);
						 	appService.updatePosto(postotemp);
	
				  }
				
			}
			

			System.out.println("route:"+route.location_list.size());
			return  rid+"";
	     }
		 
		
		

			@POST
			@Path("/getRoutesBy")
			@Produces(MediaType.APPLICATION_JSON)
			public List<Route>  getRoutesBy(Posto posto)
			{
				int rid;

				List<Route> Routes = appService.getAllRoutes();
			
			 	for(int i=0; i <Routes.size();i++){
			    	ArrayList<MyLatlng> Latlngs= new ArrayList<MyLatlng>();
				    rid= Routes.get(i).getRid();
			        List<route_location_list> locations = appService.getroute_location_listById(rid);	
	 
					for(int j=0; j <locations.size();j++){
						MyLatlng Latlngtem=new MyLatlng();
						Latlngtem.latitude=locations.get(j).getLatitude();
						Latlngtem.longitude=locations.get(j).getLongitude();
						Latlngs.add(Latlngtem);
					}
					
					  ArrayList<Integer> pids = new  ArrayList<Integer>();
					List<Posto> postos =  appService.getPostoByBelong_id(rid);
					for(int j=0; j <postos.size();j++){
						pids.add(postos.get(j).getPid());
					}
					
					
					
					Routes.get(i).setPids(pids);
					Routes.get(i).setLocation_list(Latlngs);
				}
				System.out.println(Routes.size());
				return Routes;
			}

			@POST
			@Path("/getRoutesById")
			@Produces(MediaType.APPLICATION_JSON)
			public List<Route>  getRoutesById(Posto posto)
			{
				int rid;
                Route route_tem=appService.getRouteById(posto.getBelong_rid());
				List<Route> Routes  =new ArrayList<Route> ();
				Routes.add(route_tem);
			 	for(int i=0; i <Routes.size();i++){
			    	ArrayList<MyLatlng> Latlngs= new ArrayList<MyLatlng>();
				    rid= Routes.get(i).getRid();
			        List<route_location_list> locations = appService.getroute_location_listById(rid);	
	 
					for(int j=0; j <locations.size();j++){
						MyLatlng Latlngtem=new MyLatlng();
						Latlngtem.latitude=locations.get(j).getLatitude();
						Latlngtem.longitude=locations.get(j).getLongitude();
						Latlngs.add(Latlngtem);
					}
					
					  ArrayList<Integer> pids = new  ArrayList<Integer>();
					List<Posto> postos =  appService.getPostoByBelong_id(rid);
					for(int j=0; j <postos.size();j++){
						pids.add(postos.get(j).getPid());
					}
					
					
					
					Routes.get(i).setPids(pids);
					Routes.get(i).setLocation_list(Latlngs);
				}
				System.out.println(Routes.size());
				return Routes;
			}

			
			
			@POST
			@Path("/getRoutesByLocation")
			@Produces(MediaType.APPLICATION_JSON)
			public List<Route>  getRoutesByLocation(Posto posto)
			{
				int rid;

				List<Route> Routes = appService.getRouteByLocation(posto);
			
				
				
				
			 	for(int i=0; i <Routes.size();i++){
			    	ArrayList<MyLatlng> Latlngs= new ArrayList<MyLatlng>();
				    rid= Routes.get(i).getRid();
			        List<route_location_list> locations = appService.getroute_location_listById(rid);	
	 
					for(int j=0; j <locations.size();j++){
						MyLatlng Latlngtem=new MyLatlng();
						Latlngtem.latitude=locations.get(j).getLatitude();
						Latlngtem.longitude=locations.get(j).getLongitude();
						Latlngs.add(Latlngtem);
					}
					
					  ArrayList<Integer> pids = new  ArrayList<Integer>();
					List<Posto> postos =  appService.getPostoByBelong_id(rid);
					for(int j=0; j <postos.size();j++){
						pids.add(postos.get(j).getPid());
					}
					
					
					
					Routes.get(i).setPids(pids);
					Routes.get(i).setLocation_list(Latlngs);
				}
				System.out.println(Routes.size());
				return Routes;
			}	

			@POST
			@Path("/getRoutesByUsername")
			@Produces(MediaType.APPLICATION_JSON)
			public List<Route>  getRoutesByUsername(Posto posto)
			{
				int rid;

				List<Route> Routes = appService.getRouteByUsername(posto.getUsername());
			
				
				
				
			 	for(int i=0; i <Routes.size();i++){
			    	ArrayList<MyLatlng> Latlngs= new ArrayList<MyLatlng>();
				    rid= Routes.get(i).getRid();
			        List<route_location_list> locations = appService.getroute_location_listById(rid);	
	 
					for(int j=0; j <locations.size();j++){
						MyLatlng Latlngtem=new MyLatlng();
						Latlngtem.latitude=locations.get(j).getLatitude();
						Latlngtem.longitude=locations.get(j).getLongitude();
						Latlngs.add(Latlngtem);
					}
					
					  ArrayList<Integer> pids = new  ArrayList<Integer>();
					List<Posto> postos =  appService.getPostoByBelong_id(rid);
					for(int j=0; j <postos.size();j++){
						pids.add(postos.get(j).getPid());
					}
					
					
					
					Routes.get(i).setPids(pids);
					Routes.get(i).setLocation_list(Latlngs);
				}
				System.out.println(Routes.size());
				return Routes;
			}				 
/*-------------------------------------------------------------------------------------------------------*/
	
	//comment
			
			
			
			

			 @POST
		     @Path("/addComment")
			 //@Produces(MediaType.APPLICATION_JSON)
			 //@Consumes(MediaType.APPLICATION_OCTET_STREAM)
			 @Consumes(MediaType.APPLICATION_JSON)
			 @Produces("text/html")
		     public String addComment(Comment comment){
 
				 appService.addComment(comment);
 
				return  "SUCCESS";
		     }
			 
			 

				@POST
				@Path("/getComments")
				@Produces(MediaType.APPLICATION_JSON)
				public List<Comment>  getComments(Posto posto)
				{
				 

					List<Comment> Comments = appService.getCommentsByPid(posto.getPid());
					System.out.print(Comments.size());
					return Comments;
				}
				@POST
				@Path("/getCommentsRoute")
				@Produces(MediaType.APPLICATION_JSON)
				public List<Comment>  getCommentsRoute(Posto posto)
				{
				 

					List<Comment> Comments = appService.getCommentsByRid(posto.getPid());
					System.out.print(Comments.size());
					return Comments;
				}

				/*-------------------------------------------------------------------------------------------------------*/
				
				//praise
						
						
						
						

						 @POST
					     @Path("/addPraise")
						 //@Produces(MediaType.APPLICATION_JSON)
			   			 //@Consumes(MediaType.APPLICATION_OCTET_STREAM)
						 @Consumes(MediaType.APPLICATION_JSON)
						 @Produces("text/html")
					     public String addPraise(Praise Praise){
			 
							 appService.addPraise(Praise);
			 
							return  "SUCCESS";
					     }
						 
						 

							@POST
							@Path("/getPraises")
							@Produces(MediaType.APPLICATION_JSON)
							public int  getPraises(Posto posto)
							{
							 
								
								List<Praise> Praises = appService.getPraisesByPid(posto.getPid());
								int size =  Praises.size();
								for(int i =0;i< Praises.size();i++){
									if (Praises.get(i).getUsername().equals(posto.getUsername())){
										size =  -1*size;
										break;
									}
								}
								System.out.print(size);
								return size;
							}	
							
							
							
							@POST
							@Path("/getPraisesRoute")
							@Produces(MediaType.APPLICATION_JSON)
							public int  getPraisesRoute(Posto posto)
							{
							 
								
								List<Praise> Praises = appService.getPraisesByRid(posto.getPid());
								int size =  Praises.size();
								for(int i =0;i< Praises.size();i++){
									if (Praises.get(i).getUsername().equals(posto.getUsername()) ){
										size =  -1*size;
										break;
									}
								}
								System.out.print("size:"+size);
								return size;
							}
							
	/*-------------------------------------------------------------------------------------------------------*/
							
	//Friend
	 //uid1 申请人 uid2被申请好友的那个人
							 @POST
						     @Path("/deleteFriend")
							 //@Produces(MediaType.APPLICATION_JSON)
							 //@Consumes(MediaType.APPLICATION_OCTET_STREAM)
							 @Consumes(MediaType.APPLICATION_JSON)
							 @Produces("text/html")
						     public String deleteFriend(Posto posto){
								 Friend friend=new Friend() ;
 
								friend=appService.getFriendByUids(posto.getPid(), posto.getBelong_rid(), 1);
								appService.deleteFriend(friend);
 
								return  "1";
						     }
							 @POST
						     @Path("/addFriend")
							 //@Produces(MediaType.APPLICATION_JSON)
							 //@Consumes(MediaType.APPLICATION_OCTET_STREAM)
							 @Consumes(MediaType.APPLICATION_JSON)
							 @Produces("text/html")
						     public String addFriend(Posto posto){
								 Friend friend=new Friend() ;
								 friend.setUid1(posto.getPid());
								 friend.setUid2(posto.getBelong_rid());
								 friend.setState(1);
							 	appService.addFriend(friend);
								friend=appService.getFriendByUids(posto.getPid(), posto.getBelong_rid(), 0);
								appService.deleteFriend(friend);
								friend=appService.getFriendByUids(posto.getPid(), posto.getBelong_rid(), 0);
								if(friend!=null)appService.deleteFriend(friend);
								return  "1";
						     }
							 
							 @POST
						     @Path("/applyFriend")
							 //@Produces(MediaType.APPLICATION_JSON)
							 //@Consumes(MediaType.APPLICATION_OCTET_STREAM)
							 @Consumes(MediaType.APPLICATION_JSON)
							 @Produces("text/html")
						     public String applyFriend(Posto posto){
								 int uid1 = posto.getPid();
								 int uid2 = posto.getBelong_rid();
							 	 if(appService.check(uid1, uid2)==1 && appService.getUserById(uid2)!=null &&uid1!=uid2 ){
									 Friend friend=new Friend() ;
									 friend.setUid1(posto.getPid());
									 friend.setUid2(posto.getBelong_rid());
									 friend.setState(0);
								   	appService.addFriend(friend);
					 
									return  "1";
								 }
							 	 return "-1";
						     }	
							 
							 @POST
						     @Path("/applyFriendByName")
							 //@Produces(MediaType.APPLICATION_JSON)
							 //@Consumes(MediaType.APPLICATION_OCTET_STREAM)
							 @Consumes(MediaType.APPLICATION_JSON)
							 @Produces("text/html")
						     public String applyFriendByName(Posto posto){
								 int uid1 = posto.getPid();
								 String name = posto.getName();
								 User user2 = appService.getUserByUsername(name);
								 
								 if (user2==null) return "-1";  
								 
								 int uid2 =user2.getId();
							 	 if(appService.check(uid1, uid2)==1 && appService.getUserById(uid2)!=null &&uid1!=uid2 ){
									 Friend friend=new Friend() ;
									 friend.setUid1(posto.getPid());
									 friend.setUid2(uid2);
									 friend.setState(0);
								   	appService.addFriend(friend);
					 
									return  "1";
								 }
							 	 return "-1";
						     }										
							 

								@POST
								@Path("/getFriendsById")
								@Produces(MediaType.APPLICATION_JSON)
								public List<User>  getFriendsById(Posto posto)
								{
								 
									 List<User> users= new ArrayList<User>();
									 
									List<Friend> Friends = appService.getFriendById(posto.getPid());
									for(int i = 0 ; i < Friends.size();i++){
										
										User user;
										int state = Friends.get(i).getState();
										if(state ==1){
												int uid1 = Friends.get(i).getUid1();
												int uid2 = Friends.get(i).getUid2();
								
												if(uid1==posto.getPid()){
													user = appService.getUserById(uid2);
												}
												else{
													user = appService.getUserById(uid1);
												}
									
												users.add(user);
										}

									}
									System.out.println(users.size()+"user");
										return users;
								}
								
								@POST
								@Path("/getAppliesById")
								@Produces(MediaType.APPLICATION_JSON)
								public List<User>  getApplysById(Posto posto)
								{
								 
									List<User> users= new ArrayList<User>();
									List<Friend> Friends = appService.getFriendById(posto.getPid());
									for(int i = 0 ; i < Friends.size();i++){
										
										User user;
										int state = Friends.get(i).getState();
										int id =  Friends.get(i).getUid2();
										if(id==posto.getPid()&&state ==0){
												int uid1 = Friends.get(i).getUid1();
												int uid2 = Friends.get(i).getUid2();
								
												if(uid2==posto.getPid()){
													user = appService.getUserById(uid1);
													users.add(user);
												}
									
											
										}

									}
									System.out.println(users.size()+"user");
										return users;
								}
								
	/*-------------------------------------------------------------------------------------------------------*/
	
	//user
		@GET
		@Path("/getUsers")
		@Produces(MediaType.APPLICATION_JSON)
		public List<User>  AllUsers()
		{
			System.out.println("getUsers");
			List<User> users = appService.getAllUsers();
			return users;
		}
	
	
	
	 @POST
     @Path("/addUser")
	 //@Produces(MediaType.APPLICATION_JSON)
	 //@Consumes(MediaType.APPLICATION_OCTET_STREAM)
	 @Consumes(MediaType.APPLICATION_JSON)
	 @Produces("text/html")
     public String addUser(User user){
		 
		 System.out.println("addUser");
	
		 if(appService.checkUsername(user.getUsername(),user.getPassword())==0){
			 	 
			 	 return  appService.addUser(user).toString();
		 }
		 else{
			 return "-1";
		 }
		 
     }
	 
	 @POST
     @Path("/checkUser")
	 //@Produces(MediaType.APPLICATION_JSON)
	 //@Consumes(MediaType.APPLICATION_OCTET_STREAM)
	 @Consumes(MediaType.APPLICATION_JSON)
	 @Produces("text/html")
     public String checkUser(User user){
		 

	 
		 if(appService.checkUser(user.getUsername(),user.getPassword())>0){
			 	 
			 	 return appService.checkUser(user.getUsername(),user.getPassword()).toString();
		 }
		 else{
			 return "-1";
		 }
		 
     }
	 
	
	 
	 
	 
	 @POST
     @Path("/addBook")
	 //@Produces(MediaType.APPLICATION_JSON)
	 //@Consumes(MediaType.APPLICATION_OCTET_STREAM)
	 @Consumes(MediaType.APPLICATION_JSON)
	 @Produces("text/html")
     public String addBook(Book book){
		 System.out.println("addBook");
		 appService.addBook(book);
		 return "success";
     }
	 
	 /*@POST
     @Path("/add")
      public String add(@FormParam("id") String id,
                      @FormParam("name") String name,
                      @FormParam("age") Integer age){
        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);
        map.put(id,student);
        return "success";
    }*/
	 
 
	 @POST
     @Path("/updateUser")
     public String updateUser(User user){
		 System.out.println("updateUser");
		 appService.updateUser(user);
		 return "success";
     }
 
	 
	 @GET
     @Path("/deleteUser/{id}")
     public String deleteUserById(@PathParam("id") int id){
		 System.out.println("deleteUser");
		 
			User user = appService.getUserById(id);
			appService.deleteUser(user);

        return "success";
     }
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	/* --------------*/
	 @POST
     @Path("/pic/")
     public String updateUser( String url){

			url.replace("\n", "");
			System.out.print("1111111");
			BASE64Decoder decoder = new sun.misc.BASE64Decoder(); 
			  try { 
			  byte[] bytes1 = decoder.decodeBuffer(url); 

			  ByteArrayInputStream bais = new ByteArrayInputStream(bytes1); 
			  BufferedImage bi1 =ImageIO.read(bais); 
			  File w2 = new File("D://QQ.bmp");//可以是jpg,png,gif格式 
			  ImageIO.write(bi1,"jpg", w2);//不管输出什么格式图片，此处不需改动 
			  } catch (IOException e) { 
			 e.printStackTrace();
			 }
			
			
		/*	String base64Img = url.replaceAll("data:image/jpeg;base64,", "");      
            BASE64Decoder decoder = new BASE64Decoder();      
            try {      
                // Base64解码      
                byte[] b = decoder.decodeBuffer(base64Img);      
                for (int i = 0; i < b.length; ++i) {      
                    if (b[i] < 0) {// 调整异常数据      
                        b[i] += 256;      
                    }      
                }      
                // 生成jpeg图片      
              String ret_fileName = new String(("QQ"+".jpg").getBytes("gb2312"), "ISO8859-1" ) ;     
                File file = new File("D://" + ret_fileName);    
                OutputStream out = new FileOutputStream(file);      
                out.write(b);      
                out.flush();      
                out.close();      
            } catch (Exception e) {      
                e.printStackTrace();      
            }      
           
			
			/*
	         byte[] photoimg = new BASE64Decoder().decodeBuffer( url);  
	            for (int i = 0; i < photoimg.length; ++i) {  
	                if (photoimg[i] < 0) {  
	                    // 调整异常数据  
	                    photoimg[i] += 256;  
	                }  
	            }  
			  ByteArrayInputStream bais = new ByteArrayInputStream(photoimg); 
			  BufferedImage bi1 =ImageIO.read(bais); 
			  File w2 = new File("D://QQ.jpg");//可以是jpg,png,gif格式 
			  ImageIO.write(bi1,"jpg", w2);//不管输出什么格式图片，此处不需改动 
		}*/ //catch (IOException e1) {
			// TODO Auto-generated catch block
		//	e1.printStackTrace();
		//} 
		 return "success";
     }


	 /* static BASE64Encoder encoder = new sun.misc.BASE64Encoder(); 
	  static BASE64Decoder decoder = new sun.misc.BASE64Decoder(); 

	  public static void main(String[] args) { 
	 System.out.println(getImageBinary());

	 base64StringToImage(getImageBinary());
	 }

	  static String getImageBinary(){ 
	  File f = new File("c://20090709442.jpg"); 
	  BufferedImage bi; 
	  try { 
	  bi = ImageIO.read(f); 
	  
	  ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
	  ImageIO.write(bi,"jpg", baos); 
	  byte[] bytes = baos.toByteArray(); 

	  return encoder.encodeBuffer(bytes).trim(); 
	  } catch (IOException e) { 
	 e.printStackTrace();
	 }
	  return null; 
	 }

	  static void base64StringToImage(String base64String){ 
	  try { 
	  byte[] bytes1 = decoder.decodeBuffer(base64String); 

	  ByteArrayInputStream bais = new ByteArrayInputStream(bytes1); 
	  BufferedImage bi1 =ImageIO.read(bais); 
	  File w2 = new File("c://QQ.bmp");//可以是jpg,png,gif格式 
	  ImageIO.write(bi1,"jpg", w2);//不管输出什么格式图片，此处不需改动 
	  } catch (IOException e) { 
	 e.printStackTrace();
	 }
	 }*/

	 
	
}