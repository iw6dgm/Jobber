CREATE VIEW report AS
SELECT
name,
description,
ROUND(total_hours_per_project,2) as total_hours_per_project,
ROUND(100.0*total_hours_per_project/total_hours,2) AS perc
FROM
(
   SELECT
   u.name,
   p.description,
   SUM ( julianday(event_end.event_date) - julianday(event_start.event_date) ) * 24 as total_hours_per_project,
   total.total_hours
   FROM event_store as event_end,
   event_store as event_start,
   user_project up,
   user u,
   project p,
   (
      SELECT
      u.id,
      SUM
      (
         julianday(event_end.event_date) - julianday(event_start.event_date)
      )
      * 24 as total_hours
      FROM event_store as event_end,
      event_store as event_start,
      user_project up,
      user u,
      project p
      WHERE event_end.parent_id = event_start.rowid
      AND event_start.project_id = up.project_id
      AND event_start.user_id = up.user_id
      AND u.id = up.user_id
      AND p.id = up.project_id
      AND event_start.event_type = 'PROJECT_START'
      GROUP BY u.id
   )
   as total
   WHERE event_end.parent_id = event_start.rowid
   AND event_start.project_id = up.project_id
   AND event_start.user_id = up.user_id
   AND u.id = up.user_id
   AND p.id = up.project_id
   AND event_start.event_type = 'PROJECT_START'
   AND u.id = total.id
   GROUP BY name,
   description
)
AS report
ORDER BY 1,4 DESC
;
