package frontEnd;

import static spark.Spark.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
class Book{
	String id;
	 String title;
	 String quantity;
	 String price;
	String category;
	Book(String title,String quantity,String price){
		this.title=title;
		this.quantity=quantity;
		this.price=price;
	}
	Book(String id,String title){
		this.id=id;
		this.title=title;
	}
	Book(String id,String title,String quantity,String price,String category){
	this.id=id;
		this.title=title;
		this.quantity=quantity;
		this.price=price;
		this.category=category;
	}
}
public class Frontend {
		
static Book cache[]=new Book[5];
	
static boolean to_order=true;

static boolean to_cat=true;


    static public void initial() {
    	for(int i=0;i<5;i++)cache[i]=new Book("","","","","");
    }
	public static void putBook(Book b) {
	
	for(int i=4;i>0;i--)
		cache[i]=cache[i-1];
	
	cache[0]=b;	
	}
	
	public static Book getBookByID(String id) {
		
		for(int i =0 ; i<cache.length ; i++) {
			if(cache[i].id.equals(id)) {
				return new Book(cache[i].title,cache[i].quantity,cache[i].price);
			}
			}
		return null;
	}
	
	public static String getBookBYCat(String cat) {
		
		String books="";
    

		for(Book b:cache) {
			if(books.length()!=0)books+="/";
			if(b.category.equals(cat)) {books+=b.id+","+b.title;
		
			}}
			
		
		return books;
		
	}
	
