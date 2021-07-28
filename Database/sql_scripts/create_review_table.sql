DROP TABLE REVIEW; 
CREATE TABLE REVIEW (
    book_id int NOT NULL,
    user_id int NOT NULL,
    description varchar(1000) DEFAULT null,
    rating double NOT NULL,
    rating_date date NOT NULL,
    isPublic boolean NOT NULL DEFAULT FALSE,
    PRIMARY KEY(book_id, user_id),
    FOREIGN KEY (book_id) REFERENCES Book(book_id),
    FOREIGN KEY (user_id) REFERENCES USER_T(user_id)
);

INSERT INTO REVIEW VALUES(1, 1,'Krajnje zanimljiva knjiga, vrijedi pročitati',4,'2021-07-07',true);
INSERT INTO REVIEW VALUES(2, 1,'Dobro štivo za pročitati ali nije me krajnje baš zainteresirala. Likovi su malo dosadni',2,'2021-07-17',true);
INSERT INTO REVIEW VALUES(3, 1,'Nije vrijedno baš za čitati ali okej je.',1,'2021-07-07',true);
INSERT INTO REVIEW VALUES(4, 1,'Zanimljivo. Lijepo i baš je dobra knjiga',3,'2021-07-07',true);
INSERT INTO REVIEW VALUES(5, 1,'Krajnje zadimljiva knjiga, vrijedi pročitati',4,'2021-07-07',false);
INSERT INTO REVIEW VALUES(6, 1,'Krajnje zadimljiva knjiga, vrijedi pročitati',5,'2021-07-07',true);

INSERT INTO REVIEW VALUES(6, 2,'Krajnje zanimljiva knjiga, vrijedi pročitati',4,'2021-07-07',true);
INSERT INTO REVIEW VALUES(5, 2,'Dobro štivo za pročitati ali nije me krajnje baš zainteresirala. Likovi su malo dosadni',2,'2021-07-17',true);
INSERT INTO REVIEW VALUES(4, 2,'Nije vrijedno baš za čitati ali okej je.',1,'2021-07-07',true);
INSERT INTO REVIEW VALUES(3, 2,'Zanimljivo. Lijepo i baš je dobra knjiga. Jako super.',3,'2021-07-07',true);
INSERT INTO REVIEW VALUES(2, 2,'Krajnje zadimljiva knjiga, vrijedi pročitati.',4,'2021-07-07',false);
INSERT INTO REVIEW VALUES(1, 2,'Krajnje zadimljiva knjiga, vrijedi pročitati.',5,'2021-07-07',true);

INSERT INTO REVIEW VALUES(7, 2,default,5,'2021-07-25',true);