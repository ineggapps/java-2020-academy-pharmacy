import java.util.Calendar;
import java.util.HashMap;
import java.util.InputMismatchException;
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
		try {
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
							return;
						}
					} while (!loggedIn);// 로그인이 되지 않은 경우에 계속 순회
				}
				do {
					System.out.print("1.제품관리  2. 판매현황  3.증상관리   4.로그아웃 =>");
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
					managePrescription();
					break;
				}
			}

		} catch (Exception e) {
//			e.printStackTrace();
			System.out.println("비정상적으로 입력하셨습니다.");
			sc.next();
		}
	}

	public void inventory() {
		int ch;
		try {
			while (true) {
				do {
					System.out.print("1.제품추가 2.제품수정 3.제품삭제 4.제품목록 5.입고등록 6.입고수정 7.입고삭제 8.입고목록 9.종료 => ");
					ch = sc.nextInt();
				} while (ch < 1 || ch > 9);
				if (ch == 9)
					return;
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
					listStock();
					break;
				case 5:
					input();
					break;
				case 6:
					updateInput();
					break;
				case 7:
					deleteInput();
					break;
				case 8:
					listInput();
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("올바르게 입력되지 않았습니다.");
		}
	}

	// 제품추가
	public void insert() {
		System.out.println("\n 제품추가...");
		ProductDTO dto = new ProductDTO();
		try {
			System.out.print("추가할 제품명? ");
			dto.setPname(sc.next());
			System.out.print("판매가 ? ");
			dto.setPrice(sc.nextInt());

			int result = dao.insertProduct(dto);
			System.out.println(dto.getPname() + "이 등록이 완료 되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	// 제품수정
	public void update() {
		System.out.println("\n 제품수정...");
		int pnum;
		ProductDTO dto = new ProductDTO();
		try {

			System.out.print(" 수정할 품목 번호? ");
			pnum = sc.nextInt();

			dto = dao.readProduct(pnum);

			if (dto == null) {
				System.out.println("등록된 아이디가 없습니다.\n");
				return;
			}

			System.out.print(dto.getPnum() + "\t");
			System.out.print(dto.getPname() + "\t");
			System.out.print(dto.getPrice() + "\t");
			System.out.print(dto.getStock() + "\n");

			System.out.print("품목 명? ");
			dto.setPname(sc.next());
			System.out.print("판매가 ? ");
			dto.setPrice(sc.nextInt());

			int result = dao.updateProduct(dto);
			System.out.println(result + "이 수정 완료 되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	public void delete() {
		System.out.println("\n제품 삭제...");
		int pnum;
		ProductDTO dto = new ProductDTO();

		try {
			printProducts(dao.listProduct());
			System.out.print("삭제할 제품번호?");
			pnum = sc.nextInt();

			dto = dao.readProduct(pnum);

			if (dto == null) {
				System.out.println("등록된 제품이 없습니다.\n");
				return;
			}

			System.out.print(dto.getPnum() + "\t");
			System.out.print(dto.getPname() + "\t");
			System.out.print(dto.getPrice() + "\t");
			System.out.print(dto.getStock() + "\n");

			int result = dao.deleteProduct(pnum);

			if (result != 0)
				System.out.println("제품삭제 성공....");
		} catch (Exception e) {
			System.out.println("제품 삭제에 실패했습니다.\n" + e.getMessage());
		}

	}

//입고	
	public void input() {
		System.out.println("\n 입고 등록..."); // 마스크와 손소독제 입고 등록

		InputDTO dto = new InputDTO();
		try {
			printProducts(dao.listProduct());
			System.out.print("물품 번호? ");
			dto.setPnum(sc.nextInt());
			System.out.print("입고 날짜? ");
			dto.setIdate(sc.next());
			System.out.print("개수 ? ");
			dto.setIqty(sc.nextInt());

			int result = dao.insertInput(dto);
			if (result != 0)
				;
			System.out.println("입고번호 " + result + "이 등록이 완료 되었습니다.");
			pharmacistManage();

		} catch (Exception e) {
			System.out.println("올바르게 입력되지 않았습니다...");
		}

		System.out.println();

	}

	// 물품수정

	public void updateInput() {
		System.out.println("물품 수정 ... ");

		List<InputListDTO> list = dao.listStock();
		for (InputListDTO Ip : list) {
			System.out.print("입고번호:  " + Ip.getInum() + "\t");
			System.out.print("물품번호:  " + Ip.getPnum() + "\t");
			System.out.print("물품명:  " + Ip.getPname() + "\t");
			System.out.print("입고수량 : " + Ip.getIqty() + "\t");
			System.out.print("재고:  " + Ip.getStock() + "\n");
		}
		try {
			InputListDTO dto = new InputListDTO();
			System.out.print("수정 할 입고번호?");
			dto.setInum(sc.nextInt());
			InputDTO input = dao.readInput(dto.getInum());
			if (input == null) {
				System.out.println("잘못 입력하셨습니다.");
				return;
			}
			System.out.print("입고 날짜?");
			dto.setIdate(sc.next());
			System.out.print("수량?");
			int beforeIqty = input.getIqty();
			dto.setIqty(sc.nextInt());
			if (beforeIqty >= dto.getIqty()) {
				System.out.println("재고수량은 이전 수량보다 크게 입력해야 합니다.");
				return;
			}
			int result = dao.updateInput(dto);
			if (result != 0) {
				System.out.println("수정 완료.");
			} else {
				System.out.println("수정 실패");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 제품삭제

	public void deleteInput() {

		List<InputListDTO> list = dao.listStock();
		for (InputListDTO Ip : list) {
			System.out.print("입고번호:  " + Ip.getInum() + "\t");
			System.out.print(" 물품번호:  " + Ip.getPnum() + "\t");
			System.out.print(" 물품명:  " + Ip.getPname() + "\t");
			System.out.print(" 입고수량 : " + Ip.getIqty() + "\t");
			System.out.print(" 재고:  " + Ip.getStock() + "\n");
		}
		int inum;
		try {
			System.out.print("삭제할 입고 번호? (취소: 0) > ");
			inum = sc.nextInt();
			if (inum == 0) {
				return;
			}
			int result = dao.deleteInput(inum);
			if (result != 0) {
				System.out.println(inum + "번 물품을 삭제 했습니다.");
			} else {
				System.out.println("삭제 실패.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 재고리스트
	public void listStock() {
		System.out.println("물품 재고 리스트");
		List<ProductDTO> list = dao.listProduct();
		for (ProductDTO dto : list) {
			System.out.print("물품번호:  " + dto.getPnum() + "\t");
			System.out.print(" 물품명:  " + dto.getPname() + "\t");
			System.out.print(" 가격:  " + dto.getPrice() + "\t");
			System.out.print(" 재고: " + dto.getStock() + "\t");
			System.out.println();
		}
	}

	// 입고리스트
	public void listInput() {
		System.out.println("물품 입고 리스트");
		List<InputListDTO> list = dao.listStock();
		for (InputListDTO dto : list) {
			System.out.print("입고번호:  " + dto.getInum() + "\t");
			System.out.print(" 물품번호:  " + dto.getPnum() + "\t");
			System.out.print(" 물품명:  " + dto.getPname() + "\t");
			System.out.print(" 입고수량 : " + dto.getIqty() + "\t");
			System.out.println();
		}
	}

	public void sales() {
		int ch;
		while (true) {
			System.out.println("\n 약사");
			do {
				System.out.println("1.손님별 마스크 판매현황 2.전체 판매리스트  3.종료=> ");
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

//주민번호 유효성검사	
	public boolean isValidRRN(String rrn) {
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
				System.out.println("월 입력오류 : " + month + "월 로 입력했습니다.");
				return false;
			}
			Calendar cal = Calendar.getInstance();
			cal.set(year, month - 1, 1);
			endDayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			if (day < 1 || day > endDayOfMonth) {
				System.out.println("일 입력오류 : " + day + "일 로 입력했습니다.");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

// 손님별 마스크판매리스트
	public void customerlist() {
		System.out.println("\n손님별 마스크 판매리스트...");
		String rrn;

		while (true) {
			System.out.print("검색할 손님 주민등록번호('-' 포함) (이전메뉴: n) > ");
			rrn = sc.next();
			if (rrn.equalsIgnoreCase("n")) { // 주민번호 유효성검사 메소드 호출
				return;
			}
			if (isValidRRN(rrn) == false) {
				System.out.println("다시 입력해주세요^^");
			} else {
				break;
			}
		}

		List<SaleSumDTO> list = dao.listCustomer(rrn);
		System.out.println("이름\t 판매날짜\t\t판매품목\t\t구매수량");

		for (SaleSumDTO dto : list) {
			System.out.print(dto.getcName() + "\t");
			System.out.print(dto.getSdate() + "\t");
			System.out.print(dto.getpName() + "\t");
			System.out.print(dto.getsQty() + "\n");
		}
	}

//전체 판매리스트
	private void productlist() {
		System.out.println("\n품목별 전체 판매리스트...");
		List<SaleSumDTO> list = dao.listSumProduct();
		System.out.println("품목이름\t 품목번호\t재고수량\t판매날짜");
		for (SaleSumDTO dto : list) {
			System.out.print(dto.getpName() + "\t");
			System.out.print(dto.getpNum() + "\t");
			System.out.print(dto.getsQty() + "\t");
			System.out.print(dto.getSdate() + "\n");
		}
	}

//처방

	public void managePrescription() {
		int ch;
		int result;
		String keyword;
		int pnum;
		try {
			System.out.print("1.증상목록 2.증상추가 3.증상삭제 4.뒤로가기> ");
			ch = sc.nextInt();
			if (ch == 4) {
				return;
			} else if (ch < 1 || ch > 4) {
				System.out.println("올바른 번호를 입력해 주세요...");
				return;
			}
			List<String> keywords = dao.getKeywords();
			List<ProductKeywordDTO> list = null;
			printSymtoms(keywords);
			switch (ch) {
			case 1:// 증상 목록
				if (keywords == null || keywords.size() == 0) {
					System.out.println("등록된 증상 목록이 없습니다. 증상 추가를 이용하여 등록해 주세요...");
					return;
				}
				System.out.println("등록된 증상 목록은 다음과 같습니다...");
				while (true) {
					System.out.print("키워드 번호 입력 (나가기: 0)> ");
					ch = sc.nextInt();
					if (ch == 0) {
						break;
					}
					keyword = keywords.get(ch - 1);
					list = dao.listByKeyword(keyword);
					System.out.println(keyword + " 검색 결과 ..." + "(총 " + list.size() + "건)");
					printSymtomObjects(list);
					System.out.println("============================");
					printSymtoms(keywords);
				}
				break;
			case 2:// 증상 추가
				System.out.println("기존에 등록된 증상 항목은 다음과 같습니다. ");
				System.out.print("추가할 증상(주관식) ? ");
				keyword = sc.next();
				List<ProductDTO> target = dao.searchAvailableProduct(keyword);
				while (true) {
					if (target != null && target.size() > 0) {
						printProducts(target);
					} else {
						printProducts(dao.listProduct());
					}

					System.out.print("추가할 상품번호 (나가기: 0)? ");
					pnum = sc.nextInt();
					if (pnum == 0) {
						break;
					}
					result = dao.insertKeyword(pnum, keyword);
					if (result != 0) {
						for (int i = 0; i < target.size(); i++) {
							if (target.get(i).getPnum() == pnum) {
								System.out.println("뺀다...");
								target.remove(i);
							}
						}
						System.out.println("증상이 정상적으로 등록되었습니다.");
					} else {
						System.out.println("이미 등록되었습니다만...?");
					}
				}
				break;
			case 3:// 증상 삭제
				System.out.print("삭제할 증상 (주관식) ? ");
				keyword = sc.next();
				while (true) {
					list = dao.listByKeyword(keyword);
					if (list == null || list.size() == 0) {
						break;
					}
					printSymtomObjects(list);
					System.out.print("삭제할 상품번호 (나가기: 0) ? ");
					pnum = sc.nextInt();
					if (pnum == 0) {
						break;
					}
					result = dao.deleteKeywordProduct(pnum, keyword);
					if (result != 0) {
						System.out.println("증상이 성공적으로 삭제되었습니다.");
					} else {
						System.out.println("상품번호를 정확하게 입력해 주세요.");
					}
				}
				break;
			}
		} catch (Exception e) {
			System.out.println("올바르게 입력되지 않았습니다.\n" + e.getMessage());
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
