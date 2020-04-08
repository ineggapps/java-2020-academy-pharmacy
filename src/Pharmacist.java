import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Pharmacist {
	public static final String KEY_ID = "id";
	public static final String KEY_PW = "pw";
	public static final String ADMIN_ID = "admin";
	public static final String ADMIN_PW = "1234";
	private Scanner sc = new Scanner(System.in);
	private ProductDAO dao = new ProductDAOImpl();
	private boolean loggedIn = false;
	private Map<String, String> administrator = new HashMap<String, String>();
	/*
	 * private PharmacistDAO dao = new PharmacistDAOImpl(); > �ñ��� ��. -
	 * PharmacistDAO�� �������̽��� �����ϰ�, �װ��� �����Ѱ��� PharmacistDAOImpl�ε�, - private
	 * PharmacistDAO dao = new PharmacistDAOImpl(); �̷��� �ν��Ͻ�ȭ ����?
	 */

	public Pharmacist() {
		administrator.put(KEY_ID, ADMIN_ID);
		administrator.put(KEY_PW, ADMIN_PW);
	}

	public void pharmacistManage() {
		int ch;
		try {
			while (true) {
				System.out.println("\n ��� [������ ���]");
				String id;
				String pwd;
				if (!loggedIn) {
					do {
						System.out.println("BUT.... �α����� �ʿ��մϴ�.");
						System.out.print("���̵�:");
						id = sc.next();
						System.out.print("��й�ȣ:");
						pwd = sc.next();
						if (id.equals(administrator.get(KEY_ID)) && pwd.equals(administrator.get(KEY_PW))) {
							System.out.println("�����ڷ� �α����Ͽ����ϴ�.");
							loggedIn = true;
						} else {
							System.out.println("���̵� ��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
							return;
						}
					} while (!loggedIn);// �α����� ���� ���� ��쿡 ��� ��ȸ
				}
				do {
					System.out.print("1.������  2. �Ǹ���Ȳ  3.ó��   4.�α׾ƿ� =>");
					ch = sc.nextInt();
				} while (ch < 1 || ch > 4);
				if (ch == 4) {
					loggedIn = false;
					break;
				}
				switch (ch) {
				case 1:
					inventory();
					break;
				case 2:
					sales();
					break;
				case 3:
					prescription();
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void inventory() {
		int ch;
		while (true) {
			do {
				System.out.println("1.��ǰ�߰� 2.��ǰ���� 3.��ǰ���� 4.�԰��� 5.�԰���� 6.�԰���� 7.����Ʈ 8.����=>");
				ch = sc.nextInt();
			} while (ch < 1 || ch > 8);
			if (ch == 8)
				break;
			switch (ch) {
			case 1:
				insert();
				break;
			case 2:
				update();
				break;
			case 3:
				delete();
				break;
			case 4:
				input();
				break;
			case 5:
				updateInput();
				break;
			case 6:
				deleteInput();
				break;
			case 7:
				sales();
				break;
			}
		}
	}


	// ��ǰ�߰�
	public void insert() {
		System.out.println("\n ��ǰ�߰�...");
		ProductDTO dto = new ProductDTO();
		try {
			System.out.print("ǰ�� ��ȣ? ");
			dto.setPnum(sc.nextInt());
			System.out.print("ǰ�� ��? ");
			dto.setPname(sc.next());
			System.out.print("�ǸŰ� ? ");
			dto.setPrice(sc.nextInt());

			int result = dao.insertProduct(dto);
			System.out.println(result + "�� ����� �Ϸ� �Ǿ����ϴ�.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	// ��ǰ���� 
	 public void update() {
	  System.out.println("\n ��ǰ����...");
	  int pnum;
	  ProductDTO dto = new ProductDTO();
	  try {
	   
	   System.out.print(" ������ ǰ�� ��ȣ? ");
	   pnum=sc.nextInt();
	   
	   dto = dao.readProduct(pnum);
	   
	   if(dto==null) {
	    System.out.println("��ϵ� ���̵� �����ϴ�.\n");
	    return;
	   }
	   
	   System.out.print(dto.getPnum()+"\t");
	   System.out.print(dto.getPname()+"\t");
	   System.out.print(dto.getPrice()+"\t");
	   System.out.print(dto.getStock()+"\n");
	   
	   
	   System.out.print("ǰ�� ��? ");
	   dto.setPname(sc.next());
	   System.out.print("�ǸŰ� ? ");
	   dto.setPrice(sc.nextInt());
	   
	   int result = dao.updateProduct(dto);
	   System.out.println(result + "�� ���� �Ϸ� �Ǿ����ϴ�.");    
	  } catch (Exception e) {
	   e.printStackTrace();
	  }
	  System.out.println();
	 }
	 
	 public void delete() {
	  System.out.println("\n��ǰ ����...");
	  int pnum;
	  ProductDTO dto = new ProductDTO();
	  
	  try {
	   
	 
	   System.out.print("������ ��ǰ��ȣ?");
	   pnum=sc.nextInt();
	   
	   dto = dao.readProduct(pnum);
	   
	   if(dto==null) {
	    System.out.println("��ϵ� ��ǰ�� �����ϴ�.\n");
	    return;
	   }
	   
	   System.out.print(dto.getPnum()+"\t");
	   System.out.print(dto.getPname()+"\t");
	   System.out.print(dto.getPrice()+"\t");
	   System.out.print(dto.getStock()+"\n");
	   
	   int result=dao.deleteProduct(pnum);
	   
	   if (result!=0)
	    System.out.println("��ǰ���� ����....");
	  } catch (Exception e) {
	  }
	  
	 }

//�԰�	
	public void input() {
		System.out.println("\n �԰� ���..."); // ����ũ�� �ռҵ��� �԰� ���

		InputDTO dto = new InputDTO();
		try {
			printProducts(dao.listProduct());
			System.out.print("��ǰ ��ȣ? ");
			dto.setPnum(sc.nextInt());
			System.out.print("�԰� ��¥? ");
			dto.setIdate(sc.next());
			System.out.println("���� ? ");
			dto.setIqty(sc.nextInt());

			int result = dao.insertInput(dto);
			if (result != 0)
				;
			System.out.println("�԰��ȣ " + result + "�� ����� �Ϸ� �Ǿ����ϴ�.");
			pharmacistManage();

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println();

	}

	// ��ǰ����

	public void updateInput() {
		System.out.println("��ǰ ���� ... ");

		List<InputListDTO> list = dao.listStock();
		for (InputListDTO Ip : list) {
			System.out.print("�԰��ȣ:  " + Ip.getInum() + "\t");
			System.out.print("��ǰ��ȣ:  " + Ip.getPnum() + "\t");
			System.out.print("��ǰ��:  " + Ip.getPname() + "\t");
			System.out.print("�԰���� : " + Ip.getIqty() + "\t");
			System.out.print("���:  " + Ip.getStock() + "\n");
		}
		try {
			InputListDTO dto = new InputListDTO();
			System.out.print("���� �� �԰��ȣ?");
			dto.setInum(sc.nextInt());
			InputDTO input = dao.readInput(dto.getInum());
			if (input == null) {
				System.out.println("�߸� �Է��ϼ̽��ϴ�.");
				return;
			}
			System.out.print("�԰� ��¥?");
			dto.setIdate(sc.next());
			System.out.print("����?");
			int beforeIqty = input.getIqty();
			dto.setIqty(sc.nextInt());
			if (beforeIqty >= dto.getIqty()) {
				System.out.println("�������� ���� �������� ũ�� �Է��ؾ� �մϴ�.");
				return;
			}
			int result = dao.updateInput(dto);
			if (result != 0) {
				System.out.println("���� �Ϸ�.");
			} else {
				System.out.println("���� ����");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ��ǰ����

	public void deleteInput() {

		List<InputListDTO> list = dao.listStock();
		for (InputListDTO Ip : list) {
			System.out.print("�԰��ȣ:  " + Ip.getInum() + "\t");
			System.out.print("��ǰ��ȣ:  " + Ip.getPnum() + "\t");
			System.out.print("��ǰ��:  " + Ip.getPname() + "\t");
			System.out.print("�԰���� : " + Ip.getIqty() + "\t");
			System.out.print("���:  " + Ip.getStock() + "\n");
		}
		int inum;
		try {
			System.out.print("������ �԰� ��ȣ?");
			inum = sc.nextInt();
			int result = dao.deleteProduct(inum);
			if (result != 0) {
				System.out.println(result + "�� ��ǰ�� ���� �߽��ϴ�.");
			} else {
				System.out.println("���� ����.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// �����Ʈ
	public void liststock() {
		System.out.println("��ǰ ��� ����Ʈ");
		List<InputListDTO> list = dao.listStock();
		for (InputListDTO dto : list) {
			System.out.print("�԰��ȣ:  " + dto.getInum() + "\t");
			System.out.print("��ǰ��ȣ:  " + dto.getPnum() + "\t");
			System.out.print("��ǰ��:  " + dto.getPname() + "\t");
			System.out.print("���:  " + dto.getStock() + "\n");
		}
	}

	public void sales() {
		int ch;
		while (true) {
			System.out.println("\n ���");
			do {
				System.out.println("1.�մԺ� ����ũ �Ǹ���Ȳ 2.��ü �ǸŸ���Ʈ  3.����=>");
				ch = sc.nextInt();
			} while (ch < 1 || ch > 3);
			if (ch == 3)
				break;
			switch (ch) {
			case 1:
				customerlist();
				break;
			case 2:
				productlist();
				break;

			}
		}
	}

//�ֹι�ȣ ��ȿ���˻�	
	public boolean isValidRRN(String rrn) {
		int year, month, day;
		int endDayOfMonth;
		try {
			// 1. ���ڸ� 6�ڸ� + ���ڸ� 7�ڸ� = 13 Ȥ�� ������ �����Ͽ� 14�ڸ����� �˻�
			if (rrn.length() < 13 || rrn.length() > 14) {
				System.out.println("�ֹε�Ϲ�ȣ �ڸ��� ����");
				return false;
			}
			year = Integer.parseInt(rrn.substring(0, 2));
			month = Integer.parseInt(rrn.substring(2, 4));
			day = Integer.parseInt(rrn.substring(4, 6));
			if (month < 1 || month > 12) {
				System.out.println("�� �Է¿��� : " + month+"�� �� �Է��߽��ϴ�.");
				return false;
			}
			Calendar cal = Calendar.getInstance();
			cal.set(year, month - 1, 1);
			endDayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			if (day < 1 || day > endDayOfMonth) {
				System.out.println("�� �Է¿��� : " + day + "�� �� �Է��߽��ϴ�.");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
// �մԺ� ����ũ�ǸŸ���Ʈ
	public void customerlist() {
		System.out.println("\n�մԺ� ����ũ �ǸŸ���Ʈ...");
		String rrn;

		while (true) {
			System.out.println("�˻��� �մ� �ֹε�Ϲ�ȣ(�� �޴��� ���ư��� : n)?");
			rrn = sc.next();
			if(rrn.equalsIgnoreCase("n")) {  //�ֹι�ȣ ��ȿ���˻� �޼ҵ� ȣ��
				return;
			}
			if (isValidRRN(rrn)==false) {
				System.out.println("�ٽ� �Է����ּ���^^");
			}
			else{
				break;
			}
		}

		List<SaleSumDTO> list = dao.listCustomer(rrn);
		System.out.println("�̸�\t �Ǹų�¥\t\t�Ǹ�ǰ��\t\t���ż���");

		for (SaleSumDTO dto : list) {
			System.out.print(dto.getcName() + "\t");
			System.out.print(dto.getSdate() + "\t");
			System.out.print(dto.getpName() + "\t");
			System.out.print(dto.getsQty() + "\n");
		}
	}

//��ü �ǸŸ���Ʈ
	private void productlist() {
		System.out.println("\nǰ�� ��ü �ǸŸ���Ʈ...");
		List<SaleSumDTO> list = dao.listSumProduct();
		System.out.println("ǰ���̸�\t ǰ���ȣ\t������\t�Ǹų�¥");
		for (SaleSumDTO dto : list) {
			System.out.print(dto.getpName() + "\t");
			System.out.print(dto.getpNum() + "\t");
			System.out.print(dto.getsQty() + "\t");
			System.out.print(dto.getSdate() + "\n");
		}
	}

//ó��

	public void prescription() {
		int ch;
		try {
			while (true) {
				do {
					ch = sc.nextInt();
				} while (ch < 1 || ch > 3);
				switch (ch) {
				case 1:
					prescribe();
					break;
				case 2:
					managePrescription();
					break;
				}
				if (ch == 3) {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void prescribe() {
		int result; // ���� ó�����
		int choice, qty; // ���ù�ȣ, ����
		String keyword; // �˻���
		List<ProductDTO> list = null;
		try {
			System.out.print("\n ���� �Է� > ");
			keyword = sc.next().trim();
			if (keyword == null || keyword.length() == 0) {
				System.out.println("Ű���带 �Է��ϼ���...");
				return;
			}
			list = dao.searchKeyword(keyword);
			if (list == null || list.size() == 0) {
				System.out.println("�˻� ����� �����ϴ�.");
				return;
			}
			for (int i = 0; i < list.size(); i++) {
				// ���� ��ǰ ���
				System.out.println(i + 1 + "��. " + list.get(i).toString());
			}
			System.out.print("\n ó���� �� ��� ��ȣ�� ���� > ");
			choice = sc.nextInt();
			if (choice < 1 || choice > list.size()) {
				System.out.println("�ùٸ� ��ȣ�� �Է��ϼ���.");
				return;
			}
			System.out.print("���� �Է� ? ");
			qty = sc.nextInt();
			result = dao.insertSale(list.get(choice - 1).getPnum(), qty);
			if (result != 0) {
				System.out.println("ó��Ǿ����ϴ�. �����ޣ��ޣ���");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void managePrescription() {
		int ch;
		int result;
		String keyword;
		int pnum;
		System.out.print("1.������ 2.�����߰� 3.������� 4.�ڷΰ���> ");
		ch = sc.nextInt();
		if (ch == 4) {
			return;
		} else if (ch < 1 || ch > 4) {
			System.out.println("�ùٸ� ��ȣ�� �Է��� �ּ���...");
			return;
		}
		List<String> keywords = dao.getKeywords();
		List<ProductKeywordDTO> list = null;
		printSymtoms(keywords);
		switch (ch) {
		case 1:// ���� ���
			if (keywords == null || keywords.size() == 0) {
				System.out.println("��ϵ� ���� ����� �����ϴ�. ���� �߰��� �̿��Ͽ� ����� �ּ���...");
				return;
			}
			System.out.println("��ϵ� ���� ����� ������ �����ϴ�...");
			while (true) {
				System.out.print("Ű���� ��ȣ �Է� (������: 0)> ");
				ch = sc.nextInt();
				if (ch == 0) {
					break;
				}
				keyword = keywords.get(ch - 1);
				list = dao.listByKeyword(keyword);
				System.out.println(keyword + " �˻� ��� ..." + "(�� " + list.size() + "��)");
				printSymtomObjects(list);
			}
			break;
		case 2:// ���� �߰�
			System.out.println("������ ��ϵ� ���� �׸��� ������ �����ϴ�. ");
			System.out.print("�߰��� ����(�ְ���) ? ");
			keyword = sc.next();
			List<ProductDTO> target = dao.listProduct();
			while (true) {
				if (target == null || target.size() == 0) {
					break;
				}
				printProducts(target);

				System.out.print("�߰��� ��ǰ��ȣ (������: 0)? ");
				pnum = sc.nextInt();
				if (pnum == 0) {
					break;
				}
				result = dao.insertKeyword(pnum, keyword);
				if (result != 0) {
					for (int i = 0; i < target.size(); i++) {
						if (target.get(i).getPnum() == pnum) {
							target.remove(i);
						}
					}
					System.out.println("������ ���������� ��ϵǾ����ϴ�.");
				} else {
					System.out.println("�̹� ��ϵǾ����ϴٸ�...?");
				}
			}
			break;
		case 3:// ���� ����
			System.out.print("������ ���� (�ְ���) ? ");
			keyword = sc.next();
			while (true) {
				list = dao.listByKeyword(keyword);
				if (list == null || list.size() == 0) {
					break;
				}
				printSymtomObjects(list);
				System.out.print("������ ��ǰ��ȣ (������: 0) ? ");
				pnum = sc.nextInt();
				if (pnum == 0) {
					break;
				}
				result = dao.deleteKeywordProduct(pnum, keyword);
				if (result != 0) {
					System.out.println("������ ���������� �����Ǿ����ϴ�.");
				} else {
					System.out.println("��ǰ��ȣ�� ��Ȯ�ϰ� �Է��� �ּ���.");
				}
			}

			break;
		}
	}

	public void printSymtoms(List<String> keywords) {
		if (keywords == null || keywords.size() == 0) {
			return;
		}
		int index = 0;
		for (String k : keywords) {
			System.out.println(++index + ". " + k);
		}
	}

	public void printSymtomObjects(List<ProductKeywordDTO> list) {
		if (list == null || list.size() == 0) {
			return;
		}

		for (ProductKeywordDTO dto : list) {
			System.out.println(dto);
		}
	}

	public void printProducts(List<ProductDTO> list) {
		if (list == null || list.size() == 0) {
			return;
		}
		for (ProductDTO dto : list) {
			System.out.println(dto);
		}

	}
}
