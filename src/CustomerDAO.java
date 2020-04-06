
public interface CustomerDAO {
	public int checkPurchaseMask(CustomerDTO dto);
	public int insertCustomer(CustomerDTO dto);
	public int insertSale(CustomerDTO dto, int pnum, int qty);
	public int deleteSale(int snum);
	public CustomerDTO checkAvailability(String rrn);
	public CustomerDTO readCustomer(String rrn);
	public String checkDate();
}
