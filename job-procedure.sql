DELIMITER $$
CREATE PROCEDURE `login`(IN vUserId VARCHAR(25), IN vPassword VARCHAR(50))
BEGIN
SELECT user_id FROM users WHERE password=password(vPassword) AND user_id=vUserId;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE `startProject`(in in_user_id  varchar(25), in in_project_id int)
BEGIN

DECLARE lastId int;
   DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;

   START TRANSACTION;


insert into user_jobs(user_id, project_id, startdate, enddate, status) values(
in_user_id,
in_project_id,
null,
null,
1);

SET lastId = @@IDENTITY;

update users set project_id=in_project_id,
status=1,
event_id=lastId
where user_id=in_user_id;

COMMIT;

SELECT lastId;

END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE `stopProject`(in in_user_id  varchar(25))
BEGIN

   DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;

   START TRANSACTION;


update user_jobs set status=0
where id=(
select event_id from users where user_id=in_user_id
);
update users set status=0 where user_id=in_user_id;

COMMIT;

END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE `updateNote`(IN vId int, IN vNote VARCHAR(225))
BEGIN
   DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;

   START TRANSACTION;

    UPDATE user_jobs SET notes=vNote WHERE id=vId;

  COMMIT;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE `removeUserProject`(in userid varchar(25), in projectid smallint)
BEGIN
   DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;

   START TRANSACTION;

  DELETE FROM user_projects WHERE user_id = userid AND project_id = projectid;

  COMMIT;

END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE `deleteProject`(in id smallint)
BEGIN
   DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;

   START TRANSACTION;


     INSERT INTO backup_user_jobs
     SELECT uj.id, uj.user_id, p.name project_name, uj.startdate, uj.enddate, uj.status, uj.notes
     FROM user_jobs uj INNER JOIN project p ON (uj.project_id = p.project_id) WHERE p.project_id=id;



     DELETE FROM user_jobs WHERE project_id=id;

     DELETE FROM user_projects WHERE project_id = id;

     DELETE FROM project WHERE project_id=id;

   COMMIT;

END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE `deleteUser`(in id varchar(25))
BEGIN
  INSERT INTO backup_user_jobs
  SELECT uj.id, uj.user_id, p.name project_name, uj.startdate, uj.enddate, uj.status, uj.notes
  FROM user_jobs uj INNER JOIN project p ON (uj.project_id = p.project_id) WHERE uj.user_id=id;


  DELETE FROM user_jobs WHERE user_id = id;

  DELETE FROM user_projects WHERE user_id = id;

  DELETE FROM users WHERE user_id = id;

END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE `getNote`(IN vId int)
BEGIN
SELECT notes FROM user_jobs WHERE id=vId;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE `getProjects`(IN vUserId VARCHAR(25))
BEGIN
SELECT p.project_id, p.name, p.priority FROM user_projects u, project p
WHERE p.status > 0 AND p.project_id = u.project_id AND u.user_id=vUserId ORDER BY priority DESC;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE `getStatus`(IN vUserId VARCHAR(25))
BEGIN
SELECT status, project_id, event_id FROM users WHERE user_id=vUserId;
END$$
DELIMITER ;