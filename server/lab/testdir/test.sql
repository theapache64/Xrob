SELECT
  (SELECT IFNULL(GROUP_CONCAT(f2.file_name SEPARATOR '/'), '/')
   FROM files f2
      LEFT JOIN files f3 ON f3.parent_id = f2.file_id
   WHERE f2.file_id = f1.parent_id) AS abs_file_path,
  f1.file_name
FROM files f1
WHERE f1.victim_id = 1 AND f1.parent_id = 7;