import java.util.List;

public interface CustomerDAO {
	public int checkPurchaseMask(CustomerDTO dto);
	public int insertCustomer(CustomerDTO dto);
	public int insertSaleMask(CustomerDTO dto, int pnum, int qty);
	public int insertSaleItem(int pnum, int qty);
	public int deleteSale(int snum);
	public String checkAvailability(String rrn);
	public CustomerDTO readCustomer(String rrn);
	public String checkDate();
	
	
	// 처방
	public List<ProductDTO> searchKeyword(String keyword); // 처방검색
	public List<String> getKeywords(); // 증상 키워드만 출력 (중복제거)
	public List<ProductKeywordDTO> listByKeyword(String keyword); // 증상 목록들 출력
}
