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
				System.out.println("◎  방문해주셔서 감사합니다.무엇이 필요하신가요? ◎");
				do {
					System.out.print("1.코로나 마스크 구매 | 2.일반 구매 | 3.약사's pick | 4.메인 => ");
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
		System.out.println("\n◎  마스크 구매를 도와드리겠습니다. ◎");
		int ch;
		int qty; // 요구 수량
		int remain = 0; // 살 수 있는 마스크 개수
		int result = 0;
		try {
			if (dto == null) {
				dto = identifyCustomer();
			} else if (dto != null) {
				System.out.print("\n"+dto.getcName() + "님이 맞습니까? 1.예 2.아니오 > ");
				ch = Integer.parseInt(br.readLine());
				if (ch == 2) {
					dto = identifyCustomer();
				}
			}
			String day = dao.checkAvailability(dto.getRrn());
			System.out.println("\n"+ dto.getcName() + "님의 마스크 구매 가능한 날짜는");
			System.out.println("★ " + day + " ★입니다.");

			String today = dao.checkDate();
			if (!day.equals(today)) {
				// 오늘 구매할 수 없으면...
				return;
			}

			System.out.println("오늘은 " + today + "로 마스크 구매 ★가능★합니다.");
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
					System.out.println("\n이미 이번주에 구매하셨으므로 구매가 불가능합니다. /_\\");
					System.out.println("다음주에 찾아주세요~");
					return;
				}
			}

			System.out.print("몇 개 구매하실 건가요 [취소: 0]? ");
			qty = Integer.parseInt(br.readLine());
			if (qty == 0) {
				System.out.println("마스크 구매를 취소합니다...");
				return;
			}
			System.out.println(qty + "개 주문하셨죠? 잠시만요!");
			result = dao.insertSaleMask(dto, 1, qty);
			if (result >= 1) {
				System.out.println("{{{(>_<)}}} 구매가 완료되었습니다");
			} else {
				System.out.println("오류로 인해 구매에 실패하였습니다...");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	///
	public int checkRemain() {// 구매 개수
		int qty = 0;
		try {
			if (dto != null && dto.getcNum()<0) {
				System.out.println("저희 약국에서 마스크를 구매하신 이력이 없으므로 구매가 가능합니다.>ㅡ<");
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
		try {
			int pnum, qty;
			// 상품 목록 출력
			List<ProductDTO> list = productDAO.listProduct();
			for (ProductDTO dto : list) {
				if (!dto.getPname().contains("마스크")) {
					System.out.println(dto);
				}
			}
			System.out.print("구매할 상품번호 [취소: 0]> ");
			pnum = Integer.parseInt(br.readLine());
			if (pnum == 0) {
				return;
			}
			System.out.print("수량 > ");
			qty = Integer.parseInt(br.readLine());
			result = dao.insertSaleItem(pnum, qty);
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
			System.out.print("성명 :  ");
			name = br.readLine();
			System.out.print("주민번호 : ");
			// 회원정보가 등록돼 있는지 검색
			rrn = br.readLine();
			if (rrn == null || rrn.length() == 0) {
				rrn = "011009-3012345";
				System.out.println(rrn + " 자동입력");
			}
			if (!isValidRRN(rrn)) {
				throw new Exception("주민등록번호 형식에 맞지 않습니다 ㅠㅠ.");
			}
			// 주민등록번호에 하이픈이 없으면 중간에 하이픈 삽입
			if (rrn.length() == 13 && isNumber(rrn)) {
				rrn = rrn.substring(0, 6) + "-" + rrn.substring(6);
				System.out.println(rrn);
			}
			dto = dao.readCustomer(rrn);
			if (dto != null) {
				name = dto.getcName();
			} else {
				// 존재하지 않으면
				dto = new CustomerDTO(-1, name, rrn);
			}
			System.out.println("*" + name + "님의 방문을 환영합니다 ^^ *");
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
		int result; // 쿼리 처리결과
		int choice, qty; // 선택번호, 수량
		List<ProductDTO> list = null;
		List<String> keywords = null;
		try {
			keywords = dao.getKeywords();
			System.out.println("=========================");
			System.out.println("안녕하세요 환자님. /_\\ 어떻게 아프세요???");
			System.out.println("=========================");
			printSymtoms(keywords);// 증상 목록
			System.out.print("\n 증상 번호 입력 (취소: 0) > ");
			choice = Integer.valueOf(br.readLine());
			if (choice == 0) {
				System.out.println("입력을 취소합니다.");
				return;
			}
			list = dao.searchKeyword(keywords.get(choice - 1));
			if (list == null || list.size() == 0) {
				System.out.println("검색 결과가 없습니다.");
				return;
			}
			for (int i = 0; i < list.size(); i++) {
				// 관련 상품 출력
				System.out.println(i + 1 + "번. " + list.get(i).toString());
			}
			System.out.print("\n 처방할 약 목록 번호를 선택 > ");
			choice = Integer.valueOf(br.readLine());
			if (choice < 1 || choice > list.size()) {
				System.out.println("올바른 번호를 입력하세요.");
				return;
			}
			System.out.print("수량 입력 ? ");
			qty = Integer.valueOf(br.readLine());
			result = dao.insertSaleItem(list.get(choice - 1).getPnum(), qty);
			if (result != 0) {
				System.out.println("처방되었습니다. ＜（＾－＾）＞");
			}
		} catch (NumberFormatException e) {
			System.out.println("/_ \\ 올바르게 입력해 주세요... ");
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
