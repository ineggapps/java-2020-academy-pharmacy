
import java.util.List;

public interface ProductDAO {
	public int insertProduct(InputDTO dto); //��ǰ�߰�-�԰���
	public int updateProduct(InputListDTO dto); //��ǰ����-��ǰ����
	public int deleteProduct(int inum); //��ǰ����-��ǰ����
	public InputDTO readInput(int inum   ); //������� (����ȣ ���)
	public List<InputListDTO> listStock();//�������ȳ�
	public List<ProductDTO> listProduct(); //��ǰ ����Ʈ
	
	
	public List<SaleSumDTO> listCustomer(String rrn); //�մԺ� �Ǹ���Ȳ-com>score3>ScoreDAO
	public List<SaleSumDTO> listSumProduct(); //�� �ǸŸ���Ʈ
	
	//ó��
	public List<ProductDTO> searchKeyword(String keyword); //ó��˻�
	public int insertSale(int pnum, int qty);
	//���� ���
	public List<String> getKeywords(); //���� Ű���常 ��� (�ߺ�����)
	public List<ProductKeywordDTO> listByKeyword(String keyword); //���� ��ϵ� ���
	//���� �߰�
	public int insertKeyword(int pnum, String keyword);
	//���� ����
	public int deleteKeywordProduct(int pnum, String keyword);
}

