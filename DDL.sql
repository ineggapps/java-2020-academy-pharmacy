--테이블 작성

CREATE TABLE test1(
    id VARCHAR2(30) PRIMARY KEY,
    name VARCHAR2(30) NOT NULL
);

CREATE TABLE test2(
    id VARCHAR2(30) PRIMARY KEY,
    birth VARCHAR2(30) NOT NULL,
    FOREIGN KEY(id) REFERENCES test1(id)
);

CREATE TABLE test3(
    id VARCHAR2(30) PRIMARY KEY,
    tel VARCHAR2(30) NOT NULL,
    FOREIGN KEY(id) REFERENCES test1(id)
);

select * from sale;
CREATE TABLE sale(
	sNum NUMBER PRIMARY KEY,
	pNum NUMBER NOT NULL,
	sDate DATE NOT NULL,
	sQty NUMBER NOT NULL,
	cNum NUMBER NOT NULL,
	FOREIGN KEY(cNum) REFERENCES customer(cNum),
FOREIGN KEY(pNum) REFERENCES product(pNum)	
);

CREATE TABLE product_keyword(
    pnum NUMBER,
    keyword VARCHAR(30) NOT NULL,
    CONSTRAINT PK_PRODUCT_KEYWORD PRIMARY KEY (pnum, keyword) 
);
select * from product_keyword;
-------------------------------------------------

SELECT id, name, birth, tel
FROM test1
LEFT OUTER JOIN test2 USING(id)
LEFT OUTER JOIN test3 USING(id);

desc member1;
desc member2;

select * from member1
LEFT OUTER JOIN member2 using(id);

select * from score;

select * from tab;


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
select * from user_jobs;

select * from product;
select * from input;
SELECT inum, TO_CHAR(idate, 'yyyy-mm-dd hh:mi:ss') idate, iqty FROM input order by inum desc;
exec PHARMACY_INPUT_AUTOMATICALLY();
select job, schema_user, last_Sec, next_sec from user_jobs;
EXECUTE dbms_job.REMOVE(288);
commit;
select * from product;

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

--랜덤 행 추출
SELECT pnum FROM(
    SELECT pnum FROM product
    ORDER BY DBMS_RANDOM.value()
) WHERE ROWNUM = 1;

--시퀀스

select * from seq;

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
drop sequence customer_seq;
drop sequence input_seq;
drop sequence sale_seq;
drop sequence product_seq;
create sequence product_seq
        start with 1
        increment by 1
        nomaxvalue
        nocycle
        nocache;
--

select input_seq.CURRVAL from dual;

--
desc product;
--이름    널?       유형           
------- -------- ------------ 
--PNUM  NOT NULL NUMBER(8)    
--PNAME NOT NULL VARCHAR2(30) 
--PRICE NOT NULL VARCHAR2(20) 
--STOCK NOT NULL NUMBER(8)  

desc sale;
--이름    널?       유형     
------- -------- ------ 
--SNUM  NOT NULL NUMBER 
--SDATE          DATE   
--SQTY  NOT NULL NUMBER 
--CNUM  NOT NULL NUMBER 

alter table sale modify cnum null;

desc input;
--이름    널?       유형        
------- -------- --------- 
--INUM  NOT NULL NUMBER(8) 
--PNUM  NOT NULL NUMBER(8) 
--IDATE NOT NULL DATE      
--IQTY  NOT NULL NUMBER(8) 



--누적삭제 되게끔

ALTER TABLE input
DROP CONSTRAINT FK_pnum;

alter table sale drop constraint SYS_C008759;
alter table sale drop constraint fk_pnum;

alter table sale add constraint fk_sale_pnum
foreign key (pnum) references product(pnum);

alter table input add constraint fk_pnum 
foreign key (pnum) references product(pnum)
on delete cascade;

select * from user_constraints;

drop table sale;
drop table input;
drop table customer;

alter table sale modify cnum null;
commit;
--출처: https://lbnl1027.tistory.com/entry/CASCADE-제약조건-추가 [Once in a Lifetime]
select * from(
select distinct p.pnum, pname, price, stock, keyword from product p left outer join product_keyword k on k.pnum = p.pnum
order by p.pnum
) where keyword is null or keyword !='만성피로';


SELECT pnum, pname, price, stock from product
MINUS
select distinct p.pnum, pname, price, stock from product p left outer join product_keyword k on k.pnum = p.pnum
where keyword='변비';

select * from input;
select pnum, pname, price, stock from (select distinct p.pnum, pname, price, stock, keyword from product p join product_keyword k on k.pnum = p.pnum ) WHERE nvl(keyword,' ') != '만성피로'; 
