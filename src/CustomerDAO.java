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
	
	
	// ó��
	public List<ProductDTO> searchKeyword(String keyword); // ó��˻�
	public List<String> getKeywords(); // ���� Ű���常 ��� (�ߺ�����)
	public List<ProductKeywordDTO> listByKeyword(String keyword); // ���� ��ϵ� ���
}
