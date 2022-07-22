alter table program
    add foreign key (channel_id) references channel;

alter table program
    add foreign key (content_id) references content;