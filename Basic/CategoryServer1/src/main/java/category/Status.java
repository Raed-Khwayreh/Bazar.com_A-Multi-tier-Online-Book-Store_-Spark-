package category;

public class Status {
	OrderStatus orderStatus;
	Book book;
	Status(OrderStatus orderStatus,
	Book book){
		this.orderStatus=orderStatus;
		this.book=book;
	}
}
