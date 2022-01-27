DROP TABLE login;

CREATE TABLE login (
	user VARCHAR(32) PRIMARY KEY,
	password VARCHAR(256) NOT NULL
);

INSERT INTO login VALUES('user','tester');

CREATE TABLE project (
	id VARCHAR(32) PRIMARY KEY,
	description VARCHAR(256) NOT NULL,
	status SMALLINT NOT NULL DEFAULT 1
);

CREATE TABLE user (
	id VARCHAR(32) PRIMARY KEY,
	name VARCHAR(256) NOT NULL,
	status SMALLINT NOT NULL DEFAULT 1
);

INSERT INTO project(id,description) VALUES('test','Test Project');

INSERT INTO user(id,name) VALUES('user','Test User');

DROP TABLE user_project;

CREATE TABLE user_project (
	user_id VARCHAR(32) NOT NULL,
	project_id VARCHAR(32) NOT NULL,
	primary key (user_id, project_id),
	foreign key (user_id) references user(id),
	foreign key (project_id) references project(id)
);

INSERT INTO user_project(user_id,project_id) VALUES('user','test');

DROP TABLE event_store;

CREATE TABLE event_store (
	user_id VARCHAR(32) NOT NULL,
	project_id VARCHAR(32) NOT NULL,
	event_type VARCHAR(32) NOT NULL,
	event_date DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
	parent_id INTEGER NULL,
	foreign key (user_id) references user(id),
	foreign key (project_id) references project(id)
);

CREATE TABLE user_event (
	user_id VARCHAR(32) PRIMARY KEY NOT NULL,
	project_id VARCHAR(32) NULL,
	event_id INTEGER NULL
);