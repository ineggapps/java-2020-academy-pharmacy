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
				do {
					System.out.print("1.�ڷγ� ����ũ ���� 2.�ǰ��Ǿ�ǰ ���� 3.���� => ");
					ch = Integer.parseInt(br.readLine());
				} while (ch < 1 || ch > 4);

				if (ch == 3)
					break;

				switch (ch) {
				case 1:
					purchaseMask();
					break;
				case 2:
					purchase();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void purchaseMask() {
		System.out.println("\n����ũ �����ұ�?");
		int ch;
		int qty; // �䱸 ����
		int remain = 0; // �� �� �ִ� ����ũ ����
		int result = 0;
		List<Integer> pnums = null;

		try {
			if (dto == null) {
				dto = identifyCustomer();
			} else if (dto != null) {
				System.out.print(dto.getcName() + "���� �½��ϱ�? 1.�� 2.�ƴϿ� > ");
				ch = Integer.parseInt(br.readLine());
				if (ch == 2) {
					dto = identifyCustomer();
				}
			}
			String day = dao.checkAvailability(dto.getRrn());
			System.out.println(dto.getcName() + "���� ����ũ ���� ������ ��¥��");
			System.out.println("�� " + day + " ���Դϴ�.");

			String today = dao.checkDate();
			if (!day.equals(today)) {
				// ���� ������ �� ������...
				return;
			}

			System.out.println("������ " + today + "�� ����ũ ���� �����մϴ�.");
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
					System.out.println("�̹� �̹��ֿ� �����ϼ����Ƿ� ���Ű� �Ұ����մϴ�.");
					return;
				}
			}

			System.out.println("�� �� �ֹ��Ͻ� �ǰ��� (���: 0)? ");
			qty = Integer.parseInt(br.readLine());
			if (qty == 0) {
				System.out.println("����ũ ���Ÿ� ����մϴ�...");
				return;
			}
			System.out.println(qty + "�� �ֹ��ϼ���? ��ø���!");
			pnums = dao.getMaskProductNumbers("����ũ", true);
			result = dao.insertSaleMask(dto, pnums.get((int) (Math.random() * pnums.size())), qty);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	///
	public int checkRemain() {// ���� ����
		int qty = 0;
		try {
			if (dto == null) {
				System.out.println("���� �౹���� ����ũ�� �����Ͻ� �̷��� �����Ƿ� ���Ű� �����մϴ�.");
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
		List<Integer> pnums = null;
		try {
			int ch, qty;
			// ��ǰ ��� ���
			List<ProductDTO> list = productDAO.listProduct();
			for (ProductDTO dto : list) {
				if (!dto.getPname().contains("����ũ")) {
					System.out.println(dto);
				}
			}
			System.out.println("������ ��ǰ��ȣ ? ");
			ch = Integer.parseInt(br.readLine());
			System.out.print("����? ");
			qty = Integer.parseInt(br.readLine());
//			switch (ch) {
//			case 1:

//				break;
//			default:
			pnums = dao.getMaskProductNumbers("�ռҵ���", true);
			result = dao.insertSaleItem(pnums.get((int) (Math.random() * pnums.size())), qty);
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
			System.out.print("�̸� ? ");
			name = br.readLine();
			System.out.print("�ֹι�ȣ ? ");
			// ȸ�������� ��ϵ� �ִ��� �˻�
			rrn = br.readLine();
			if (rrn == null || rrn.length() == 0) {
				rrn = "011009-3012345";
				System.out.println(rrn + " �ڵ��Է�");
			}
			if (!isValidRRN(rrn)) {
				throw new Exception("�ֹε�Ϲ�ȣ ���Ŀ� ���� �ʽ��ϴ�.");
			}
			// �ֹε�Ϲ�ȣ�� �������� ������ �߰��� ������ ����
			if (rrn.length() == 13 && isNumber(rrn)) {
				rrn = rrn.substring(0, 7) + "-" + rrn.substring(6);
				System.out.println(rrn);
			}
			dto = dao.readCustomer(rrn);
			if (dto != null) {
				name = dto.getcName();
			} else {
				// �������� ������
				dto = new CustomerDTO(-1, name, rrn);
			}
			System.out.println(name + "�� ����.");
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
}
