package frontEnd;

import static spark.Spark.*;

import com.google.gson.Gson;

public class Frontend {
	class Book{
		String id;
		String category;
	}
	public static void main(String[] args) {	
		port(7777);
		get("/info/:id", (request, response) -> {
			response.redirect("http://192.168.1.3:5555/info/"+request.params(":id"));
			
			return null;
		});
		post("/info", (request, response) -> {
			Book bookID = new Gson().fromJson(request.body(), Book.class);
			response.redirect("http://192.168.1.3:5555/info/"+bookID.id +"");
			return null;
		});
		get("/search/:cateName", (request, response) -> {
			response.redirect("http://192.168.1.3:5555/search/"+request.params(":cateName"));

			return null;
		});
		post("/search", (request, response) -> {
			Book bookCategory = new Gson().fromJson(request.body(), Book.class);
			response.redirect("http://192.168.1.3:5555/search/"+bookCategory.category +"");
			return null;
		});
		put("purchase/:id", (request, response) -> {
			response.redirect("http://192.168.1.3:9999/purchase/"+request.params(":id"));
			return null;
		});
		put("/purchase", (request, response) -> {
			Book bookID = new Gson().fromJson(request.body(), Book.class);
			response.redirect("http://192.168.1.3:9999/purchase/"+bookID.id +"");
			return null;
		});
		
	}

}