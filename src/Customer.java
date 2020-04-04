import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;

public class Customer {
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	CustomerDAO dao = new CustomerDAOImpl();
	public void enter() {
		int ch;
		try {
			while(true) {
				do {
					System.out.print("1.마스크구매여부확인 2.구매 3.취소 > ");
					ch =  Integer.parseInt(br.readLine());					
				}while(ch<1||ch>3);
				if(ch==3) {
					break;
				}
				switch(ch) {
				case 1://마스크구매여부확인
					checkPurchase();
					break;
				case 2://구매
					purchase();
					break;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void checkPurchase() {// 구매여부 확인
		try {
			String rrn;
			System.out.print("주민번호 ? ");
			rrn = br.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void purchase() {
		int result = 0;
		try {
			int ch;
			CustomerDTO dto = identifyCustomer();
			System.out.print("1.마스크 2.손소독제 > ");
			ch = Integer.parseInt(br.readLine());
			switch(ch) {
			case 1:
				result = dao.insertSale(ch, "2020-04-05", 2, dto.getcNum());
				break;
			case 2:break;
			}
			if(result>=1) {				
				System.out.println("{{{(>_<)}}} 구매가 완료되었습니다");
			}else {
				System.out.println("오류로 인해 구매에 실패하였습니다...");
			}
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public CustomerDTO identifyCustomer() {
		CustomerDTO dto = null;
		try {
			String rrn, name;
			System.out.print("주민번호 ? ");
			//회원정보가 등록돼 있는지 검색
			rrn = br.readLine();
			if(rrn==null || rrn.length()==0) {
				rrn = "011009-3012345";
			}
			dto = dao.readCustomer(rrn);
			if(dto==null) {
				//회원이 등록되지 않았으면?
				System.out.print("이름 ? ");
				name = br.readLine();
				//서버에 회원정보 등록
			}else {
				name = dto.getcName();
				System.out.println(name + "님 안녕하세요.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}
}
