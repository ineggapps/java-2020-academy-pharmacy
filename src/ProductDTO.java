

public class ProductDTO {
	private int pnum;		//품목번호(기본키)
	private String pname;	//품목명
	private int price;	//단가
	private int stock;		//재고수량
	
	@Override
	public String toString() {
		return "[상품번호: " + pnum + " | 상품명: " + pname + " | 정가: " + price + "원 |  재고: " + stock + "개]";
	}

	
	public int getPnum() {
		return pnum;
	}
	public void setPnum(int pnum) {
		this.pnum = pnum;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	
	
	
}
