package category;

import static spark.Spark.*;

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
		Book book;
		try {
			Connection connection = DriverManager.getConnection(jdURL);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement
					.executeQuery("Select * from categories where id = '" + bookID.getId() + "'");
			book = new Book(resultSet.getString("title"), resultSet.getString("quantity"),
					resultSet.getString("price"));
			return resultSet.wasNull() ? new Gson().toJson(new OrderStatus("Faild", "Book not exist"))  : new Gson().toJson(new Status(new OrderStatus("Faild", "Book not exist"),book));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "Problem";
	}

	////////////////////////////////// infoUrlArgument
	public static String infoUrlArgument(Request request, String jdURL) {
		Book book;
		try {
			Connection connection = DriverManager.getConnection(jdURL);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement
					.executeQuery("Select * from categories where id = '" + request.params(":id") + "'");
			book = new Book(resultSet.getString("title"), resultSet.getString("quantity"),
					resultSet.getString("price"));
			return resultSet.wasNull() ? new Gson().toJson(new OrderStatus("Faild", "Book not exist")) : new Gson().toJson(new Status(new OrderStatus("Success", "Book exist"),book));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "Problem";
	}

	////////////////////////////////// searchUrlArgument
	public static String searchUrlArgument(Request request, String jdURL) {
		try {
			Connection connection = DriverManager.getConnection(jdURL);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement
					.executeQuery("Select * from categories where category = '" + request.params(":cateName") + "'");
			ArrayList<Book> bookList = new ArrayList<Book>();
			while (resultSet.next()) {
				bookList.add(new Book(resultSet.getString("id"), resultSet.getString("title")));
			}
			return new Gson().toJson(bookList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "Problem";
	}

	////////////////////////////////// searchPassedArgument
	public static String searchPassedArgument(Request request, String jdURL) {
		Book bookCate = new Gson().fromJson(request.body(), Book.class);
		try {
			Connection connection = DriverManager.getConnection(jdURL);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement
					.executeQuery("Select * from categories where category= '" + bookCate.getCategory() + "'");
			ArrayList<Book> bookList = new ArrayList<Book>();
			while (resultSet.next()) {
				bookList.add(new Book(resultSet.getString("id"), resultSet.getString("title")));
			}
			return new Gson().toJson(bookList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "Problem";
	}

	////////////////////////////check if book exist ? decrement : faild;
	public static boolean check(Request request, String jdURL) {
		try {
			Connection connection = DriverManager.getConnection(jdURL);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement
					.executeQuery("Select * from categories where id = '" + request.params(":id") + "' and quantity >= 1");
			int i=0;
			while (resultSet.next()) {
				i++;
			}
			if(i>=1) {
				statement
				.execute("UPDATE `categories` SET `quantity`=`quantity`-1 WHERE id = "+request.params(":id")+"");
				return true;
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;

	}

	////////////////////////////////// main
	public static void main(String[] args) {
		String jdURL = "jdbc:sqlite:categories.db";
		port(5555);
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
		get("/infoToPurchase/:id", (request, response) -> {
			response.type("application/json");
			return check(request, jdURL) ? new Gson().toJson(new OrderStatus("Success" , "Purchase completed")):new Gson().toJson(new OrderStatus("Faild","Book not exist or sold"));
		});
	}

}
