select * from customer;
insert into customer values (customer_seq.NEXTVAL,'홍길동','011009-3012345');
commit;
--프로시저
SELECT * FROM product;
INSERT INTO product(pnum, pname, price, stock) VALUES(product_seq.NEXTVAL, '공적 마스크 KF94', 1500, 0);
INSERT INTO product(pnum, pname, price, stock) VALUES(product_seq.NEXTVAL, '손소독제', 9900, 0);
commit;
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
    VALUES (product_seq.NEXTVAL, pnum, pdate, pqty);
    COMMIT;
    inum := product_seq.CURRVAL;
END;
/


CREATE OR REPLACE PROCEDURE insertInputWithoutOut(
    pNum input.pnum%TYPE,
    pDate input.idate%TYPE,
    pQTY input.iqty%TYPE
)
IS
BEGIN
    INSERT INTO input(inum, pnum, idate, iqty)
    VALUES (product_seq.NEXTVAL, pnum, pdate, pqty);
    COMMIT;
END;
/
EXEC insertInputWithoutOut(4,'2020-04-06',55);

select * from product;
select  * from input;

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

CREATE OR REPLACE TRIGGER deleteTrigger_Input
AFTER DELETE ON input
FOR EACH ROW
BEGIN   
    UPDATE product SET stock = stock - :OLD.iQTY
    WHERE pNum = :OLD.pNum;
END;
/

EXEC deleteInput(6);
select * from product;
select * from input;


CREATE OR REPLACE PROCEDURE checkPurchaseMask(
    pDate IN OUT sale.sdate%TYPE,
    pRRN IN VARCHAR,
    qty OUT NUMBER
)
IS
    group_a NUMBER;--1~5년생
    group_b NUMBER;--6~0년생
    
    customer_in_year NUMBER; --고객 끝자리
    today NUMBER;
    
    start_day DATE; --5부제 시작일
    end_day DATE; --5부제 종료일
    soldCount NUMBER; --해당 기간 동안 산 마스크의 개수
BEGIN
    IF pDate IS NULL THEN
        pDate := SYSDATE;
    END IF;
    --TODO: 마스크5부제에 의하여 오늘이 구매일이 맞는지 확인
    qty := 0;
    SELECT to_char(pDate, 'd')-1 INTO today FROM dual; --오늘의 요일 날짜함수가 (1:일요일~7:토요일)라면 결괏값은 (0:일요일, 1:월요일, 5:금요일, 6:토요일)
    group_a := today; --1~5년생 구하기
    group_b := MOD(today+5,10); --6~0년생 구하기
    DBMS_OUTPUT.PUT_LINE(group_a || ' ' || group_b || ' ' || today);
    SELECT SUBSTR(pRRN,2,1) 
    INTO customer_in_year
    FROM dual; -- 구매고객의 연도 끝자리 구하기
    IF today > 1 OR today >5 THEN --평일이면 5부제 검사
        IF customer_in_year != group_a AND customer_in_year != group_b THEN
            raise_application_error(-20011,'오늘 구매대상이 아닙니다.');
        END IF;
    END IF;
    --일주일 이내에 구매하였는지 확인
    ---참고로 마스크 5부제는 2020년 3월 9일 (월요일)부터 도입되었다.
    SELECT NEXT_DAY(SYSDATE-8,1)+1 INTO start_day FROM dual;
    SELECT NEXT_DAY(SYSDATE-8,1)+7 INTO end_day FROM dual;
    SELECT nvl(sum(sQTY),0) INTO soldCount from (
    SELECT snum,sqty FROM sale 
    JOIN customer c on sale.cnum = c.cnum 
    WHERE rrn=pRRN AND 
            (SYSDATE >=start_day
            AND SYSDATE <=end_day
    ));
