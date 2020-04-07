
public class ProductKeywordDTO {
	private String keyword;
	private ProductDTO product;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public ProductDTO getProduct() {
		return product;
	}

	public void setProduct(ProductDTO product) {
		this.product = product;
	}

	public String toString() {
		return "[상품번호: " + product.getPnum() + " | 상품명: " + product.getPname() + " | 정가: " + product.getPrice()
				+ "원 |  재고: " + product.getStock() + "개]";
	}

}
