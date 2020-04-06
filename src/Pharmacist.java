
import java.util.Scanner;

public class Pharmacist {
	private Scanner sc = new Scanner(System.in);
	private ProductDAO dao = new ProductDAOImpl();
	/*
	 *private PharmacistDAO dao = new PharmacistDAOImpl(); 
	 *		> 궁금한 점. 
	 *				-  PharmacistDAO를 인터페이스로 생성하고, 그것을 구현한것이 PharmacistDAOImpl인데,
	 *				-  private PharmacistDAO dao = new PharmacistDAOImpl(); 이렇게 인스턴스화 가능?
	 */
	
	
	public void pharmacistManage() {
		int ch;
		while(true) {
			System.out.println("\n 약사");
			do {
				System.out.println("1.재고관리  2. 판매현황  3.처방   4.로그아웃 =>");
				ch = sc.nextInt();
			} while (ch<1 || ch>4);
			
			if(ch==4) break;
			
			switch (ch) {
			case 1: inventory();break;
			case 2: sales();break;
			case 3: prescription();break;

			}
		}
	}
	
	public void inventory() {
		int ch;
		
		while(true) {
			System.out.println("\n 약사");
			do {
				System.out.println("1.입고등록 2.제품수정 3.제품삭제  4.남은재고안내 5.종료=>");
				ch = sc.nextInt();
			} while (ch<1 || ch>5);
			
			if(ch==5) break;
			
			switch (ch) {
			case 1: insert();break;
			case 2: update();break;
			case 3: delete();break;
			case 4: listproduct();break;
			}
		}
		
	}
	
	public void insert() {
		System.out.println("\n 입고 등록..."); //마스크와 손소독제 입고 등록 
		
		ProductDTO dto = new ProductDTO();
		
		
		System.out.println();
		
	}
	
	public void update() {
		
	}
	
	public void delete() {
		
	}
	
	public void listproduct() {
		
	}
	
	
	public void sales() {
		int ch;
		
		while(true) {
			System.out.println("\n 약사");
			do {
				System.out.println("1.손님별 판매현황 2.품목별 판매리스트  3.총 판매실적 4.종료=>");
				ch = sc.nextInt();
			} while (ch<1 || ch>4);
			
			if(ch==4) break;
			
			switch (ch) {
			case 1: customerlist();break;
			case 2: productlist();break;
			case 3: totalsales();break;
			
			}
		}
	}
	


	public void customerlist() {
		
	}
	
	public void totalsales() {
		
	}
	
	private void productlist() {
		
		
	}
	
	public void prescription() {
		System.out.println("\n 증상 입력 >..."); 
	}


}

