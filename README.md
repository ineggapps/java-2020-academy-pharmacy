# 코로나 약국

## 1. 기획의도

JAVA, ORACLE을 이용하여 데이터를 입출력하며 기본적으로 Back-end 단에서 데이터가 어떤 방식으로 흐르는지를 살펴보기 위해 실습을 시작하였습니다. JAVA에서는 CallableStatement를 이용하여 PL/SQL의 프로시저를 호출하는 실습을 진행하였고, PreparedStatement로 SQL 문을 작성하여 직접 CRUD 작업을 시행하였습니다. 또한, ORACLE에서 데이터의 입출력과 같은 이벤트가 발생할 때마다 트리거를 이용하여 자동으로 데이터가 산출되는 기능을 구현하는 등 데이터베이스에서 제공하는 기본적인 기능과 개념을 망라하였습니다.

이러한 개념들을 최대한 적용하면서 실습을 진행할 때 가장 최적합한 주제가 무엇인지를 함께 고민했으며, 그 결과 2020년 3~4월 시점을 기준으로 COVID19 바이러스가 대유행하여 마스크와 기타 의약품을 판매하는 약국을 주제로 삼아 제작해 보게 되었습니다.

## 2. 파트너 (총 개발 5명)

- @eunijoo
- @lsh829
- 외 2명

## 3. 요구사항

- 맡은 부분

1. 마스크를 살 때에는 다음의 조건을 만족하여야 한다.
2. 인당 2매 초과하여 판매할 수 없다.
3. 재고수량이 없으면 판매할 수 없다.
4. 출생연도를 확인하여 마스크5부제에 따라 출생연도 끝자리로 구별하여 구매할 수 있는 날인지 판별할 수 있어야 한다.
5. 기존에 구매한 이력이 있는 경우에 판매 시 7일 이내에 마스크를 구매한 수량과 현재 판매하고자 하는 마스크의 수량과 합산하여 2매를 초과하면 안 된다. (합산하여 2020년 3월 9일(월)을 시작으로 7일간을 한 주로 본다.)
6. 기존에 구매한 이력이 없으면 현재 구매한 이력을 데이터베이스에 등록한다.

- @lsh829

1. 증상 관리: 약사는 손님에게 처방하여 물품을 판매할 수 있다.
2. 증상 검색: 약국에 등록된 상품 중 해당 증상에 효능이 있는 약품만을 확인할 수 있다.
3. 증상 목록: 약사는 손님들이 자주 호소하는 증상을 미리 등록하고 해당 증상에 효능이 있는 제품들을 조회할 수 있다.
4. 상품 관리: 상품번호를 입력하여 해당 증상에 효능이 있는 약품을 추가할 수 있다.
5. 상품 판매: 처방 수량과 재고수량을 고려하여 제품을 판매한다.

- @eunijoo
  [손님별 마스크판매리스트]

1. 주민번호를 입력하여 마스크 구매한 사람 이력을 조회할 수 있다.
2. 주민번호 입력 시, 자릿수에 맞춰 입력받아야한다.
3. 주민번호 입력 시, 년,월,일 유효성 검사를 한다.
4. 구매한 이력이 없을 경우 구매이력 없음을 출력해준다.
5. 조회내역은 이름, 구매날짜, 판매품목,판매수량을 확인 할 수 있다.
   [전체 판매리스트]🏣 🏥
6. 판매 이력을 조회 할 수 있다.
7. 조회내역은 품목이름,품목번호,재고수량,판매날짜를 확인 할 수 있다.

- 이\*혜
  [Product Input]

1. <insert> 새로운 제품을 등록한다.
2. <update> 등록된 제품을 수정한다.
3. 수정시 제품번호(pnum)를 입력 받아 수정한다.
4. 입고 번호가 존재하지 않으면 수정할 수 없다.
5. <delete> 등록 제품을 삭제한다.
6. 삭제시 제품 번호(pnum)를 입력 받아 삭제한다.
7. 입고 번호가 존재하지 않으면 삭제할 수 없다.  
   [Checkavailability]
8. 이름(cName)과 주민번호(rrn)을 입력하여 마스크 구매요일을 확인한다.

[CheckDate]

