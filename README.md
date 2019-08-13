## wpx1997

## 资料
[Spring 文档](https://spring.io/guides/)\
[Spring Web](https://spring.io/guides/gs/serving-web-content/)\
[Github OAuth](https://developer.github.com/apps/building-oauth-apps/creating-an-oauth-app/)
[Bootstrap](https://v3.bootcss.com/)
[thymeleaf](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html)

## 工具
[Git](https://git-scm.com/download/win)
[Lombok](http://plugins.jetbrains.com/plugin/6317-lombok/versions)

##脚本
创建user表
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
创建question表
~~~sql
create table question
(
	id int auto_increment,
	title varchar(50),
	description text,
	gmt_create BIGINT,
	gmt_modified BIGINT,
	creator int,
	comment_count int default 0,
	view_count int default 0,
	like_count int default 0,
	tag varchar(256),
	constraint question_pk
		primary key (id)
);
~~~


