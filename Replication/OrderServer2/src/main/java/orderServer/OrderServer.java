package orderServer;

import static spark.Spark.*;

public class OrderServer {
	public static void main(String[] args) {
		port(8888);
		get("/purchase/:id", (request, response) -> {
			response.redirect("http://192.168.1.108:5555/infoToPurchase/"+request.params(":id"));
			return null;
		});
	}

}
