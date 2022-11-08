package orderServer;
 
import static spark.Spark.*;

public class OrderServer {
	public static void main(String[] args) {
		port(9999);
		get("/purchase/:id", (request, response) -> {
			response.redirect("http://192.168.1.14:5555/infoToPurchase/"+request.params(":id"));
			return null;
		});
	}

}
