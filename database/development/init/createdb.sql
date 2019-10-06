create schema dev_db;

alter schema dev_db owner to dev_db;

create table if not exists dev_db.user_account
(
	id serial not null
		constraint user_account_pk
			primary key,
	username varchar not null,
	password varchar not null
	role varchar not null
);

alter table dev_db.user_account owner to dev_db;

create unique index if not exists user_account_id_uindex
	on dev_db.user_account (id);

create unique index user_account_username_uindex
	on dev_db.user_account (username);
