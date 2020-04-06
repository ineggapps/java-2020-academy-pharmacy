
public interface CustomerDAO {
	public int checkPurchaseMask(CustomerDTO dto);
	public int insertCustomer(CustomerDTO dto);
	public int insertSale(int pnum, String date, int qty, int cnum);
	public int deleteSale(int snum);
	public CustomerDTO checkAvailability(String rrn);
	public CustomerDTO readCustomer(String rrn);
	public String checkDate();
}
