BEGIN;
ALTER TABLE tb_recurrence
ADD recurrence_day INTEGER NOT NULL,
ADD recurrence_month INTEGER NOT NULL,
ADD recurrence_year INTEGER NOT NULL;

UPDATE tb_recurrence
SET recurrence_day = EXTRACT(DAY FROM recurring_until),
    recurrence_month = EXTRACT(MONTH FROM recurring_until),
    recurrence_year = EXTRACT(YEAR FROM recurring_until);

ALTER TABLE tb_recurrence DROP COLUMN recurring_until;
COMMIT;
