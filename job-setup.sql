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

CREATE TABLE user_project (
	user_id VARCHAR(32) NOT NULL,
	project_id VARCHAR(32) NOT NULL
);

INSERT INTO user_project(user_id,project_id) VALUES('user','test');