9. 오늘 요일을 추출 : 입력받은 주민번호 출생년도 끝자리와 오늘 요일을 비교해 구매요일이면 구매로 바로 넘어갈수 있도록 함. 구매요일이 아니면 구매 가능한 요일만 출력해준다.

- 전\*지
  [Input]

1. <inputinsert> 입고된 제품을 등록한다.
2. <inputupdate> 입고된 제품을 수정한다.
3. 수정시 입고 번호(고유번호)를 입력 받아 수정한다.
4. 입고 번호가 존재하지 않으면 수정할 수 없다.
5. 제품 수량 수정시 물품의 재고보다 작은 수량으로 수정할 수 없다.
6. <inputdelete> 입고 제품을 삭제한다.
7. 삭제시 입고 번호를 입력 받아 삭제한다.
8. 입고 번호가 존재하지 않으면 삭제할 수 없다.
9. <readInput> 제품 수정,삭제시 입고번호, 물품번호, 물품명, 입고수량, 재고를 출력한다.
10. <liststock> 입고된 제품과 현재 재고 수량을 출력 한다.

## 4. 맡은 역할

- PL/SQL

  > 트리거: 상품 입고, 혹은 고객에게 판매가 완료되면 해당 수량만큼 제품 테이블에서 마스크 등 의약품의 재고의 수치에서 가감하여 현 재고 현황을 관리합니다.

  > 프로시저: 상품 입고, 입고사항 수정, 입고사항 삭제, 물품 랜덤하게 자동입고(1~10개 무작위 수량) 등의 프로시저 작성
  >
  > > 마스크 구매기간 산정 (월요일~그 다음 주 일요일 7일간 구매한 수량 계산)

- JAVA

  > CallableStatement: 마스크 구매 프로시저 호출, 일반 의약품 구매 프로시저 호출

  > PreparedStatemet: 증상 검색, 고객정보 저장

  > 프로젝트 결합하여 git저장소에 업로드
  > 버그 최종 점검

## 5. DDL

계정 생성

```sql
ALTER SESSION SET "_ORACLE_SCRIPT" = true;
--약국 DB계정 생성
CREATE USER pharmacist IDENTIFIED BY "java!@#$%"
    DEFAULT TABLESPACE USERS
    TEMPORARY TABLESPACE TEMP
    QUOTA UNLIMITED ON USERS;
--DB접근권한 부여 (프로시저 및 트리거까지)
GRANT CONNECT, RESOURCE, CREATE TRIGGER,
CREATE PROCEDURE TO pharmacist;
```

테이블 생성

```sql
<물품 정보>
CREATE TABLE product (
	pNum NUMBER(8) PRIMARY KEY  --품목번호 (기본키)
	,pName VARCHAR2(30) NOT NULL --품목명
	,price VARCHAR2(20) NOT NULL --단가
	,stock NUMBER(8) NOT NULL --재고 수량
 );

<물품 키워드>
CREATE TABLE product_keyword(
    pnum NUMBER,
    keyword VARCHAR(30) NOT NULL,
    CONSTRAINT PK_PRODUCT_KEYWORD PRIMARY KEY (pnum, keyword)
);


 <입고현황 >
CREATE TABLE input (
	iNum NUMBER(8) PRIMARY KEY --입고번호
	,pNum NUMBER(8) NOT NULL --품목번호
	,iDate DATE NOT NULL --입고일자
	,iQty NUMBER(8) NOT NULL --입고 수량
	,CONSTRAINT fk_pnum FOREIGN KEY(pNum) REFERENCES product(pNum) -- product table의 품목번호(pNum) 참조키
);

<고객님>
CREATE TABLE customer(
	cNum NUMBER PRIMARY KEY,
	cName VARCHAR2(30) NOT NULL,
	rrn VARCHAR2(14) UNIQUE NOT NULL
);

<판매내역>
CREATE TABLE sale(
	sNum NUMBER PRIMARY KEY,
	pNum NUMBER NOT NULL,
	sDate DATE NOT NULL,
	sQty NUMBER NOT NULL,
	cNum NUMBER NOT NULL,
	FOREIGN KEY(cNum) REFERENCES customer(cNum),
FOREIGN KEY(pNum) REFERENCES product(pNum)
);


--시퀀스
create sequence customer_seq
        start with 1
        increment by 1
        nomaxvalue
        nocycle
        nocache;

create sequence sale_seq
        start with 1
        increment by 1
        nomaxvalue
        nocycle
        nocache;

create sequence input_seq
        start with 1000
        increment by 1
        nomaxvalue
        nocycle
        nocache;
create sequence product_seq
        start with 1
        increment by 1
        nomaxvalue
        nocycle
        nocache;

--
drop sequence customer_seq;
drop sequence input_seq;
drop sequence sale_seq;
drop sequence product_seq;


```

