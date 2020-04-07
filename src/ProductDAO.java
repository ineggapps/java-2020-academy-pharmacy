
import java.util.List;

public interface ProductDAO {
	public int insertProduct(InputDTO dto); //제품추가-입고등록
	public int updateProduct(InputDTO dto); //제품수정-제품수정
	public int deleteProduct(int inum); //제품삭제-제품삭제
	public List<ProductDTO> listStock();//남은재고안내
	//public Map<String, Integer> listStock(); 
	
	
	//public ProductDTO readProduct(String rrn); //주민번호별 판매리스트 - 이름 !
	//public List<ProductDTO> listProduct(Map<String, Object> map);//손님별 판매현황
	public List<SaleSumDTO> listCustomer(String rrn); //손님별 판매현황-com>score3>ScoreDAO
	public List<SaleSumDTO> listProduct(); //총 판매리스트
	
	//처방
	public List<ProductDTO> searchKeyword(String keywrod); //처방
	public int insertSale(int pnum, int qty);

	//진단기능
	//제품삭제.
	
}

