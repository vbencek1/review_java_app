DROP TABLE DATA_LOG_EMPLOYEES; 
CREATE TABLE DATA_LOG_EMPLOYEES (
    data_log_id int NOT NULL  primary key
        GENERATED ALWAYS AS IDENTITY
        (START WITH 1, INCREMENT BY 1),
    view_name varchar(255),
    method_name varchar(255),
    parametars varchar(1000),
    action_date timestamp NOT NULL,
	employee_id int NOT NULL,
	FOREIGN KEY (employee_id) REFERENCES EMPLOYEE(employee_id)
);

INSERT INTO DATA_LOG_EMPLOYEES VALUES(default, 'test','test','test','2021-07-07 10:10:10',1);