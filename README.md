## wpx1997

## 资料
[Spring 文档](https://spring.io/guides/)\
[Spring Web](https://spring.io/guides/gs/serving-web-content/)\
[Github OAuth](https://developer.github.com/apps/building-oauth-apps/creating-an-oauth-app/)

## 工具
[Git](https://git-scm.com/download/win)

##脚本
~~~sql
create table user(
        	id int auto_increment,
        	account_id varchar(100),
        	name varchar(50),
        	token char(36),
        	gmt_create BIGINT,
        	gmt_modified BIGINT,
        	bio varchar(256),
        	constraint user_pk
        		primary key (id)
            );
~~~
