DROP TABLE USER_T; 
CREATE TABLE USER_T (
    user_id int NOT NULL  primary key
        GENERATED ALWAYS AS IDENTITY
        (START WITH 1, INCREMENT BY 1),
    username varchar(25) NOT NULL,
    password varchar(255) NOT NULL,
    firstName varchar(255),
    lastName varchar(255),
    email varchar(255) NOT NULL
);