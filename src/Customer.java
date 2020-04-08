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
				System.out.println("\n[고객]");
				do {
					System.out.print("1.코로나 마스크 구매 2.건강의약품 구매 3.메인 => ");
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
		System.out.println("\n마스크 구매할까?");
		int ch;
		int qty; // 요구 수량
		int remain = 0; // 살 수 있는 마스크 개수
		int result = 0;
		List<Integer> pnums = null;

		try {
			if (dto == null) {
				dto = identifyCustomer();
			} else if (dto != null) {
				System.out.print(dto.getcName() + "님이 맞습니까? 1.예 2.아니오 > ");
				ch = Integer.parseInt(br.readLine());
				if (ch == 2) {
					dto = identifyCustomer();
				}
			}
			String day = dao.checkAvailability(dto.getRrn());
			System.out.println(dto.getcName() + "님의 마스크 구매 가능한 날짜는");
			System.out.println("★ " + day + " ★입니다.");

			String today = dao.checkDate();
			if (!day.equals(today)) {
				// 오늘 구매할 수 없으면...
				return;
			}

			System.out.println("오늘은 " + today + "로 마스크 구매 가능합니다.");
			System.out.print("구매하시겠습니까? 1.예 2.아니오 > ");
			ch = Integer.parseInt(br.readLine());
			if (ch == 2) {
				return;
			}

			/////////////// 메서드 통합
			remain = checkRemain();
			if (remain > 0) {
				System.out.println(dto.getcName() + "님은 이번주에 " + remain + "개까지 구매가 가능합니다.");
			} else {
				switch (remain) {
				case -20011:
					// 이미 위에서 조건 판별하였으므로 skip...
//					System.out.println("오늘은 구매 대상이 아니십니다.");
					return;
				case -20021:
					System.out.println("이미 이번주에 구매하셨으므로 구매가 불가능합니다.");
					return;
				}
			}

			System.out.println("몇 개 주문하실 건가요 (취소: 0)? ");
			qty = Integer.parseInt(br.readLine());
			if (qty == 0) {
				System.out.println("마스크 구매를 취소합니다...");
				return;
			}
			System.out.println(qty + "개 주문하셨죠? 잠시만요!");
			pnums = dao.getMaskProductNumbers("마스크", true);
			result = dao.insertSaleMask(dto, pnums.get((int) (Math.random() * pnums.size())), qty);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	///
	public int checkRemain() {// 구매 개수
		int qty = 0;
		try {
			if (dto == null) {
				System.out.println("저희 약국에서 마스크를 구매하신 이력이 없으므로 구매가 가능합니다.");
			}
			// 회원등록이 된 경우 함수를 통하여 호출하자
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
			// 상품 목록 출력
			List<ProductDTO> list = productDAO.listProduct();
			for (ProductDTO dto : list) {
				if (!dto.getPname().contains("마스크")) {
					System.out.println(dto);
				}
			}
			System.out.println("구매할 상품번호 ? ");
			ch = Integer.parseInt(br.readLine());
			System.out.print("수량? ");
			qty = Integer.parseInt(br.readLine());
//			switch (ch) {
//			case 1:

//				break;
//			default:
			pnums = dao.getMaskProductNumbers("손소독제", true);
			result = dao.insertSaleItem(pnums.get((int) (Math.random() * pnums.size())), qty);
//				break;
//			}
			if (result >= 1) {
				System.out.println("{{{(>_<)}}} 구매가 완료되었습니다");
			} else {
				System.out.println("오류로 인해 구매에 실패하였습니다...");
			}
			System.out.println();
		} catch (NumberFormatException e) {
			System.out.println("올바른 숫자를 입력하세요\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isValidRRN(String rrn) {
		// 주민등록번호 유효성 검사
		int year, month, day;
		int endDayOfMonth;
		try {
			// 1. 앞자리 6자리 + 뒷자리 7자리 = 13 혹은 하이픈 포함하여 14자리인지 검사
			if (rrn.length() < 13 || rrn.length() > 14) {
				System.out.println("주민등록번호 자릿수 오류");
				return false;
			}
			year = Integer.parseInt(rrn.substring(0, 2));
			month = Integer.parseInt(rrn.substring(2, 4));
			day = Integer.parseInt(rrn.substring(4, 6));
			if (month < 1 || month > 12) {
				System.out.println("월 오류" + month);
				return false;
			}
			Calendar cal = Calendar.getInstance();
			cal.set(year, month - 1, 1);
			endDayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			if (day < 1 || day > endDayOfMonth) {
				System.out.println("일" + day + "..." + endDayOfMonth);
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
			System.out.print("이름 ? ");
			name = br.readLine();
			System.out.print("주민번호 ? ");
			// 회원정보가 등록돼 있는지 검색
			rrn = br.readLine();
			if (rrn == null || rrn.length() == 0) {
				rrn = "011009-3012345";
				System.out.println(rrn + " 자동입력");
			}
			if (!isValidRRN(rrn)) {
				throw new Exception("주민등록번호 형식에 맞지 않습니다.");
			}
			// 주민등록번호에 하이픈이 없으면 중간에 하이픈 삽입
			if (rrn.length() == 13 && isNumber(rrn)) {
				rrn = rrn.substring(0, 7) + "-" + rrn.substring(6);
				System.out.println(rrn);
			}
			dto = dao.readCustomer(rrn);
			if (dto != null) {
				name = dto.getcName();
			} else {
				// 존재하지 않으면
				dto = new CustomerDTO(-1, name, rrn);
			}
			System.out.println(name + "님 입장.");
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
