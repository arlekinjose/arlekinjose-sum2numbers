drop database sumtwonumbersdb;
drop user sumtwonumbers;
create user sumtwonumbers with password 'HEp8K7szyMRvjJX9GAs6';
#create user sumtwonumbers with password 'DB_APP_PASSWORD_VALUE';
create database sumtwonumbersdb with template=template0 owner=sumtwonumbers;
\connect sumtwonumbersdb;
alter default privileges grant all on tables to sumtwonumbers;
alter default privileges grant all on sequences to sumtwonumbers;

create table s2n_users(
user_id integer primary key not null,
first_name varchar(20) not null,
last_name varchar(20) not null,
email varchar(30) not null,
password text not null
);

create table s2n_sums(
sum_id integer primary key not null,
user_id integer not null,
sum1 integer not null,
sum2 integer not null,
result integer not null
);
alter table s2n_sums add constraint cat_users_fk
foreign key (user_id) references s2n_users(user_id);

create sequence s2n_users_seq increment 1 start 1;
create sequence s2n_sums_seq increment 1 start 1;
