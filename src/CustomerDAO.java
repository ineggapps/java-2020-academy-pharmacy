
public interface CustomerDAO {
	public int insertSale(int pnum, String date, int qty, int cnum);
	public int deleteSale(int snum);
	public CustomerDTO readCustomer(String rrn);
}
