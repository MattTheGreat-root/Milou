use milou_db;
create table if not exists emails(
    id int primary key auto_increment,
    sender_id int not null,
    foreign key (sender_id) references users(id),
    subject varchar(255),
    body text not null,
    sentAt timestamp default current_timestamp,
    emailCode varchar(255) not null
);
ALTER TABLE emails ADD CONSTRAINT unique_email_code UNIQUE (emailCode);
