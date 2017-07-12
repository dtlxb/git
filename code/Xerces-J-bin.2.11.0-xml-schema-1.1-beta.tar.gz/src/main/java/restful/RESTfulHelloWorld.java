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
import model.Posto;
import model.Route;
import model.User;
import model.Photo;
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

	      	 // ByteArrayInputStream bais = new ByteArrayInputStream(photoimg); 

			 // BufferedImage bi1 =ImageIO.read(bais); 
    
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
	    		appService.addPosto(posto);
			//  ImageIO.write(bi1,"jpg", w2);//不管输出什么格式图片，此处不需改动 
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		 return "success";
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
     @Path("/addPicture")
	 //@Produces(MediaType.APPLICATION_JSON)
	 //@Consumes(MediaType.APPLICATION_OCTET_STREAM)
	 @Consumes(MediaType.APPLICATION_JSON)
	 @Produces("text/html")
     public String addPicture(Photo photo ){
		 
 
			try {
				String picurl = photo.getcontent();
				 byte[] photoimg = Base64.decodeBase64(picurl.replace("data:image/png;base64,",""));
				 picurl = picurl.replace("base64,","");
	            for (int i = 0; i < photoimg.length; ++i) {  
	                if (photoimg[i] < 0) {  
	                    // 调整异常数据  
	                    photoimg[i] += 256;  
	                }  
	            }

		      	 // ByteArrayInputStream bais = new ByteArrayInputStream(photoimg); 

				 // BufferedImage bi1 =ImageIO.read(bais); 
				  
	            File file = new File("D://bzbp/", photo.getname()+".png");  
	            if (!file.exists()) {  
	                file.createNewFile();  
	            }  
	            FileOutputStream out = new FileOutputStream(file);  
		            out.write(photoimg);  
		            out.flush();  
		            out.close();  
	 
				//  ImageIO.write(bi1,"jpg", w2);//不管输出什么格式图片，此处不需改动 
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
			 return "success";
     }
	
	

	 @POST
     @Path("/addPhoto")
	 //@Produces(MediaType.APPLICATION_JSON)
	 //@Consumes(MediaType.APPLICATION_OCTET_STREAM)
	 @Consumes(MediaType.APPLICATION_JSON)
	 @Produces("text/html")
     public String addPicture(Posto posto ){
		 
 
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

		      	 // ByteArrayInputStream bais = new ByteArrayInputStream(photoimg); 

				 // BufferedImage bi1 =ImageIO.read(bais); 
	    
	            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");//获取当前时间，进一步转化为字符串
 
	          //  String str = format.format(date);
	          //  posto.setPath_server("D://bzbp/"+ posto.getUsername()+"_"+date+".png");
	            File file = new File("D://bzbp/", posto.getUsername()+"_"+".png");  
	            if (!file.exists()) {  
	                file.createNewFile();  
	            }  
	            FileOutputStream out = new FileOutputStream(file);  
		            out.write(photoimg);  
		            out.flush();  
		            out.close();  
	 
				//  ImageIO.write(bi1,"jpg", w2);//不管输出什么格式图片，此处不需改动 
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
			 return "success";
     }
	
		@POST
		@Path("/getPostosBy")
		@Produces(MediaType.APPLICATION_JSON)
		public List<Posto>  getPostosBy(Posto posto)
		{

			List<Posto> Postos = appService.getAllPostos();
			for(int i=0; i < Postos.size();i++){
				 BASE64Encoder encoder = new sun.misc.BASE64Encoder(); 
				 
				  File f = new File(Postos.get(i).getPath_server());
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
	
	
		/*--------------------------------------------------------------------------------------*/
	//route
		
		 @POST
	     @Path("/addRoute")
		 //@Produces(MediaType.APPLICATION_JSON)
		 //@Consumes(MediaType.APPLICATION_OCTET_STREAM)
		 @Consumes(MediaType.APPLICATION_JSON)
		 @Produces("text/html")
	     public Integer addRoute(User user){
			 String username = user.getUsername();
			 Route route = new Route();
			 route.setName(username);
		
			 System.out.println("addUser");
 
			 	return  appService.addRoute(route);
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
			 	 appService.addUser(user);
			 	 return "success";
		 }
		 else{
			 return "fail";
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
			 	 
			 	 return "success";
		 }
		 else{
			 return "fail";
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