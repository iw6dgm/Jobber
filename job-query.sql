SELECT
    u.name,
    p.description,
    SUM
        (
                julianday(event_end.event_date) - julianday(event_start.event_date)
        )
        * 24 as total_hours
FROM event_store as event_end,
     (SELECT rowid,event_date from event_store WHERE event_type = 'PROJECT_START') as event_start,
     project p,
     user u
WHERE event_end.parent_id = event_start.rowid
  AND event_end.project_id = p.id
  AND event_end.user_id = u.id
;
