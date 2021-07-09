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

--password is user
INSERT INTO USER_T VALUES(default, 'user','214eb9f0829447645138d4daa96671099e55ab75180542dd792ef21761bac564af246707665cfef8','user','user','test.test@test.com');