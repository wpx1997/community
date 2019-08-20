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