-- GRANT ALL PRIVILEGES ON root.* TO 'root'@'%' IDENTIFIED BY 'test1357' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%';
flush privileges;


create table users
(
    id         int auto_increment
        primary key,
    user_id    varchar(20)                        not null,
    pwd        varchar(20)                        not null,
    name       varchar(20)                        not null,
    created_at datetime default CURRENT_TIMESTAMP null
);

