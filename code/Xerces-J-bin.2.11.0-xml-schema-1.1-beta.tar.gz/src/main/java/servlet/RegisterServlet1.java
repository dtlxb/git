package servlet;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
public class RegisterServlet1 extends HttpServlet  {

	
	    public void doGet(HttpServletRequest request, HttpServletResponse response)  
	            throws ServletException, IOException {  
	        request.setCharacterEncoding("utf-8");  
	        response.setCharacterEncoding("utf-8");  
	        response.setContentType("text/html");  
	        String photo = request.getParameter("photo");  
	        String name = request.getParameter("name");  
	  
	        try {  
	  
	            // 对base64数据进行解码 生成 字节数组，不能直接用Base64.decode（）；进行解密  
	            byte[] photoimg = new BASE64Decoder().decodeBuffer(photo);  
	            for (int i = 0; i < photoimg.length; ++i) {  
	                if (photoimg[i] < 0) {  
	                    // 调整异常数据  
	                    photoimg[i] += 256;  
	                }  
	            }  
	  
	            // byte[] photoimg = Base64.decode(photo);//此处不能用Base64.decode（）方法解密，我调试时用此方法每次解密出的数据都比原数据大  所以用上面的函数进行解密，在网上直接拷贝的，花了好几个小时才找到这个错误（菜鸟不容易啊）  
	            System.out.println("图片的大小：" + photoimg.length);  
	            File file = new File("D:", "decode.png");  
	            File filename = new File("D:\\name.txt");  
	            if (!filename.exists()) {  
	                file.createNewFile();  
	            }  
	            if (!file.exists()) {  
	                file.createNewFile();  
	            }  
	            FileOutputStream out = new FileOutputStream(file);  
	            FileOutputStream out1 = new FileOutputStream(filename);  
	            out1.write(name.getBytes());  
	            out.write(photoimg);  
	            out.flush();  
	            out.close();  
	            out1.flush();  
	            out1.close();  
	        } catch (Exception e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        } 
   }
}  
