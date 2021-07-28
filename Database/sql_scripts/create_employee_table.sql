DROP TABLE EMPLOYEE; 
CREATE TABLE EMPLOYEE (
    employee_id int NOT NULL  primary key
        GENERATED ALWAYS AS IDENTITY
        (START WITH 1, INCREMENT BY 1),
    username varchar(25) NOT NULL,
    password varchar(255) NOT NULL,
    firstName varchar(255),
    lastName varchar(255),
    email varchar(255) NOT NULL,
    isblocked boolean NOT NULL DEFAULT FALSE,
	employee_role_id int NOT NULL,
	FOREIGN KEY (employee_role_id) REFERENCES EMPLOYEE_ROLE(employee_role_id)
);

--password is user
INSERT INTO EMPLOYEE VALUES(default, 'admin','214eb9f0829447645138d4daa96671099e55ab75180542dd792ef21761bac564af246707665cfef8','admin','admin','test.admin@test.com',default,1);