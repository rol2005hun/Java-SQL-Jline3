-- users tábla törlése, ha létezik
drop table users;

-- szekvencia törlése, ha létezik
drop sequence user_id_seq;

-- szekvencia létrehozása
create sequence user_id_seq
start with 1
increment by 1
nocache
nocycle;

-- users tábla létrehozása
create table users (
    id number(20), -- 20 számjegyű szám
    username varchar2(40),
    password varchar2(40),
    email varchar2(255),
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp,
    constraint user_pk primary key (id),
    constraint user_unique_username unique (username),
    constraint user_unique_email unique (email),
    constraint user_check_username check (length(username) > 0 and username is not null),
    constraint user_check_password check (length(password) > 0 and password is not null),
    constraint user_check_email check (length(email) > 0),
    constraint user_check_created_at check (created_at is not null),
    constraint user_check_updated_at check (updated_at is not null)
);

-- trigger létrehozása az id automatikus generálásához
create or replace trigger before_insert_users
before insert on users
for each row
begin
    :new.id := user_id_seq.nextval;
end;

-- saját magad beszúrása, de külön futtasd le
insert into users (username, password)
values ('ranzak', 'jojelszo');