create table comment
(
	id bigint auto_increment,
	question_id bigint not null,
	commentator bigint not null,
	type int not null,
	content varchar(1024),
	gmt_create bigint,
	gmt_modified bigint,
	like_count bigint default 0,
	constraint comment_pk
		primary key (id)
);