DELETE FROM redirection_link;

INSERT INTO redirection_link (UUID, COURSE_ID, LECTURE_ID, STREAM_TEAM_PROFILE_ID, STUDENT_PROFILE_ID,
                              URL_TO_REDIRECT, REDIRECTION_PAGE, EXPIRATION_MINUTES, EVENT_NAME, EVENT_TYPE, STATUS,
                              TIMESTAMP)

VALUES ('working-aaaa-aaaa-bbbb', 1, 2, 3, 4, 'http://urltoredirect',
        'https://moodle.jrr.by/redirect/aaaa-aaaa-aaaa-bbbb',
        15, 'event name', 'LECTURE', 'NEW', '2020-12-31T23:55:59'),
       ('expired-aaaa-aaaa-aaaa', 1, 2, 3, 4, 'http://urltoredirect',
        'https://moodle.jrr.by/redirect/aaaa-aaaa-aaaa-aaaa',
        15, 'event name', 'LECTURE', 'NEW', '2020-12-31T23:40:59'),
       ('used-aaaa-aaaa-aaaa', 1, 2, 4, 3, 'http://urltoredirect', 'https://moodle.jrr.by/redirect/aaaa-aaaa-aaaa-aaaa',
        15, 'event name', 'LECTURE', 'USED', '2020-12-31T23:40:59'),
       ('already-expired-aaaa-aaaa', 1, 2, 4, 3, 'http://urltoredirect', 'https://moodle.jrr.by/redirect/aaaa-aaaa-aaaa-aaaa',
        15, 'event name', 'LECTURE', 'EXPIRED', '2020-12-31T23:40:59');