--    SELECT nvl(sum(sQTY),0) INTO soldCount FROM sale
--    JOIN customer on sale.cnum = customer.cnum
--    WHERE rrn=pRRN AND (sDate >= start_day AND sDate <= end_day);
    DBMS_OUTPUT.PUT_LINE(start_day || ' ' || end_day || ' ' || soldCount);
    IF soldCount >= 2 THEN --이번주에 이미 구매한 수량이 2개 이상이면 살 수 없다 
        raise_application_error(-20021,'이미 이번주에 ' || soldCount || '개 씩이나 구매하셨습니다..');
    ELSE
        qty := 2-soldCount;
    END IF;
END;
/
exec checkPurchaseMask('2020-04-06','910606-2222222',1);

--테스트
SELECT SUBSTR(TO_DATE('911009-1234567'),2,1) FROM dual; -- 구매고객의 연도 끝자리 구하기
select * from customer;
--판매
CREATE OR REPLACE PROCEDURE insertSale(
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
    SELECT stock INTO stock FROM product
    WHERE pnum=productNum;
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
    IF customerCount = 0 THEN
        INSERT INTO customer(cnum, cname, rrn) VALUES (customer_seq.NEXTVAL, pCName,pRRN);
        COMMIT;
    END IF;
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
    IF soldCount >= 2 THEN --이번주에 이미 구매한 수량이 2개 이상이면 살 수 없다 
        raise_application_error(-20021,'이미 이번주에 ' || soldCount || '개 씩이나 구매하셨습니다..');
    END IF;
    --월요일(1,6) 화요일(2,7) 수요일(3,8) 목요일(4,9) 금요일(5,0), 토일 (사지 않은 인원만)   
    IF pQTY >= 2 THEN --2매 이상 구매하였는지 확인하기
        raise_application_error(-20031,'일주일에 인당 2매까지만 구매가 가능합니다. ');
    END IF;
    IF stock-pQTY < 0 THEN -- 재고가 있는지 확인
        raise_application_error(-20041,'재고가 부족하여 판매할 수 없습니다. ' || '(현재 재고: ' || stock || '개, 구매수량: ' || pQTY || '개)');
    END IF;
    --조건에 만족하면 이제 추가
    INSERT INTO sale(snum, pnum, sdate, sqty, cnum)
    VALUES (sale_seq.NEXTVAL,productNum, pDate, pQTY, customerNum);
    COMMIT;
END;
/
EXEC insertSale(1,'2020-04-04',1,1);
select * from sale;

select mod(to_char(sysdate,'d')-1,7) from dual;

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
delete from sale;
delete from input;
delete from customer;
delete from product;
commit;
select * from sale;
select * from customer;
update customer set rrn='010109-4012345' where cnum=2;
commit;
select * from product;
select * from input;

alter table product modify price number;
select * from product;
select * from sale full outer join customer on sale.cnum=customer.cnum;
select * from input;

delete from product;
delete from sale;
delete from input;
delete from customer;
rollback;
commit;
select * from input;

desc product;
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

select * from product;
update product set pname='벤트락스겔' where pnum=14;
commit;

select * from product;
insert into product_keyword(pnum, keyword) values(2,'위생');
insert into product_keyword(pnum, keyword) values(3,'두통');
insert into product_keyword(pnum, keyword) values(4,'두통');
insert into product_keyword(pnum, keyword) values(14,'여드름');
insert into product_keyword(pnum, keyword) values(15,'여드름');
insert into product_keyword(pnum, keyword) values(16,'여드름');

commit;

select p.pnum, pname, price, stock from product p
JOIN product_keyword k ON k.pnum = p.pnum
WHERE keyword='두통';

--증상 목록 (중복제거)
select distinct keyword from product_keyword;
--증상별 품목
select keyword, p.pnum, pname, price, stock from product_keyword k 
JOIN product p ON k.pnum = p.pnum
GROUP BY keyword, (p.pnum, pname, price, stock)
order by keyword, stock desc;
select * from customer;
select * from sale s full outer join customer c on c.cnum = s.cnum;

select * from sale s 
join customer c on s.cnum = c.cnum
order by sdate desc;
