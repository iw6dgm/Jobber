/*
Daily
*/

select p.name, year(t.enddate) as y,  month(t.enddate) as m, day(t.enddate) as d, week(t.enddate) as w, sum(t.delta) / 28800 as delta from
(
select user_id, project_id, enddate, startdate,  (hour(timediff(enddate, startdate))*3600 + minute(timediff(enddate, startdate))*60 + second(timediff(enddate, startdate))) as delta
from user_jobs
where status = 0 and project_id != 1 and user_id = 'joshua'
) t, project p
where
t.project_id = p.project_id
group by name, y, m, d, w
order by y asc, m asc, d asc, w asc, delta asc

/*
Weekly
*/

select t.user_id, p.name, year(t.enddate) as y,  month(t.enddate) as m, week(t.enddate) as w, sum(t.delta) / 28800 as delta from
(
select user_id, project_id, enddate, startdate,  (hour(timediff(enddate, startdate))*3600 + minute(timediff(enddate, startdate))*60 + second(timediff(enddate, startdate))) as delta
from user_jobs
where status = 0 and project_id != 1
) t, project p
where
t.project_id = p.project_id
group by user_id, name, y, m, w
order by y asc, m asc, w asc, delta asc

/*
Total daily
*/

select t.user_id, month(t.enddate) as month, day(t.enddate) as day, week(t.enddate) as week, sum(t.delta) / 28800 as delta from
(
select user_id, project_id, enddate, startdate,  (hour(timediff(enddate, startdate))*3600 + minute(timediff(enddate, startdate))*60 + second(timediff(enddate, startdate))) as delta
from user_jobs
where status = 0 and project_id != 1
) t, project p
where
t.project_id = p.project_id
group by user_id, day, month, week
order by week asc, month asc, day asc, delta asc

/*
Total weekly
*/

select t.user_id,  year(t.enddate) as year , month(t.enddate) as month,  week(t.enddate) as week, sum(t.delta) / 28800 as delta from
(
select user_id, project_id, enddate, startdate,  (hour(timediff(enddate, startdate))*3600 + minute(timediff(enddate, startdate))*60 + second(timediff(enddate, startdate))) as delta
from user_jobs
where status = 0 and project_id != 1
) t, project p
where
t.project_id = p.project_id
group by user_id,  year(t.enddate),month(t.enddate),week(t.enddate)
order by year asc,week asc, month asc,  delta asc

/*
select user_id, project_id, enddate, startdate, day(enddate) as day, week(enddate) as week, timediff(enddate, startdate) as timediff, (hour(timediff(enddate, startdate))*3600 + minute(timediff(enddate, startdate))*60 + second(timediff(enddate, startdate))) as delta
from user_jobs
where status = 0
*/
