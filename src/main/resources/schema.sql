create table if not exists Users (
	id bigint primary key AUTO_INCREMENT,
	username varchar(32) not null,
	password text not null,
	country varchar(40),
	city varchar(40),
	birthdate date,
	avatarUrl text
);

create table if not exists Roles (
  id varchar(45) not null,
  displayname varchar(45) not null,
  PRIMARY KEY (`id`)
);


CREATE table if not exists Users_roles (
  user_id bigint not null,
  role_id varchar(45) not null,
  CONSTRAINT `role_fk` FOREIGN KEY (`role_id`) REFERENCES `Roles` (`id`),
  CONSTRAINT `user_fk` FOREIGN KEY (`user_id`) REFERENCES `Users` (`id`)
);

create table if not exists Forums (
	id bigint primary key AUTO_INCREMENT,
	name varchar(32) not null
);

create table if not exists Threads (
	id bigint primary key AUTO_INCREMENT,
	userid bigint not null,
	forumid bigint not null,
	title varchar(32) not null,
	constraint threads_ibfk_1 foreign key (userid) references Users(id),
	constraint threads_ibfk_2 foreign key (forumid) references Forums(id)
);

create table if not exists Posts (
	id bigint primary key AUTO_INCREMENT,
	threadid bigint not null,
	userid bigint not null,
	content text not null,
	creationdate datetime not null,
	constraint posts_ibfk_1 foreign key (threadid) references Threads(id),
	constraint posts_ibfk_2 foreign key (userid) references Users(id)
);