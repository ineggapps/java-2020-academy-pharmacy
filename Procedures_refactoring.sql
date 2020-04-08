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
    IF stock-pQTY < 0 THEN -- 재고가 있는지 확인
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
        IF soldCount+pQTY >= 2 THEN --이번주에 이미 구매한 수량이 2개 이상이면 살 수 없다 
            raise_application_error(-20021,'주 최대 2매까지만 구매가 가능합니다. 당신은 욕심쟁이\(￣?￣*\))');
        END IF;
    END IF;  
    --조건에 만족하면 이제 추가
    INSERT INTO sale(snum, pnum, sdate, sqty, cnum)
    VALUES (sale_seq.NEXTVAL,productNum, pDate, pQTY, customerNum);
    COMMIT;
END;
/

CREATE OR REPLACE PROCEDURE insertSaleItem(
    pNum sale.pnum%TYPE,
    pQTY sale.sqty%TYPE
)
IS
    stock NUMBER;
BEGIN
    --재고가 있는지 확인하기
    SELECT stock INTO stock FROM product WHERE pnum=pNum and rownum=1;
--    raise_application_error(-20041,'디버그(⊙x⊙;) ' || stock || pnum );
-- raise_application_error(-20031,stock || '<-재고개수');
    IF stock-pQTY < 0 THEN -- 재고가 있는지 확인
        raise_application_error(-20041,'재고가 부족하여 판매할 수 없습니다. (⊙x⊙;) ' || '(현재 재고: ' || stock || '개, 구매수량: ' || pQTY || '개)');
    END IF;
    --구매
    INSERT INTO sale(snum, pnum, sdate, sqty)
    VALUES (sale_seq.NEXTVAL,pNum, SYSDATE, pQTY);
    COMMIT;
END;
/

select * from sale join customer on customer.cnum = sale.cnum;
select * from product;
SELECT stock FROM product where pnum=2;

--검산
SELECT * FROM sale JOIN customer ON sale.cnum = customer.cnum;