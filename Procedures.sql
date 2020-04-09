select * from customer;
insert into customer values (customer_seq.NEXTVAL,'ȫ�浿','011009-3012345');
commit;
--���ν���
SELECT * FROM product;
INSERT INTO product(pnum, pname, price, stock) VALUES(product_seq.NEXTVAL, '���� ����ũ KF94', 1500, 0);
INSERT INTO product(pnum, pname, price, stock) VALUES(product_seq.NEXTVAL, '�ռҵ���', 9900, 0);
commit;
--�԰�
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

--�԰����
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

--�԰� ����
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
    group_a NUMBER;--1~5���
    group_b NUMBER;--6~0���
    
    customer_in_year NUMBER; --�� ���ڸ�
    today NUMBER;
    
    start_day DATE; --5���� ������
    end_day DATE; --5���� ������
    soldCount NUMBER; --�ش� �Ⱓ ���� �� ����ũ�� ����
BEGIN
    IF pDate IS NULL THEN
        pDate := SYSDATE;
    END IF;
    --TODO: ����ũ5������ ���Ͽ� ������ �������� �´��� Ȯ��
    qty := 0;
    SELECT to_char(pDate, 'd')-1 INTO today FROM dual; --������ ���� ��¥�Լ��� (1:�Ͽ���~7:�����)��� �ᱣ���� (0:�Ͽ���, 1:������, 5:�ݿ���, 6:�����)
    group_a := today; --1~5��� ���ϱ�
    group_b := MOD(today+5,10); --6~0��� ���ϱ�
    DBMS_OUTPUT.PUT_LINE(group_a || ' ' || group_b || ' ' || today);
    SELECT SUBSTR(pRRN,2,1) 
    INTO customer_in_year
    FROM dual; -- ���Ű��� ���� ���ڸ� ���ϱ�
    IF today > 1 OR today >5 THEN --�����̸� 5���� �˻�
        IF customer_in_year != group_a AND customer_in_year != group_b THEN
            raise_application_error(-20011,'���� ���Ŵ���� �ƴմϴ�.');
        END IF;
    END IF;
    --������ �̳��� �����Ͽ����� Ȯ��
    ---����� ����ũ 5������ 2020�� 3�� 9�� (������)���� ���ԵǾ���.
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
    IF soldCount >= 2 THEN --�̹��ֿ� �̹� ������ ������ 2�� �̻��̸� �� �� ���� 
        raise_application_error(-20021,'�̹� �̹��ֿ� ' || soldCount || '�� ���̳� �����ϼ̽��ϴ�..');
    ELSE
        qty := 2-soldCount;
    END IF;
END;
/
exec checkPurchaseMask('2020-04-06','910606-2222222',1);

--�׽�Ʈ
SELECT SUBSTR(TO_DATE('911009-1234567'),2,1) FROM dual; -- ���Ű��� ���� ���ڸ� ���ϱ�
select * from customer;
--�Ǹ�
CREATE OR REPLACE PROCEDURE insertSale(
    productNum sale.pnum%TYPE,
    pDate sale.sdate%TYPE,
    pQTY sale.sqty%TYPE,
    pRRN customer.rrn%TYPE,
    pCName customer.cname%TYPE
)
IS
    stock NUMBER;
    group_a NUMBER;--1~5���
    group_b NUMBER;--6~0���
    
    customer_in_year NUMBER; --�� ���ڸ�
    today NUMBER;
    
    start_day DATE; --5���� ������
    end_day DATE; --5���� ������
    soldCount NUMBER; --�ش� �Ⱓ ���� �� ����ũ�� ����
    
    customerNum NUMBER; --����ȣ (rrn���� ã��)
    customerCount NUMBER; --�� �����ͺ��̽����� ��ȸ
    saleData sale%rowtype;
BEGIN
    SELECT stock INTO stock FROM product
    WHERE pnum=productNum;
    --TODO: ����ũ5������ ���Ͽ� ������ �������� �´��� Ȯ��
    SELECT to_char(pDate, 'd')-1 INTO today FROM dual; --������ ���� ��¥�Լ��� (1:�Ͽ���~7:�����)��� �ᱣ���� (0:�Ͽ���, 1:������, 5:�ݿ���, 6:�����)
    group_a := today; --1~5��� ���ϱ�
    group_b := MOD(today+5,10); --6~0��� ���ϱ�
--    DBMS_OUTPUT.PUT_LINE(group_a || ' ' || group_b || ' ' || today);
    SELECT SUBSTR(pRRN,2,1) INTO customer_in_year FROM dual; -- ���Ű��� ���� ���ڸ� ���ϱ�
    IF today > 1 OR today >5 THEN --�����̸� 5���� �˻�
        IF customer_in_year != group_a AND customer_in_year != group_b THEN
                    raise_application_error(-20011,'���� ���Ŵ���� �ƴմϴ�.');
        END IF;
    END IF;
    --ȸ������ Ȯ��
    --ȸ���� �ƴϸ� ȸ������� �� �� cnum�� �������� ȸ���̸� cnum�� �����´�.
    SELECT count(cnum) INTO customerCount FROM customer WHERE rrn=pRRN;
    IF customerCount = 0 THEN
        INSERT INTO customer(cnum, cname, rrn) VALUES (customer_seq.NEXTVAL, pCName,pRRN);
        COMMIT;
    END IF;
    SELECT cnum INTO customerNum FROM customer WHERE rrn=pRRN;

    --������ �̳��� �����Ͽ����� Ȯ��
    ---����� ����ũ 5������ 2020�� 3�� 9�� (������)���� ���ԵǾ���.
    SELECT NEXT_DAY(SYSDATE-8,1)+1 INTO start_day FROM dual;
    SELECT NEXT_DAY(SYSDATE-8,1)+7 INTO end_day FROM dual;
    SELECT nvl(sum(sQTY),0) INTO soldCount from (
    SELECT snum,sqty FROM sale WHERE cnum=customerNum AND 
            (SYSDATE >=start_day
            AND SYSDATE <=end_day
    ));
