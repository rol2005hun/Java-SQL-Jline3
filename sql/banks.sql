-- bankaccounts tábla törlése, ha létezik
drop table bankaccounts;

-- szekvencia törlése, ha létezik
drop sequence bankaccount_id_seq;

-- szekvencia létrehozása
create sequence bankaccount_id_seq
start with 1
increment by 1
nocache
nocycle;

-- bankaccounts tábla létrehozása
create table bankaccounts (
    id varchar2(30), -- 30 karakter hosszú string
    userid number(20),
    balance number(20),
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp,
    constraint bank_pk primary key (id),
    constraint bank_uq unique (id, userid),
    constraint bank_check_created_at check (created_at is not null),
    constraint bank_check_updated_at check (updated_at is not null)
);

-- trigger létrehozása az id automatikus generálásához
create or replace trigger before_insert_bankaccounts
before insert on bankaccounts
for each row
begin
    :new.id := to_char(bankaccount_id_seq.nextval, 'fm00000000') || '-' ||
               to_char(bankaccount_id_seq.nextval, 'fm00000000') || '-' ||
               to_char(bankaccount_id_seq.nextval, 'fm00000000');
end;

-- példa beszúrás
insert into bankaccounts (userid, balance)
values (1, 1000);

-- beszúrt adatok lekérdezése
select * from bankaccounts;