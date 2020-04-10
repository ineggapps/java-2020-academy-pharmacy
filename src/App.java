import java.util.Scanner;

public class App {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		Customer customer = new Customer();
		Pharmacist pharmacist = new Pharmacist(); //����
		try {
			int ch;
			while (true) {
				do {
					System.out.println("\n>>> �ֿ� �౹ <<<");
					System.out.print("1.����� | 2.��� | 3.���� > ");
					ch = sc.nextInt();
				} while (ch < 1 || ch > 3);
				if (ch == 3) {
					break;
				}
				switch (ch) {
				case 1:
					// �մ�
					customer.customer();
					break;
				case 2:
					// ���
					pharmacist.pharmacistManage();
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("����Ǿ����ϴ�. ���α׷��� �ٽ� �������ּ���.\n"+e.getMessage());
		} finally {
			sc.close();
		}
	}
}