--    raise_application_error(-20021,customerNum || ' �����... ' || soldCount || '�� ���̳� �����ϼ̽��ϴ�..');
    DBMS_OUTPUT.PUT_LINE(start_day || ' ' || end_day || ' ' || soldCount);
    IF soldCount >= 2 THEN --�̹��ֿ� �̹� ������ ������ 2�� �̻��̸� �� �� ���� 
        raise_application_error(-20021,'�̹� �̹��ֿ� ' || soldCount || '�� ���̳� �����ϼ̽��ϴ�..');
    END IF;
    --������(1,6) ȭ����(2,7) ������(3,8) �����(4,9) �ݿ���(5,0), ���� (���� ���� �ο���)   
    IF pQTY >= 2 THEN --2�� �̻� �����Ͽ����� Ȯ���ϱ�
        raise_application_error(-20031,'�����Ͽ� �δ� 2�ű����� ���Ű� �����մϴ�. ');
    END IF;
    IF stock-pQTY < 0 THEN -- ��� �ִ��� Ȯ��
        raise_application_error(-20041,'��� �����Ͽ� �Ǹ��� �� �����ϴ�. ' || '(���� ���: ' || stock || '��, ���ż���: ' || pQTY || '��)');
    END IF;
    --���ǿ� �����ϸ� ���� �߰�
    INSERT INTO sale(snum, pnum, sdate, sqty, cnum)
    VALUES (sale_seq.NEXTVAL,productNum, pDate, pQTY, customerNum);
    COMMIT;
END;
/
EXEC insertSale(1,'2020-04-04',1,1);
select * from sale;

select mod(to_char(sysdate,'d')-1,7) from dual;

--����ũ 5���� ��ø���� ����
SELECT SUBSTR((SELECT RRN FROM customer WHERE cnum=3),2,1) from dual; --�� ���� ���ڸ�

--����ũ 5���� (��~��)�� �ƴ϶� (��~ ������ ��) ��������
WITH TB AS (
    SELECT TO_DATE('2020-03-11') TODAY  FROM dual
    UNION ALL
    SELECT TO_DATE('2020-03-19') TODAY FROM dual
    UNION ALL
    SELECT TO_DATE('2020-03-24') TODAY FROM dual
    UNION ALL
    SELECT TO_DATE('2020-03-31') TODAY FROM dual
)
select TODAY ����, NEXT_DAY(today-8,1)+1 "�� ��������", NEXT_DAY(today-8,1)+7 "�� ������ ����" from tb; --���� ���۰� ��
--����ũ 5���� �ش� �Ⱓ ���� ����� ��������
select * from customer;
SELECT nvl(sum(sQTY),0) sum from (
SELECT * FROM sale WHERE cnum=13 AND 
        (SYSDATE >= NEXT_DAY(SYSDATE-8,1)+1 
        AND SYSDATE <= NEXT_DAY(SYSDATE-8,7)+7
));--���ְ� �簣 ����ũ
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
insert into product(pnum, pname, price, stock) values(product_seq.NEXTVAL, '���� ����ũ (KF94)', 1500, 0);
insert into product(pnum, pname, price, stock) values(product_seq.NEXTVAL, '�ռҵ���', 1500, 0);
insert into product(pnum, pname, price, stock) values(product_seq.NEXTVAL, 'Ÿ�̷���', 1500, 0);
insert into product(pnum, pname, price, stock) values(product_seq.NEXTVAL, '�Ժ���', 1500, 0);
insert into product(pnum, pname, price, stock) values(product_seq.NEXTVAL, '����', 3300, 0);
insert into product(pnum, pname, price, stock) values(product_seq.NEXTVAL, '�뽺ī����', 25000, 0);
insert into product(pnum, pname, price, stock) values(product_seq.NEXTVAL, '��Ʈ������', 25000, 0);
insert into product(pnum, pname, price, stock) values(product_seq.NEXTVAL, '����ƽ����Ʈ��', 50000, 0);
insert into product(pnum, pname, price, stock) values(product_seq.NEXTVAL, '����Ȱ���', 700, 0);
insert into product(pnum, pname, price, stock) values(product_seq.NEXTVAL, '���� ����õ', 1300, 0);
insert into product(pnum, pname, price, stock) values(product_seq.NEXTVAL, '��û��', 2000, 0);

select * from product;
update product set pname='��Ʈ������' where pnum=14;
commit;

select * from product;
insert into product_keyword(pnum, keyword) values(2,'����');
insert into product_keyword(pnum, keyword) values(3,'����');
insert into product_keyword(pnum, keyword) values(4,'����');
insert into product_keyword(pnum, keyword) values(14,'���帧');
insert into product_keyword(pnum, keyword) values(15,'���帧');
insert into product_keyword(pnum, keyword) values(16,'���帧');

commit;

select p.pnum, pname, price, stock from product p
JOIN product_keyword k ON k.pnum = p.pnum
WHERE keyword='����';

--���� ��� (�ߺ�����)
select distinct keyword from product_keyword;
--���� ǰ��
select keyword, p.pnum, pname, price, stock from product_keyword k 
JOIN product p ON k.pnum = p.pnum
GROUP BY keyword, (p.pnum, pname, price, stock)
order by keyword, stock desc;
select * from customer;
select * from sale s full outer join customer c on c.cnum = s.cnum;

select * from sale s 
join customer c on s.cnum = c.cnum
order by sdate desc;
