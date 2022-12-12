package category;

public class Book {
	private String id;
	private String title;
	private String quantity;
	private String price;
	private String category;
	Book(String id,String title,String quantity,String price,String category){
		this.title=title;
		this.quantity=quantity;
		this.price=price;
		this.id=id;
		this.category=category;
		
	}
	Book(String id,String title){
		this.id=id;
		this.title=title;
	}
	
	public String getId(){
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
