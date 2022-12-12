package category;

import static spark.Spark.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.google.gson.Gson;

import spark.Request;

public class CategoryServer {
	////////////////////////////////// infoPassedArgument
	public static String infoPassedArgument(Request request, String jdURL) {

		Book bookID = new Gson().fromJson(request.body(), Book.class);
		System.out.println("Info request for book of id : "+bookID.getId());
		Book book;

		try {
			Connection connection = DriverManager.getConnection(jdURL);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement
					.executeQuery("Select * from categories where id = '" +  bookID.getId() + "'");
			
			book = new Book(resultSet.getString("id"),resultSet.getString("title"), resultSet.getString("quantity"),
					resultSet.getString("price"),resultSet.getString("category"));
			
			
			
			boolean flag = false;
			if (resultSet.wasNull()) {
				flag = true;
			}
			connection.close();
			return flag ? "Book not exist"
					: book.getId()+","+book.getTitle()+","+book.getQuantity()+","+book.getPrice()+","+book.getCategory();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "Problem";
	}

	////////////////////////////////// infoUrlArgument
	public static String infoUrlArgument(Request request, String jdURL) {
		Book book;

		System.out.println("Info request for book of id : "+request.params(":id"));
		try {
			Connection connection = DriverManager.getConnection(jdURL);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement
					.executeQuery("Select * from categories where id = '" + request.params(":id") + "'");
			book = new Book(resultSet.getString("id"),resultSet.getString("title"), resultSet.getString("quantity"),
					resultSet.getString("price"),resultSet.getString("category"));
			System.out.println("from1");

			boolean flag = false;
			if (resultSet.wasNull()) {
				flag = true;
			}
			connection.close();
			return flag ? "Book not exist"
					: book.getId()+","+book.getTitle()+","+book.getQuantity()+","+book.getPrice()+","+book.getCategory();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "Problem";
	}

	////////////////////////////////// searchUrlArgument
	public static String searchUrlArgument(Request request, String jdURL) {
		
		System.out.println("Search request for category : "+request.params(":cateName"));
	
		try {
			String books="";
			Connection connection = DriverManager.getConnection(jdURL);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement
					.executeQuery("Select * from categories where category = '" + request.params(":cateName") + "'");
			
			while (resultSet.next()) {
 
				
				if(books.length()!=0)books+="/";
				books+= resultSet.getString("id")+","+resultSet.getString("title")+","+ resultSet.getString("quantity")+","+
						resultSet.getString("price")+","+resultSet.getString("category");
				
			}
		
			connection.close();
			return books;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "Problem";
	}

	////////////////////////////////// searchPassedArgument
	public static String searchPassedArgument(Request request, String jdURL) {
		Book bookCate = new Gson().fromJson(request.body(), Book.class);

		System.out.println("Search request for category : "+bookCate.getCategory());
		try {
			Connection connection = DriverManager.getConnection(jdURL);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement
					.executeQuery("Select * from categories where category= '" + bookCate.getCategory() + "'");
			ArrayList<Book> bookList = new ArrayList<Book>();
			while (resultSet.next()) {
				bookList.add(new Book(resultSet.getString("id"), resultSet.getString("title")));
			}

			connection.close();
			return new Gson().toJson(bookList);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "Problem";
	}

	//////////////////////////// check if book exist ? decrement : faild;
	public static String check(Request request, String jdURL) throws IOException {
        Book book;


		System.out.println("Purchase request for book of id : "+ request.params(":id"));
		try {
			Connection connection = DriverManager.getConnection(jdURL);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"Select * from categories where id = '" + request.params(":id") + "' and quantity >= 1");
			int i = 0;
			while (resultSet.next()) {
				i++;
			}
			if (i >= 1) {
				statement.execute(
						"UPDATE `categories` SET `quantity`=`quantity`-1 WHERE id = " + request.params(":id") + "");

				ResultSet result = statement.executeQuery(
						"Select * from categories where id = '" + request.params(":id") + "' and quantity >= 1");
				
				book = new Book(result.getString("id"),result.getString("title"), result.getString("quantity"),
						result.getString("price"),result.getString("category"));
				

				
				connection.close();
				
				
					 
					URL url = new URL("http://192.168.1.108:6666/update/"+request.params(":id"));
					 HttpURLConnection con = (HttpURLConnection) url.openConnection();
					 con.setRequestMethod("GET");
				     new BufferedReader(new InputStreamReader(con.getInputStream()));


					 
				

			  return  book.getId()+","+book.getTitle()+","+book.getQuantity()+","+book.getPrice()+","+book.getCategory();
           
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";

	}
	
	public static Object Update(Request request, String jdURL) throws SQLException {
		
		
		try {
			Connection connection = DriverManager.getConnection(jdURL);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"Select * from categories where id = '" + request.params(":id") + "' and quantity >= 1");
			int i = 0;
			while (resultSet.next()) {
				i++;
			}
			if (i >= 1) {
				statement.execute(
						"UPDATE `categories` SET `quantity`=`quantity`-1 WHERE id = " + request.params(":id") + "");

				
			
				connection.close();
				
			}} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return null;
	
		
		
		
	}


	////////////////////////////////// main
	public static void main(String[] args) {
		String jdURL = "jdbc:sqlite:categories.db";

		port(6666);
		post("/info", (request, response) -> {
			response.type("application/json");
			return infoPassedArgument(request, jdURL);
		});
		get("/info/:id", (request, response) -> {
			response.type("application/json");
			return infoUrlArgument(request, jdURL);
		});
		post("/search", (request, response) -> {
			response.type("application/json");
			return searchPassedArgument(request, jdURL);
		});
		get("/search/:cateName", (request, response) -> {
			response.type("application/json");
			return searchUrlArgument(request, jdURL);
		});
		
		get("/update/:id", (request, response) -> {
			response.type("application/json");
			return Update(request, jdURL);
		});
		
		get("/infoToPurchase/:id", (request, response) -> {
			response.type("application/json");
			return check(request, jdURL) != "" ? new Gson().toJson(new OrderStatus("Success", "Purchase completed"))
					: new Gson().toJson(new OrderStatus("Faild", "Book not exist or sold"));
		});
	}

	
}