## 6. 프로시저 및 트리거 등

```sql
CREATE OR REPLACE PROCEDURE insertSaleForMask(
    productNum sale.pnum%TYPE,
    pDate sale.sdate%TYPE,
    pQTY sale.sqty%TYPE,
    pRRN customer.rrn%TYPE,
    pCName customer.cname%TYPE
)
IS
    stock NUMBER;
    group_a NUMBER;--1~5년생
    group_b NUMBER;--6~0년생

    customer_in_year NUMBER; --고객 끝자리
    today NUMBER;

    start_day DATE; --5부제 시작일
    end_day DATE; --5부제 종료일
    soldCount NUMBER; --해당 기간 동안 산 마스크의 개수

    customerNum NUMBER; --고객번호 (rrn으로 찾기)
    customerCount NUMBER; --고객 데이터베이스에서 조회
    saleData sale%rowtype;
BEGIN
    --2매 초과하는 수량을 주문하였는지 확인
    IF pQTY > 2 THEN --2매 이상 구매를 요청했는지 확인하기
        raise_application_error(-20031,'일주일에 인당 2매까지만 구매가 가능합니다. ');
    END IF;
    SELECT stock INTO stock FROM product
    WHERE pnum=productNum;
-- raise_application_error(-20031,stock || '<-재고개수');
    IF stock < pQTY THEN -- 재고가 있는지 확인
        raise_application_error(-20041,'재고가 부족하여 판매할 수 없습니다. ' || '(현재 재고: ' || stock || '개, 구매수량: ' || pQTY || '개)');
    END IF;
    --TODO: 마스크5부제에 의하여 오늘이 구매일이 맞는지 확인
    SELECT to_char(pDate, 'd')-1 INTO today FROM dual; --오늘의 요일 날짜함수가 (1:일요일~7:토요일)라면 결괏값은 (0:일요일, 1:월요일, 5:금요일, 6:토요일)
    group_a := today; --1~5년생 구하기
    group_b := MOD(today+5,10); --6~0년생 구하기
--    DBMS_OUTPUT.PUT_LINE(group_a || ' ' || group_b || ' ' || today);
    SELECT SUBSTR(pRRN,2,1) INTO customer_in_year FROM dual; -- 구매고객의 연도 끝자리 구하기
    IF today > 1 OR today >5 THEN --평일이면 5부제 검사
        IF customer_in_year != group_a AND customer_in_year != group_b THEN
                    raise_application_error(-20011,'오늘 구매대상이 아닙니다.');
        END IF;
    END IF;
    --회원여부 확인
    --회원이 아니면 회원등록을 한 후 cnum을 가져오고 회원이면 cnum을 가져온다.
    SELECT count(cnum) INTO customerCount FROM customer WHERE rrn=pRRN;
    IF customerCount = 0 THEN --구매한 이력이 없었던 경우
        INSERT INTO customer(cnum, cname, rrn) VALUES (customer_seq.NEXTVAL, pCName,pRRN);
        COMMIT;
        SELECT cnum INTO customerNum FROM customer WHERE rrn=pRRN;
    ELSE --구매한 이력이 있었던 경우
         SELECT cnum INTO customerNum FROM customer WHERE rrn=pRRN;
        --일주일 이내에 구매하였는지 확인
        ---참고로 마스크 5부제는 2020년 3월 9일 (월요일)부터 도입되었다.
        SELECT NEXT_DAY(SYSDATE-8,1)+1 INTO start_day FROM dual;
        SELECT NEXT_DAY(SYSDATE-8,1)+7 INTO end_day FROM dual;
        SELECT nvl(sum(sQTY),0) INTO soldCount from (
        SELECT snum,sqty FROM sale WHERE cnum=customerNum AND
                (SYSDATE >=start_day
                AND SYSDATE <=end_day
        ));
    --    raise_application_error(-20021,customerNum || ' 디버깅... ' || soldCount || '개 씩이나 구매하셨습니다..');
        DBMS_OUTPUT.PUT_LINE(start_day || ' ' || end_day || ' ' || soldCount);
        IF soldCount+pQTY > 2 THEN --이번주에 이미 구매한 수량이 2개 이상이면 살 수 없다
            raise_application_error(-20021,'주 최대 2매까지만 구매가 가능합니다. 당신은 욕심쟁이\(￣?￣*\))');
        END IF;
    END IF;
    --조건에 만족하면 이제 추가
    INSERT INTO sale(snum, pnum, sdate, sqty, cnum)
    VALUES (sale_seq.NEXTVAL,productNum, pDate, pQTY, customerNum);
    COMMIT;
END;
/
```