	public static void main(String[] args) {	
		initial();
		port(7777);
		
		get("/info/:id", (req, res) -> {
	
			
			 res.type("application/json");
			 
			 Book bookCheck = getBookByID(req.params(":id"));
			 if(bookCheck!=null) {
				 System.out.println("From Cache");
				 if(bookCheck.quantity.equals("0")&&bookCheck.title.equals("")) { 
					 return  new Gson().toJson("Book not exist");
					 }else  return  new Gson().toJson(bookCheck);
				
			 }
			 else {
				 
					String port="";
					if(to_cat)port="5555";
					else port="6666";
					to_cat=!to_cat;
					
			 System.out.println("From Catalouge");
			 
			 URL url = new URL("http://192.168.1.108:"+port+"/info/"+req.params(":id"));
			 
		     System.out.println(url);
		     
			 HttpURLConnection con = (HttpURLConnection) url.openConnection();
			 con.setRequestMethod("GET");
			 
			
			 BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					String content ="";
					while ((inputLine = in.readLine()) != null) {
					    content+=inputLine;
					}
					in.close();
					
					System.out.println(content);
						String s[]=content.split(",");
						if(!content.equals("Book not exist")) {
							
						Book book_cash = new Book(s[0],s[1],s[2],s[3],s[4]);
						Book book_show = new Book(s[1],s[2],s[3]);
						
						 putBook(book_cash);
						 return  new Gson().toJson(book_show);
					}
					else {
						Book book = new Book(req.params(":id"),"","0","","");
						putBook(book);	
						return  new Gson().toJson("Book not exist");

						
					}
		
		 }});
		
		
		
	
		
			
	
		post("/info", (request, response) -> {

			response.type("application/json");
			Book bookID = new Gson().fromJson(request.body(), Book.class);
			
			Book bookCheck = getBookByID(request.params(":id"));
			
			if(bookCheck!=null) {
				 if(bookCheck.quantity.equals("0")&&bookCheck.title.equals("")) { 
					 return  new Gson().toJson("Book not exist");
					 }else  return  new Gson().toJson(bookCheck);
				
			 }
			 else {
					String port="";
					if(to_cat)port="5555";
					else port="6666";
					to_cat=!to_cat;
					
			 System.out.println("From Catalouge");
			 
			 URL url = new URL("http://192.168.1.108:"+port+"/info/"+bookID.id);
			 
		     System.out.println(url);
		     
			 HttpURLConnection con = (HttpURLConnection) url.openConnection();
			 con.setRequestMethod("GET");
			 
			
			 BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					String content ="";
					while ((inputLine = in.readLine()) != null) {
					    content+=inputLine;
					}
					in.close();
					
					System.out.println(content);
						String s[]=content.split(",");
						if(!content.equals("Book not exist")) {
							Book book_cash = new Book(s[0],s[1],s[2],s[3],s[4]);
							Book book_show = new Book(s[0],s[1]);
							
							 putBook(book_cash);
							 return  new Gson().toJson(book_show);
					}
					else {
						Book book = new Book(request.params(":id"),"","0","","");
						putBook(book);	
						return  new Gson().toJson("Book not exist");

					}
		
		 }
		});
		
		
		
		get("/search/:cateName", (request, response) -> {
			
			
			response.type("application/json");
			 
			 if(!getBookBYCat(request.params(":cateName")).equals("")) {
				 String Sbooks[]=getBookBYCat(request.params(":cateName")).split("/");
				
				 Book book_show[]=new Book[Sbooks.length];
				 
				 for(int i=0;i<Sbooks.length;i++) {
					 String split[]=Sbooks[i].split(",");
					 
						 book_show[i] = new Book(split[0],split[1]);
						
						
						 
					 
				 }
				 
				 return new Gson().toJson(book_show);
			 }
			 
			 else {
				 
					String port="";
					if(to_cat)port="5555";
					else port="6666";
					to_cat=!to_cat;
			 System.out.println("From Catalouge");
			
			 URL url = new URL("http://192.168.1.108:"+port+"/search/"+request.params(":cateName").replace(" ","%20"));
			 
		     System.out.println(url);
		     
			 HttpURLConnection con = (HttpURLConnection) url.openConnection();
			 con.setRequestMethod("GET");
			 
			
			 BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					String content ="";
					while ((inputLine = in.readLine()) != null) {
					    content+=inputLine;
					}
					in.close();
					
					System.out.println(content);
					if(!content.equals("Book not exist")) {
						
						 String Sbooks[]=content.split("/");
						 Book books_cash[]=new Book[Sbooks.length];
						 Book books_show[]=new Book[Sbooks.length];
					
						 for(int i=0;i<books_show.length;i++) {
							 String split[]=Sbooks[i].split(",");
							 books_show[i]=new Book(split[0],split[1]);
							 books_cash[i]=new Book(split[0],split[1],split[2],split[3],split[4]);
							 putBook(books_cash[i]);
						 }
						 
						 return new Gson().toJson(books_show);
					}
					else {
						return  new Gson().toJson("Book not exist");
					}
		
		 }});
		
		post("/search", (request, response) -> {
			response.type("application/json");
			Book bookCategory = new Gson().fromJson(request.body(), Book.class);
		
			 
			 if(!getBookBYCat(bookCategory.category).equals("")) {
				 String Sbooks[]=getBookBYCat(bookCategory.category).split("/");
				 Book book_cash[]=new Book[Sbooks.length];
				 Book book_show[]=new Book[Sbooks.length];
				 
				 for(int i=0;i<book_cash.length;i++) {
					 String split[]=Sbooks[i].split(",");
					  book_cash[i] = new Book(split[0],split[1],split[2],split[3],split[4]);
						 book_show[i] = new Book(split[0],split[1]);
						
						 putBook(book_cash[i]);
						 
					 
				 }
				 
				 return new Gson().toJson(book_show);
			 }
			 
			 else {
				 
				
			
			 System.out.println("From Catalouge");
				String port="";
				if(to_cat)port="5555";
				else port="6666";
				to_cat=!to_cat;
			 URL url = new URL("http://192.168.1.108:"+port+"/search/"+bookCategory.category.replace(" ","%20"));
			 
		     System.out.println(url);
		     
			 HttpURLConnection con = (HttpURLConnection) url.openConnection();
			 con.setRequestMethod("GET");
			 
			
			 BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					String content ="";
					while ((inputLine = in.readLine()) != null) {
					    content+=inputLine;
					}
					in.close();
					
					System.out.println(content);
					if(!content.equals("Book not exist")) {
						
						 String Sbooks[]=content.split("/");
						 Book book_cash[]=new Book[Sbooks.length];
						 Book book_show[]=new Book[Sbooks.length];
						 
						 for(int i=0;i<book_cash.length;i++) {
							 String split[]=Sbooks[i].split(",");
							  book_cash[i] = new Book(split[0],split[1],split[2],split[3],split[4]);
								 book_show[i] = new Book(split[0],split[1]);
								
								 putBook(book_cash[i]);
								 
							 
						 }
						 
						 return new Gson().toJson(book_show);
					}
					else {
						return  new Gson().toJson("Book not exist");
					}
		
		 }
		});
		post("purchase/:id", (request, response) -> {
			
			String port;
			if(to_order)port="9999";
			else port="8888";
			to_order=!to_order;
			
			response.redirect("http://192.168.1.108:"+port+"/purchase/"+request.params(":id"));
			return null;
		});
		post("/purchase", (request, response) -> {
			
			String port;
			if(to_order)port="9999";
			else port="8888";
			to_order=!to_order;
			
			Book bookID = new Gson().fromJson(request.body(), Book.class);
			response.redirect("http://192.168.1.108:"+port+"/purchase/"+bookID.id +"");
			return null;
		});
		
	}

}