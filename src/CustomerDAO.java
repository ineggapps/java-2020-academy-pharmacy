import java.util.List;

public interface CustomerDAO {
	public int checkPurchaseMask(CustomerDTO dto);
	public int insertCustomer(CustomerDTO dto);
	public int insertSaleMask(CustomerDTO dto, int pnum, int qty);
	public int insertSaleItem(int pnum, int qty);
	public int deleteSale(int snum);
	public CustomerDTO checkAvailability(String rrn);
	public CustomerDTO readCustomer(String rrn);
	public String checkDate();
	
	////////
	public List<Integer> getMaskProductNumbers(String keyword, boolean isAvailable);
}
