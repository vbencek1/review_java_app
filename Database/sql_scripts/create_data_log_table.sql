DROP TABLE DATA_LOG; 
CREATE TABLE DATA_LOG (
    data_log_id int NOT NULL  primary key
        GENERATED ALWAYS AS IDENTITY
        (START WITH 1, INCREMENT BY 1),
    view_name varchar(255),
    method_name varchar(255),
    parametars varchar(1000),
    action_date timestamp NOT NULL,
	user_id int NOT NULL,
	FOREIGN KEY (user_id) REFERENCES USER_T(user_id)
);

INSERT INTO DATA_LOG VALUES(default, 'test','test','test','2021-07-07 10:10:10',1);