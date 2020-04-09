import java.util.List;

public interface ProductDAO {
	public int insertProduct(ProductDTO dto); // 제품추가
	public int updateProduct(ProductDTO dto); // 제품수정
	public int deleteProduct(int pnum); // 제품삭제
	public ProductDTO readProduct(int pnum); // 제품리스트

	public int insertInput(InputDTO dto);// 입고등록
	public int updateInput(InputListDTO dto); // 입고수정
	public int deleteInput(int inum); // 제품삭제-제품삭제

	public InputDTO readInput(int inum); // 재고정보 (재고번호 기반)
	public List<InputListDTO> listStock();// 남은재고안내
	public List<ProductDTO> listProduct(); // 상품 리스트
	public List<SaleSumDTO> listCustomer(String rrn); // 손님별 판매현황-com>score3>ScoreDAO
	public List<SaleSumDTO> listSumProduct(); // 총 판매리스트

	// 처방
	public List<ProductDTO> searchKeyword(String keyword); // 처방검색

	// 증상 목록
	public List<String> getKeywords(); // 증상 키워드만 출력 (중복제거)
	public List<ProductKeywordDTO> listByKeyword(String keyword); // 증상 목록들 출력
	// 증상 추가
	public int insertKeyword(int pnum, String keyword);
	public List<ProductDTO> searchAvailableProduct(String keyword);
	// 증상 삭제
	public int deleteKeywordProduct(int pnum, String keyword);

}
