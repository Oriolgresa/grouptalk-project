drop user 'grouptalk'@'localhost';
create user 'grouptalk'@'localhost' identified by 'grouptalk';
grant all privileges on grouptalkdb.* to 'grouptalk'@'localhost';
flush privileges;