
public class CustomerDTO {
	private int cNum; 
	private String cName; 
	private String rrn;
	
	/**
	 * 
	 */
	public CustomerDTO() {
	}
	/**
	 * @param cNum
	 * @param cName
	 * @param rrn
	 */
	public CustomerDTO(int cNum, String cName, String rrn) {
		this.cNum = cNum;
		this.cName = cName;
		this.rrn = rrn;
	}
	public int getcNum() {
		return cNum;
	}
	public String getcName() {
		return cName;
	}
	public String getRrn() {
		return rrn;
	}
	public void setcNum(int cNum) {
		this.cNum = cNum;
	}
	public void setcName(String cName) {
		this.cName = cName;
	}
	public void setRrn(String rrn) {
		this.rrn = rrn;
	} 
	
}
