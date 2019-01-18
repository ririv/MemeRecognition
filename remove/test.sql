insert into USER (id,username,password) values(1,'123','123');
insert into USER (id,username,password) values(2,'1234','1234');

insert into ROLE (id,name) values(1,'ROLE_ADMIN');
insert into ROLE (id,name) values(2,'ROLE_USER');

insert into USER_ROLES (USER_ID, ROLES_ID) values(1,1);
insert into USER_ROLES (USER_ID, ROLES_ID) values(2,2);
