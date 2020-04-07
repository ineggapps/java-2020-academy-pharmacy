import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Calendar;

/**/
public class Customer {
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	CustomerDAO dao = new CustomerDAOImpl();
	CustomerDTO dto = null;

	public void customer() {
		int ch;
		try {
			while (true) {
				System.out.println("\n[고객]");
				do {
					System.out.print("1.마스크 구매 가능 여부 확인 2.구매 3.메인 => ");
					ch = Integer.parseInt(br.readLine());
				} while (ch < 1 || ch > 4);

				if (ch == 3)
					break;

				switch (ch) {
				case 1:
					check();
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

	private void check() {
		System.out.println("\n마스크 구매 가능 여부 확인");
		CustomerDTO dto = new CustomerDTO();
		String name, rrn;

		try {
			System.out.print("이름 > ");
			name = br.readLine();

			System.out.print("주민번호 [- 입력] > ");
			rrn = br.readLine();

			if (rrn.length() != 14) {
				System.out.println("유효하지않은 주민번호 입니다.");
				return;
			}
			dto = dao.checkAvailability(rrn);
			System.out.println(name + "님의 마스크 구매 가능한 날짜는");
			System.out.println("★ " + dto.getRrn() + " ★입니다.");

			String date = dto.getRrn();

			if (date.equals(dao.checkDate())) {
				System.out.println("오늘은 " + dao.checkDate() + "로 마스크 구매 가능합니다.");
				System.out.println("구매하시겠습니까?");
				int ch;
				while (true) {
					do {
						System.out.print("1.네 2.아니오");
						ch = Integer.parseInt(br.readLine());
					} while (ch < 1 || ch > 2);
					if (ch == 2) {
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	///
	public int checkPurchase() {// 구매여부 확인
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
		int result = 0;
		try {
			int ch, qty;
			System.out.print("1.마스크 2.손소독제 > ");
			ch = Integer.parseInt(br.readLine());
			System.out.print("수량? ");
			qty = Integer.parseInt(br.readLine());
			switch (ch) {
			case 1:
				if (dto == null) {
					dto = identifyCustomer();
				} else if (dto != null) {
					System.out.print(dto.getcName() + "님이 맞습니까? 1.예 2.아니오 > ");
					ch = Integer.parseInt(br.readLine());
					if (ch == 2) {
						dto = identifyCustomer();
					}
				}
				int remain = 0; // 살 수 있는 마스크 개수
				remain = checkPurchase();
				if (remain > 0) {
					System.out.println(dto.getcName() + "님은 이번주에 " + remain + "개까지 구매가 가능합니다.");
				} else {
					switch (remain) {
					case -20011:
						System.out.println("오늘은 구매 대상이 아니십니다.");
						break;
					case -20021:
						System.out.println("이미 이번주에 구매하셨으므로 구매가 불가능합니다.");
						break;
					}
				}
				System.out.println(qty + "개 주문하셨죠? 잠시만요!");
				result = dao.insertSale(dto, ch, qty);
				break;
			case 2:
				break;
			}
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
