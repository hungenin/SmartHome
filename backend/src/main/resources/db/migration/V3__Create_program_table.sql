drop table if exists program CASCADE;

create table program (
    id bigint generated by default as identity,
    start timestamp not null,
    end timestamp not null,
    content_id bigint not null,
    channel_id smallint not null,
    primary key (id)
);