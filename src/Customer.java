import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.List;

/**/
public class Customer {
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	CustomerDAO dao = new CustomerDAOImpl();
	ProductDAO productDAO = new ProductDAOImpl();
	CustomerDTO dto = null;

	public void customer() {
		int ch;
		try {
			while (true) {
				System.out.println("\n[��]");
				System.out.println("��  �湮���ּż� �����մϴ�.������ �ʿ��ϽŰ���? ��");
				do {
					System.out.print("1.�ڷγ� ����ũ ���� | 2.�Ϲ� ���� | 3.���'s pick | 4.���� => ");
					ch = Integer.parseInt(br.readLine());
				} while (ch < 1 || ch > 4);

				if (ch == 4)
					break;

				switch (ch) {
				case 1:
					purchaseMask();
					break;
				case 2:
					purchase();
					break;
				case 3:
					prescribe();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void purchaseMask() {
		System.out.println("\n��  ����ũ ���Ÿ� ���͵帮�ڽ��ϴ�. ��");
		int ch;
		int qty; // �䱸 ����
		int remain = 0; // �� �� �ִ� ����ũ ����
		int result = 0;
		try {
			if (dto == null) {
				dto = identifyCustomer();
			} else if (dto != null) {
				System.out.print("\n"+dto.getcName() + "���� �½��ϱ�? 1.�� 2.�ƴϿ� > ");
				ch = Integer.parseInt(br.readLine());
				if (ch == 2) {
					dto = identifyCustomer();
				}
			}
			String day = dao.checkAvailability(dto.getRrn());
			System.out.println("\n"+ dto.getcName() + "���� ����ũ ���� ������ ��¥��");
			System.out.println("�� " + day + " ���Դϴ�.");

			String today = dao.checkDate();
			if (!day.equals(today)) {
				// ���� ������ �� ������...
				return;
			}

			System.out.println("������ " + today + "�� ����ũ ���� �ڰ��ɡ��մϴ�.");
			System.out.print("�����Ͻðڽ��ϱ�? 1.�� 2.�ƴϿ� > ");
			ch = Integer.parseInt(br.readLine());
			if (ch == 2) {
				return;
			}

			/////////////// �޼��� ����
			remain = checkRemain();
			if (remain > 0) {
				System.out.println(dto.getcName() + "���� �̹��ֿ� " + remain + "������ ���Ű� �����մϴ�.");
			} else {
				switch (remain) {
				case -20011:
					// �̹� ������ ���� �Ǻ��Ͽ����Ƿ� skip...
//					System.out.println("������ ���� ����� �ƴϽʴϴ�.");
					return;
				case -20021:
					System.out.println("\n�̹� �̹��ֿ� �����ϼ����Ƿ� ���Ű� �Ұ����մϴ�. /_\\");
					System.out.println("�����ֿ� ã���ּ���~");
					return;
				}
			}

			System.out.print("�� �� �����Ͻ� �ǰ��� [���: 0]? ");
			qty = Integer.parseInt(br.readLine());
			if (qty == 0) {
				System.out.println("����ũ ���Ÿ� ����մϴ�...");
				return;
			}
			System.out.println(qty + "�� �ֹ��ϼ���? ��ø���!");
			result = dao.insertSaleMask(dto, 1, qty);
			if (result >= 1) {
				System.out.println("{{{(>_<)}}} ���Ű� �Ϸ�Ǿ����ϴ�");
			} else {
				System.out.println("������ ���� ���ſ� �����Ͽ����ϴ�...");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	///
	public int checkRemain() {// ���� ����
		int qty = 0;
		try {
			if (dto != null && dto.getcNum()<0) {
				System.out.println("���� �౹���� ����ũ�� �����Ͻ� �̷��� �����Ƿ� ���Ű� �����մϴ�.>��<");
			}
			// ȸ������� �� ��� �Լ��� ���Ͽ� ȣ������
			qty = dao.checkPurchaseMask(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return qty;
	}

	public void purchase() {
		int result;
		try {
			int pnum, qty;
			// ��ǰ ��� ���
			List<ProductDTO> list = productDAO.listProduct();
			for (ProductDTO dto : list) {
				if (!dto.getPname().contains("����ũ")) {
					System.out.println(dto);
				}
			}
			System.out.print("������ ��ǰ��ȣ [���: 0]> ");
			pnum = Integer.parseInt(br.readLine());
			if (pnum == 0) {
				return;
			}
			System.out.print("���� > ");
			qty = Integer.parseInt(br.readLine());
			result = dao.insertSaleItem(pnum, qty);
//				break;
//			}
			if (result >= 1) {
				System.out.println("{{{(>_<)}}} ���Ű� �Ϸ�Ǿ����ϴ�");
			} else {
				System.out.println("������ ���� ���ſ� �����Ͽ����ϴ�...");
			}
			System.out.println();
		} catch (NumberFormatException e) {
			System.out.println("�ùٸ� ���ڸ� �Է��ϼ���\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isValidRRN(String rrn) {
		// �ֹε�Ϲ�ȣ ��ȿ�� �˻�
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
				System.out.println("�� ����" + month);
				return false;
			}
			Calendar cal = Calendar.getInstance();
			cal.set(year, month - 1, 1);
			endDayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			if (day < 1 || day > endDayOfMonth) {
				System.out.println("��" + day + "..." + endDayOfMonth);
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public CustomerDTO identifyCustomer() {
		CustomerDTO dto = null;
		try {
			String rrn, name;
			System.out.print("���� :  ");
			name = br.readLine();
			System.out.print("�ֹι�ȣ : ");
			// ȸ�������� ��ϵ� �ִ��� �˻�
			rrn = br.readLine();
			if (rrn == null || rrn.length() == 0) {
				rrn = "011009-3012345";
				System.out.println(rrn + " �ڵ��Է�");
			}
			if (!isValidRRN(rrn)) {
				throw new Exception("�ֹε�Ϲ�ȣ ���Ŀ� ���� �ʽ��ϴ� �Ф�.");
			}
			// �ֹε�Ϲ�ȣ�� �������� ������ �߰��� ������ ����
			if (rrn.length() == 13 && isNumber(rrn)) {
				rrn = rrn.substring(0, 6) + "-" + rrn.substring(6);
				System.out.println(rrn);
			}
			dto = dao.readCustomer(rrn);
			if (dto != null) {
				name = dto.getcName();
			} else {
				// �������� ������
				dto = new CustomerDTO(-1, name, rrn);
			}
			System.out.println("*" + name + "���� �湮�� ȯ���մϴ� ^^ *");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}

	public static boolean isNumber(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void prescribe() {
		int result; // ���� ó�����
		int choice, qty; // ���ù�ȣ, ����
		List<ProductDTO> list = null;
		List<String> keywords = null;
		try {
			keywords = dao.getKeywords();
			System.out.println("=========================");
			System.out.println("�ȳ��ϼ��� ȯ�ڴ�. /_\\ ��� ��������???");
			System.out.println("=========================");
			printSymtoms(keywords);// ���� ���
			System.out.print("\n ���� ��ȣ �Է� (���: 0) > ");
			choice = Integer.valueOf(br.readLine());
			if (choice == 0) {
				System.out.println("�Է��� ����մϴ�.");
				return;
			}
			list = dao.searchKeyword(keywords.get(choice - 1));
			if (list == null || list.size() == 0) {
				System.out.println("�˻� ����� �����ϴ�.");
				return;
			}
			for (int i = 0; i < list.size(); i++) {
				// ���� ��ǰ ���
				System.out.println(i + 1 + "��. " + list.get(i).toString());
			}
			System.out.print("\n ó���� �� ��� ��ȣ�� ���� > ");
			choice = Integer.valueOf(br.readLine());
			if (choice < 1 || choice > list.size()) {
				System.out.println("�ùٸ� ��ȣ�� �Է��ϼ���.");
				return;
			}
			System.out.print("���� �Է� ? ");
			qty = Integer.valueOf(br.readLine());
			result = dao.insertSaleItem(list.get(choice - 1).getPnum(), qty);
			if (result != 0) {
				System.out.println("ó��Ǿ����ϴ�. �����ޣ��ޣ���");
			}
		} catch (NumberFormatException e) {
			System.out.println("/_ \\ �ùٸ��� �Է��� �ּ���... ");
		} catch (Exception e) {
//			e.printStackTrace();
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
