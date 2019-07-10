# knowledgepractice
学习和工作中遇到的知识的总结

#数据库脚本
create table user (
id bigint(20) PRIMARY KEY AUTO_INCREMENT,
mobile varchar(100) not null,
password varchar(500) not null
);

insert into user (id, mobile, password) values (1000, '18827417824', '18827417824');