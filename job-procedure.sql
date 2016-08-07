delimiter //

CREATE PROCEDURE `startProject`(in in_user_id  varchar(25), in in_project_id int)
begin
insert into user_jobs values(null,
in_user_id,
in_project_id,
null,
null,
1);
update user set project_id=in_project_id,
status=1,
event_id=(select max(id) from user_jobs where
status=1 and user_id=in_user_id and project_id=in_project_id)
where user_id=in_user_id;
end
//


CREATE PROCEDURE `stopProject`(in in_user_id  varchar(25))
begin

update user_jobs set status=0
where id=(
select event_id from user where user_id=in_user_id
);

update user set status=0 where user_id=in_user_id;
end;
//
