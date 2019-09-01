## wpx1997

## 部署
### 依赖
- Git
- JDK
- Maven
- MySQL
### 步骤
- yum update
- yum install git
- mkdir App
- cd App
- git clone https://github.com/wpx1997/community.git
- yum install maven
- mvn compile package
-   cp src/main/resources/application.properties src/main/resources/application-production.properties
- vim src/main/resources/application-production.properties
- 按下esc，然后shift+；然后在输入wq保存
- 

## 资料
[Spring 文档](https://spring.io/guides/)  
[Spring Web](https://spring.io/guides/gs/serving-web-content/)  
[Github OAuth](https://developer.github.com/apps/building-oauth-apps/creating-an-oauth-app/)  
[Bootstrap](https://v3.bootcss.com/)  
[thymeleaf](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html)  
[moment](http://momentjs.cn/downloads/moment.min.js)  
[Markdown 插件](https://pandao.github.io/editor.md/)

## 工具
[Git](https://git-scm.com/download/win)  
[Lombok](http://plugins.jetbrains.com/plugin/6317-lombok/versions)

##脚本
创建user表
~~~sql
create table user
(
    id bigint auto_increment,
    account_id varchar(100),
    name varchar(50),
    token char(36),
    avatar_url varchar(100),
    gmt_create bigint,
    gmt_modified bigint,
    bio varchar(256),
    constraint user_pk
        primary key (id)
);

~~~
创建question表
~~~sql
create table question
(
    id bigint auto_increment,
    creator bigint not null,
    title varchar(50),
    description text,
    tag varchar(256),
    gmt_create bigint,
    gmt_modified bigint,
    comment_count bigint default 0,
    view_count bigint default 0,
    like_count bigint default 0,
    constraint question_pk
        primary key (id)
);
~~~
创建comment表
~~~sql
create table comment
(
    id bigint auto_increment,
    parent_id bigint not null,
    commentator bigint not null,
    type int not null,
    content varchar(1024),
    gmt_create bigint,
    gmt_modified bigint,
    like_count bigint default 0,
    comment_count bigint default 0,
    constraint comment_pk
        primary key (id)
);
~~~
创建notification表
~~~sql
create table notification
(
    id bigint auto_increment,
    notifier bigint,
    receiver bigint,
    outer_id bigint,
    type int,
    gmt_create bigint,
    status int default 0,
    constraint notification_pk
        primary key (id)
);

~~~
~~~
mvn flyway:migrate
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
~~~

