drop database if exists grouptalkdb;
create database grouptalkdb;

use grouptalkdb;

CREATE TABLE users (
    id BINARY(16) NOT NULL,
    loginid VARCHAR(15) NOT NULL UNIQUE,
    password BINARY(16) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE groups (
    id BINARY(16) NOT NULL,
    name VARCHAR(15) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE user_group (
    userid BINARY(16) NOT NULL,
    groupid BINARY(16) NOT NULL,
    FOREIGN KEY (userid) REFERENCES users(id) on delete cascade,
    FOREIGN KEY (groupid) REFERENCES groups(id) on delete cascade,
    PRIMARY KEY (userid, groupid)
);
CREATE TABLE user_roles (
    userid BINARY(16) NOT NULL,
    role VARCHAR(15) NOT NULL,
    FOREIGN KEY (userid) REFERENCES users(id) on delete cascade,
    PRIMARY KEY (userid, role)
);

CREATE TABLE auth_tokens (
    userid BINARY(16) NOT NULL,
    token BINARY(16) NOT NULL,
    FOREIGN KEY (userid) REFERENCES users(id) on delete cascade,
    PRIMARY KEY (token)
);

CREATE TABLE themes (
    id BINARY(16) NOT NULL,
    userid BINARY(16) NOT NULL,
    groupid BINARY(16) NOT NULL,
    subject VARCHAR(100) NOT NULL,
    content VARCHAR(500) NOT NULL,
    last_modified TIMESTAMP NOT NULL,
    creation_timestamp DATETIME not null default current_timestamp,
    FOREIGN KEY (userid) REFERENCES users(id) on delete cascade,
    FOREIGN KEY (groupid) REFERENCES groups(id) on delete cascade,
    PRIMARY KEY (id)
);

CREATE TABLE answers (
    id BINARY(16) NOT NULL,
    userid BINARY(16) NOT NULL,
    themeid BINARY(16) NOT NULL,
    content VARCHAR(500) NOT NULL,
    last_modified TIMESTAMP NOT NULL,
    creation_timestamp DATETIME not null default current_timestamp,
    FOREIGN KEY (userid) REFERENCES users(id) on delete cascade,
    FOREIGN KEY (themeid) REFERENCES themes(id) on delete cascade,
    PRIMARY KEY (id)
);