## 7. 샘플데이터

```sql
insert into customer values (customer_seq.NEXTVAL,'홍길동','011009-3012345');
insert into product(pnum, pname, price, stock) values(product_seq.NEXTVAL, '공적 마스크 (KF94)', 1500, 0);
insert into product(pnum, pname, price, stock) values(product_seq.NEXTVAL, '손소독제', 1500, 0);
insert into product(pnum, pname, price, stock) values(product_seq.NEXTVAL, '타이레놀', 1500, 0);
insert into product(pnum, pname, price, stock) values(product_seq.NEXTVAL, '게보린', 1500, 0);
insert into product(pnum, pname, price, stock) values(product_seq.NEXTVAL, '우루사', 3300, 0);
insert into product(pnum, pname, price, stock) values(product_seq.NEXTVAL, '노스카나겔', 25000, 0);
insert into product(pnum, pname, price, stock) values(product_seq.NEXTVAL, '벤트락스겔', 25000, 0);
insert into product(pnum, pname, price, stock) values(product_seq.NEXTVAL, '더마틱스울트라', 50000, 0);
insert into product(pnum, pname, price, stock) values(product_seq.NEXTVAL, '가스활명수', 700, 0);
insert into product(pnum, pname, price, stock) values(product_seq.NEXTVAL, '광동 생록천', 1300, 0);
insert into product(pnum, pname, price, stock) values(product_seq.NEXTVAL, '위청수', 2000, 0);
insert into product_keyword(pnum, keyword) values(2,'위생');
insert into product_keyword(pnum, keyword) values(3,'두통');
insert into product_keyword(pnum, keyword) values(4,'두통');
insert into product_keyword(pnum, keyword) values(6,'여드름');
insert into product_keyword(pnum, keyword) values(7,'여드름');
insert into product_keyword(pnum, keyword) values(8,'여드름');

--입고
CREATE OR REPLACE PROCEDURE insertInput(
    pNum input.pnum%TYPE,
    pDate input.idate%TYPE,
    pQTY input.iqty%TYPE,
    iNum out input.inum%TYPE
)
IS
BEGIN
    INSERT INTO input(inum, pnum, idate, iqty)
    VALUES (input_seq.NEXTVAL, pnum, pdate, pqty);
    COMMIT;
    inum := product_seq.CURRVAL;
END;
/

--테스트용 프로시저
CREATE OR REPLACE PROCEDURE insertInputWithoutOut(
    pNum input.pnum%TYPE,
    pDate input.idate%TYPE,
    pQTY input.iqty%TYPE
)
IS
BEGIN
    INSERT INTO input(inum, pnum, idate, iqty)
    VALUES (input_seq.NEXTVAL, pnum, pdate, pqty);
    COMMIT;
END;
/
EXEC insertInputWithoutOut(4,'2020-04-06',55);

select * from all_triggers WHERE owner='PHARMACIST';
CREATE OR REPLACE TRIGGER insertTrigger_Input
AFTER INSERT ON input
FOR EACH ROW
BEGIN
    UPDATE product SET stock=stock+:NEW.IQTY
    WHERE  pNum=:NEW.pNum;
END;
/

--입고수정
CREATE OR REPLACE PROCEDURE updateInput(
    pInputNum input.inum%TYPE,
--    pProductNum input.pnum%TYPE,
    pDate input.idate%TYPE,
    pQTY input.iqty%TYPE
)
IS
    pnum NUMBER;
BEGIN
    SELECT pnum INTO pnum FROM input WHERE inum=pInputNum;
    UPDATE input SET pnum=pnum, idate=pDate, iqty=pQty
    WHERE iNum=pInputNum;
    COMMIT;
END;
/

CREATE OR REPLACE TRIGGER updateTrigger_Input
AFTER UPDATE ON input
FOR EACH ROW
BEGIN
    UPDATE product SET stock=stock- :OLD.iQTY + :NEW.iQTY
    WHERE pnum=:NEW.pnum;
END;
/

EXEC updateInput(9,1,'2020-04-01',10);
select * from product;
select * from input;

--입고 삭제
CREATE OR REPLACE PROCEDURE deleteInput(
    pInputNum NUMBER
)
IS
BEGIN
    DELETE FROM input
    WHERE iNum=pInputNum;
    COMMIT;
END;
/
select * from input where inum=13;
select * from input;
delete from input where inum=1001;
commit;

CREATE OR REPLACE TRIGGER deleteTrigger_Input
AFTER DELETE ON input
FOR EACH ROW
BEGIN
    UPDATE product SET stock = stock - :OLD.iQTY
    WHERE pNum = :OLD.pNum;
END;
/

CREATE OR REPLACE PROCEDURE deleteSale(
  pSnum NUMBER
)
IS
BEGIN
    DELETE FROM sale
    WHERE snum=pSnum;
    COMMIT;
END;
/

CREATE OR REPLACE TRIGGER deleteTrigger_sale
AFTER DELETE ON sale
FOR EACH ROW
BEGIN
    UPDATE product SET stock=stock+:OLD.SQTY
    WHERE pnum=:OLD.pnum;
END;
/

EXEC deleteSale(1);


--테스트용
--마스크 5부제 중첩쿼리 연습
SELECT SUBSTR((SELECT RRN FROM customer WHERE cnum=3),2,1) from dual; --고객 생년 끝자리

--마스크 5부제 (일~월)이 아니라 (월~ 다음주 일) 가져오기
WITH TB AS (
    SELECT TO_DATE('2020-03-11') TODAY  FROM dual
    UNION ALL
    SELECT TO_DATE('2020-03-19') TODAY FROM dual
    UNION ALL
    SELECT TO_DATE('2020-03-24') TODAY FROM dual
    UNION ALL
    SELECT TO_DATE('2020-03-31') TODAY FROM dual
)
select TODAY 오늘, NEXT_DAY(today-8,1)+1 "주 시작일자", NEXT_DAY(today-8,1)+7 "주 마지막 일자" from tb; --주의 시작과 끝
--마스크 5부제 해당 기간 동안 샀는지 가져오기
select * from customer;
SELECT nvl(sum(sQTY),0) sum from (
SELECT * FROM sale WHERE cnum=13 AND
        (SYSDATE >= NEXT_DAY(SYSDATE-8,1)+1
        AND SYSDATE <= NEXT_DAY(SYSDATE-8,7)+7
));--한주간 사간 마스크
select * from sale where cnum=1;
CREATE OR REPLACE TRIGGER insertTrigger_sale
AFTER INSERT ON sale
FOR EACH ROW
BEGIN
    UPDATE product SET stock = stock - :NEW.sQTY
    WHERE pnum=:NEW.pnum;
END;
/


--물품자동입고
CREATE OR REPLACE PROCEDURE PHARMACY_INPUT_AUTOMATICALLY
IS
productNum NUMBER;
BEGIN
    SELECT pnum INTO productNum FROM(
        SELECT pnum FROM product
--        WHERE stock<10
        ORDER BY DBMS_RANDOM.value()
    ) WHERE ROWNUM = 1;
    insertInputWithoutOut(productNum, SYSDATE, DBMS_RANDOM.value(1,10));
    COMMIT;
END;
/

DECLARE
X NUMBER;
BEGIN
DBMS_JOB.SUBMIT
(
X -- 잡등록 ID
,'PHARMACY_INPUT_AUTOMATICALLY;' -- 실행할 프로시저명
,SYSDATE+(1/1440) --실행시킬 시간 지정
,'SYSDATE+(10/(1440))' --반복기간 지정
,FALSE
);
END;
/

--랜덤 행 추출
SELECT pnum FROM(
    SELECT pnum FROM product
    ORDER BY DBMS_RANDOM.value()
) WHERE ROWNUM = 1;
```
