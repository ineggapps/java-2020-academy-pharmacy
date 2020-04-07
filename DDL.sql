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
,'PHARMACY_INPUT_TEST;' -- 실행할 프로시저명
,to_date('03-04-2020 15:31:00','dd/mm/yyyy hh24:mi:ss') --실행시킬 시간 지정
,'SYSDATE+2/(1440)' --반복기간 지정
,FALSE 
);
END;
/

SELECT * FROM USER_SCHEDULER_JOBS; --등록된 job
SELECT * FROM USER_SCHEDULER_JOB_ARGS; --job의 arguments
SELECT * FROM USER_SCHEDULER_RUNNING_JOBS; --현재 running중인 job들의정보
SELECT * FROM USER_SCHEDULER_JOB_LOG; --job의 log
SELECT * FROM USER_SCHEDULER_JOB_RUN_DETAILS; --job의수행된정보및Error 정보
SELECT * FROM USER_SCHEDULER_PROGRAMS; -- 등록된 Program
SELECT * FROM USER_SCHEDULER_PROGRAM_ARGS; -- 프로그램의 매게변수
SELECT * FROM USER_SCHEDULER_SCHEDULES; --등록된 스케쥴러


drop table test purge;
create table test(
    test date
);

DECLARE
BEGIN
    DBMS_JOB.REMOVE(61);
END;
/


CREATE OR REPLACE PROCEDURE PHARMACY_INPUT_TEST
IS
BEGIN
    INSERT INTO test(test) VALUES(SYSDATE);
    COMMIT;
END;
/

exec PHARMACY_INPUT_TEST();
delete from test;
commit;
select TO_CHAR(test, 'HH24:MI:SS') from test order by test desc;

commit;

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