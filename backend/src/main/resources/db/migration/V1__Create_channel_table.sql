drop table if exists channel CASCADE;

create table channel (
    id smallint not null,
    name varchar(255) not null,
    logo clob,
    follow boolean not null,
    primary key (id)
);