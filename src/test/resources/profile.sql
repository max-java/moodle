DELETE
FROM time_line;

INSERT INTO time_line
(id, course_id, date_time, event_name, event_type, lecture_id, notes, stream_team_profile_id, url_to_redirect, timelineuuid)
VALUES
 (1511, 1286, '2020-08-07 19:00:00.000000', 'Lecture 1', 'LECTURE', 1265, 'это лекция 1', 1324, 'https://us02web.zoom.us/j/88181036761', '1b2d6ecb-9919-4cc2-917c-448e9fdc000d'),
 (1512, 1286, '2020-08-08 19:00:00.000000', 'Lecture 2', 'LECTURE', 1265, 'это лекция 2', 1324, 'https://us02web.zoom.us/j/88181036762', '2b2d6ecb-9919-4cc2-917c-448e9fdc000d'),
 (1513, 1286, '2020-08-09 19:00:00.000000', 'Lecture 3', 'LECTURE', 1265, 'это лекция 2', 1324, 'https://us02web.zoom.us/j/88181036763', '3b2d6ecb-9919-4cc2-917c-448e9fdc000d'),
 (2033, 1286, '2020-12-17 12:56:00.000000', 'Lecture 32', 'LECTURE', 1265, 'Исключения. Вложенные и Анонимные классы.', 1495, 'https://us02web.zoom.us/j/85405478119', 'a973a5aa-683d-414c-8c63-afa13782f709');
