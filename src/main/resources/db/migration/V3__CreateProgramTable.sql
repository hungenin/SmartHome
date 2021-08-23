drop table if exists program CASCADE;

create table program (
    id bigint generated by default as identity,
    end timestamp not null,
    start timestamp not null,
    channel_id bigint not null,
    content_id bigint not null,
    primary key (id)
);