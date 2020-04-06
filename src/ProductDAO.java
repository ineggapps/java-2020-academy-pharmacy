

import java.util.List;
import java.util.Map;

public interface ProductDAO {
	public int insertProduct(ProductDTO dto); //제품추가-입고등록
	public int updateProduct(ProductDTO dto); //제품수정-제품수정
	//public int deleteProduct(ProductDTO dto); //제품삭제-제품삭제
	public int deleteProduct(int pnum); //제품삭제 시 , 이름을 기준?
	public List<ProductDTO> listStock();//남은재고안내
	
	
	//public ProductDTO readProduct(String rrn); //주민번호별 판매리스트 - 이름 !
	//public List<ProductDTO> listProduct(Map<String, Object> map);//손님별 판매현황
	public Map<String, Integer> listCustomer(); //손님별 판매현황-com>score3>ScoreDAO
	public List<ProductDTO> listProduct(String pname); //품목별 판매리스트
	public List<ProductDTO> listProduct();  //전체 판매리스트 -총 판매실적
	
	//처방
	//진단기능
	//제품삭제. - 한개 

	
}
