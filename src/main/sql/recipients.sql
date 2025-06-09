use milou_db;
create table if not exists recipients(
    id int primary key auto_increment,
    email_id int not null,
    foreign key (email_id) references emails(id),
    recipient_user_id int not null,
    foreign key (recipient_user_id) references users(id),
    is_read boolean not null default false
);