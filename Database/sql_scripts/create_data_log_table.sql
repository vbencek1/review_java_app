DROP TABLE DATA_LOG; 
CREATE TABLE DATA_LOG (
    data_log_id int NOT NULL  primary key
        GENERATED ALWAYS AS IDENTITY
        (START WITH 1, INCREMENT BY 1),
    view_name varchar(255),
    mathod_name varchar(255),
    parametars varchar(255),
    action_date date NOT NULL,
	user_id int NOT NULL,
	FOREIGN KEY (user_id) REFERENCES USER_T(user_id)
);

--password is user
INSERT INTO DATA_LOG VALUES(default, 'test','test','test','2021-07-07',1);