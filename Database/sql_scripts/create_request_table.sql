DROP TABLE REQUEST;
CREATE TABLE REQUEST(
   request_id         INT  NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)
  ,isbn               VARCHAR(255)
  ,title              VARCHAR(255)
  ,description        VARCHAR(2000)
  ,user_id            int NOT NULL
  ,request_date       date NOT NULL
  ,FOREIGN KEY (user_id) REFERENCES USER_T(user_id)
);

INSERT INTO REQUEST VALUES(default, null,'Knjiga o džungli','Neznam, ona dobra knjiga',1,'2021-07-07');
INSERT INTO REQUEST VALUES(default, null,'Knjiga o mackama i psima','-',1,'2021-07-08');
INSERT INTO REQUEST VALUES(default, '1234512345',null,null,1,'2021-07-22');
