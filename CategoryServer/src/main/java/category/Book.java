package category;

public class Book {
	private String id;
	private String title;
	private String quantity;
	private String price;
	private String category;
	Book(String title,String quantity,String price){
		this.title=title;
		this.quantity=quantity;
		this.price=price;
	}
	Book(String id,String title){
		this.id=id;
		this.title=title;
	}
	String getId(){
		return id;
	}
	String getTitle(){
		return title;
	}
	String getQuantity(){
		return quantity;
	}
	String getPrice(){
		return price;
	}
	String getCategory(){
		return category;
	}
}
