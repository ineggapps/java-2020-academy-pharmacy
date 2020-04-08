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
,SYSDATE --실행시킬 시간 지정
,'SYSDATE+60/(1440)' --반복기간 지정
,FALSE 
);
END;
/
SELECT inum, TO_CHAR(idate, 'yyyy-mm-dd hh:mi:ss') idate, iqty FROM input order by inum desc;
exec PHARMACY_INPUT_AUTOMATICALLY();
select * from user_jobs;
EXECUTE dbms_job.REMOVE(147);
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
        start with 1
        increment by 1
        nomaxvalue
        nocycle
        nocache;

drop sequence product_seq;
create sequence product_seq
        start with 1
        increment by 1
        nomaxvalue
        nocycle
        nocache;
--
        
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