CREATE OR REPLACE TRIGGER secure_emp
BEFORE INSERT ON employees BEGIN
 --IF (TO_CHAR(SYSDATE,'DY') IN ('SAT','SUN')) OR (TO_CHAR(SYSDATE,'HH24:MI') NOT BETWEEN '08:00' AND '18:00') THEN
  IF (TO_CHAR(SYSDATE,'HH24:MI') BETWEEN '09:00' AND '12:00') THEN
  RAISE_APPLICATION_ERROR(-20500, 'You may insert'
                           ||' into EMPLOYEES table only during '
                           ||' business hours.');
  END IF;
END;
/



CREATE OR REPLACE TRIGGER restrict_salary
BEFORE INSERT OR UPDATE OF salary ON employees
FOR EACH ROW
BEGIN
  IF NOT (:NEW.job_id IN ('AD_PRES', 'AD_VP'))
     AND :NEW.salary > 15000 THEN
    RAISE_APPLICATION_ERROR (-20202,
      'Employee cannot earn more than $15,000.');
  END IF;
END;
/
