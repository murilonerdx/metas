-- Exemplo 1
CREATE OR REPLACE PROCEDURE raise_salary
  (id      IN employees.employee_id%TYPE,
   percent IN NUMBER)
IS
  e_no_update exception;
BEGIN
  UPDATE employees
  SET    salary = salary * (1 + percent/100)
  WHERE  employee_id = id;
  
  IF SQL%NOTFOUND THEN
      RAISE e_no_update;
  ELSE
     DBMS_OUTPUT.PUT_LINE('Salary Updated'); 
  END IF;
EXCEPTION
WHEN e_no_update THEN
    DBMS_OUTPUT.PUT_LINE('ID Not found');
END raise_salary;
/

set serveroutput on
EXECUTE raise_salary(1236,10);



-- Exemplo 2
CREATE OR REPLACE PROCEDURE query_emp
 (id     IN  employees.employee_id%TYPE,
  name   OUT employees.last_name%TYPE,
  salary OUT employees.salary%TYPE) IS
BEGIN
  SELECT   last_name, salary INTO name, salary
   FROM    employees
   WHERE   employee_id = id;
END query_emp;
/

DECLARE
  emp_name employees.last_name%TYPE;
  emp_sal  employees.salary%TYPE;
BEGIN
  query_emp(171, emp_name, emp_sal);
END;


-- Exemplo 3
CREATE OR REPLACE PROCEDURE format_phone
  (phone_no IN OUT VARCHAR2) IS
BEGIN
  phone_no := '(' || SUBSTR(phone_no,1,3) ||
              ')' || SUBSTR(phone_no,4,3) ||
              '-' || SUBSTR(phone_no,7);
END format_phone;
/
var v_phone varchar2(15)
execute :v_phone :='8006330575';
print v_phone
execute format_phone(:v_phone)
print v_phone


--Exemplo 4
CREATE OR REPLACE PROCEDURE process_employees
IS
   CURSOR emp_cursor IS
    SELECT employee_id
    FROM   employees;
BEGIN
   FOR emp_rec IN emp_cursor 
   LOOP
     raise_salary(emp_rec.employee_id, 10);
   END LOOP;    
   COMMIT;
END process_employees;
/

