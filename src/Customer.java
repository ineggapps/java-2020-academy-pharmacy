import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Customer {
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	public void enter() {
		int ch;
		try {
			while(true) {
				do {
					System.out.print("1.마스크구매여부확인 2.구매 3.취소");
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
		try {
			int ch;
			System.out.print("1.마스크 2.손소독제 > ");
			ch = Integer.parseInt(br.readLine());
			System.out.println("{{{(>_<)}}} 구매가 완료되었습니다");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void identifyCustomer() {
		try {
			String rrn, name;
			System.out.print("주민번호 ? ");
			//회원정보가 등록돼 있는지 검색
			
			if(true==true) {
				//회원이 등록되지 않았으면?
				System.out.print("이름 ? ");
				name = br.readLine();
				//서버에 회원정보 등록
			}else {
				name ="홍길동";
				System.out.println("~님 안녕하세요.");
				//로직 처리 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
