drop table if exists channel_connector CASCADE;

create table channel_connector (
    channel_id bigint not null,
    port_id integer not null,
    primary key (channel_id)
);