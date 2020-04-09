import java.util.List;

public interface ProductDAO {
	public int insertProduct(ProductDTO dto); // ��ǰ�߰�
	public int updateProduct(ProductDTO dto); // ��ǰ����
	public int deleteProduct(int pnum); // ��ǰ����
	public ProductDTO readProduct(int pnum); // ��ǰ����Ʈ

	public int insertInput(InputDTO dto);// �԰���
	public int updateInput(InputListDTO dto); // �԰����
	public int deleteInput(int inum); // ��ǰ����-��ǰ����

	public InputDTO readInput(int inum); // ������� (����ȣ ���)
	public List<InputListDTO> listStock();// �������ȳ�
	public List<ProductDTO> listProduct(); // ��ǰ ����Ʈ
	public List<SaleSumDTO> listCustomer(String rrn); // �մԺ� �Ǹ���Ȳ-com>score3>ScoreDAO
	public List<SaleSumDTO> listSumProduct(); // �� �ǸŸ���Ʈ

	// ó��
	public List<ProductDTO> searchKeyword(String keyword); // ó��˻�

	// ���� ���
	public List<String> getKeywords(); // ���� Ű���常 ��� (�ߺ�����)
	public List<ProductKeywordDTO> listByKeyword(String keyword); // ���� ��ϵ� ���
	// ���� �߰�
	public int insertKeyword(int pnum, String keyword);
	public List<ProductDTO> searchAvailableProduct(String keyword);
	// ���� ����
	public int deleteKeywordProduct(int pnum, String keyword);

}
