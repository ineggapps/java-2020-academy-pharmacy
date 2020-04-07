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
	 * private PharmacistDAO dao = new PharmacistDAOImpl(); > 궁금한 점. -
	 * PharmacistDAO를 인터페이스로 생성하고, 그것을 구현한것이 PharmacistDAOImpl인데, - private
	 * PharmacistDAO dao = new PharmacistDAOImpl(); 이렇게 인스턴스화 가능?
	 */

	public Pharmacist() {
		administrator.put(KEY_ID, ADMIN_ID);
		administrator.put(KEY_PW, ADMIN_PW);
	}

	public void pharmacistManage() {
		int ch;
		while (true) {
			System.out.println("\n 약사");
			do {
				System.out.println("1.재고관리  2. 판매현황  3.처방   4.로그아웃 =>");
				ch = sc.nextInt();
			} while (ch < 1 || ch > 4);

			if (ch == 4)
				break;

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
	}

	public void inventory() {
		int ch;

		while (true) {
			System.out.println("\n 약사 [관리자 모드]");
			String id;
			String pwd;
			if (!loggedIn) {
				do {
					System.out.println("BUT.... 로그인이 필요합니다.");
					System.out.print("아이디:");
					id = sc.next();
					System.out.print("비밀번호:");
					pwd = sc.next();
					if (id.equals(administrator.get(KEY_ID)) && pwd.equals(administrator.get(KEY_PW))) {
						System.out.println("관리자로 로그인하였습니다.");
						loggedIn = true;
					} else {
						System.out.println("아이디나 비밀번호가 일치하지 않습니다.");
					}
				} while (!loggedIn);//로그인이 되지 않은 경우에 계속 순회
			}

			do {
				System.out.println("1.입고등록 2.제품수정 3.제품삭제  4.리스트 5.종료=>");
				ch = sc.nextInt();
			} while (ch < 1 || ch > 5);

			if (ch == 5)
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
				sales();
				break;

			}

		}
	}

//입고	
	public void insert() {
		System.out.println("\n 입고 등록..."); // 마스크와 손소독제 입고 등록

		InputDTO dto = new InputDTO();
		try {
			System.out.print("물품 번호? [1.마스크 2.손소독제]");
			dto.setPnum(sc.nextInt());
			System.out.print("입고 날짜?");
			dto.setIdate(sc.next());
			System.out.println("개수 ? ");
			dto.setIqty(sc.nextInt());

			int result = dao.insertProduct(dto);
			if (result != 0)
				;
			System.out.println("입고번호 " + result + "이 등록이 완료 되었습니다.");
			pharmacistManage();

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println();

	}

//물품수정	
	public void update() {
		System.out.println("물품 수정 ... ");
		InputDTO dto = new InputDTO();

		try {
			System.out.print("수정 할 입고번호?");
			dto.setInum(sc.nextInt());

			System.out.print("물품 번호?");
			dto.setPnum(sc.nextInt());

			System.out.print("입고 날짜?");
			dto.setIdate(sc.next());

			System.out.print("수량?");
			dto.setIqty(sc.nextInt());

			int result = dao.updateProduct(dto);
			if (result != 0) {
				System.out.println("수정 완료.");
			} else {
				System.out.println("수정 실패");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

//제품삭제	
	public void delete() {
		System.out.println("[ 물품 삭제 ]");
		int inum;

		try {
			InputDTO dto = new InputDTO();
			System.out.print("삭제할 입고 번호?");
			inum = sc.nextInt();
			int result = dao.deleteProduct(inum);
			if (result != 0) {
				System.out.println(result + "번 물품을 삭제 했습니다.");
			} else {
				System.out.println("삭제 실패.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void sales() {
		int ch;

		while (true) {
			System.out.println("\n 약사");
			do {
				System.out.println("1.재고리스트 2.손님별 판매현황 3.전체 판매리스트  4.종료=>");
				ch = sc.nextInt();
			} while (ch < 1 || ch > 4);

			if (ch == 4)
				break;

			switch (ch) {
			case 1:
				liststock();
				break;
			case 2:
				customerlist();
				break;
			case 3:
				productlist();
				break;
			}
		}
	}

//재고리스트	
	public void liststock() {
		System.out.println("물품 재고 리스트");

		List<ProductDTO> list = dao.listStock();
		for (ProductDTO dto : list) {
			System.out.print("물품번호:  " + dto.getPnum() + "\t");
			System.out.print("물품명:  " + dto.getPname() + "\t");
			System.out.print("재고:  " + dto.getStock() + "\n");
		}

	}

//손님별 판매리스트
	public void customerlist() {
		System.out.println("\n손님별 판매리스트...");
		String rrn;
		System.out.println("검색할 손님?");
		rrn = sc.next();
		List<SaleSumDTO> list = dao.listCustomer(rrn);

		for (SaleSumDTO dto : list) {
			System.out.println(dto.getcName() + "\t");
			System.out.print(dto.getSdate() + "\t");
			System.out.print(dto.getpName() + "\t");
			System.out.print(dto.getsQty() + "\n");
		}
	}

//전체 판매리스트
	private void productlist() {
		System.out.println("\n품목별 판매리스트...");
		List<SaleSumDTO> list = dao.listProduct();

		for (SaleSumDTO dto : list) {
			System.out.print(dto.getpName() + "\t");
			System.out.print(dto.getpNum() + "\t");
			System.out.print(dto.getsQty() + "\t");
			System.out.print(dto.getSdate() + "\n");
		}
	}

//처방

	public void prescription() {
		int result; // 쿼리 처리결과
		int choice, qty; // 선택번호, 수량
		String keyword; // 검색어
		List<ProductDTO> list = null;
		try {
			System.out.println("\n 증상 입력 >...");
			keyword = sc.next().trim();
			if (keyword == null || keyword.length() == 0) {
				System.out.println("키워드를 입력하세요...");
				return;
			}
			list = dao.searchKeyword(keyword);
			for (int i = 0; i < list.size(); i++) {
				// 관련 상품 출력
				System.out.println(i + 1 + "번. " + list.get(i).toString());
			}
			System.out.print("\n 처방할 약 목록 번호를 선택 > ");
			choice = sc.nextInt();
			if (choice < 1 || choice > list.size()) {
				System.out.println("올바른 번호를 입력하세요.");
				return;
			}
			System.out.print("수량 입력 ? ");
			qty = sc.nextInt();
			result = dao.insertSale(list.get(choice - 1).getPnum(), qty);
			if (result != 0) {
				System.out.println("처방되었습니다. ＜（＾－＾）＞");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
