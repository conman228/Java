
public class item {
	int id;
	String quote;
	double price;
	
	public item(int id, String quote, double price) { //book object
		this.id = id;
		this.quote = quote;
		this.price = price;	
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getQuote() {
		return quote;
	}

	public void setQuote(String quote) {
		this.quote = quote;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
}
