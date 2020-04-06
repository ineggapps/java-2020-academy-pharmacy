
public class SaleDTO {
	private int snum; // 판매번호
	private int pnum; // 품목번호
	private String sdate; // 구매일자
	private int sqty; // 구매수량
	private int cnum; // 구매자(회원번호)

	public int getSnum() {
		return snum;
	}

	public void setSnum(int snum) {
		this.snum = snum;
	}

	public int getPnum() {
		return pnum;
	}

	public void setPnum(int pnum) {
		this.pnum = pnum;
	}

	public String getSdate() {
		return sdate;
	}

	public void setSdate(String sdate) {
		this.sdate = sdate;
	}

	public int getSqty() {
		return sqty;
	}

	public void setSqty(int sqty) {
		this.sqty = sqty;
	}

	public int getCnum() {
		return cnum;
	}

	public void setCnum(int cnum) {
		this.cnum = cnum;
	}

}
