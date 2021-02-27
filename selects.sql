--Список тех, кто заджойнился в тг
select timestamp, event_type, stream_team_profile_id, student_profile_id from student_action_to_log where stream_team_profile_id = 83647 and event_type = 'TELEGRAM_CHAT';

--profile_id тех, кто заджойнился в тг
select student_profile_id from student_action_to_log where stream_team_profile_id = 83647 and event_type = 'TELEGRAM_CHAT';

--profile_id тех, кто не заджойнился в тг
select subscriber_profile_id from stream_and_team_subscriber where stream_team_profile_id = 83647 and subscriber_profile_id not in (select student_profile_id from student_action_to_log where stream_team_profile_id = 83647 and event_type = 'TELEGRAM_CHAT');

---userData тех, кто не заджойнился в тг
select profile.id, users.first_and_last_name, users.phone from profile
join users 
ON profile.user_id = users.user_id
where profile.id in (select subscriber_profile_id from stream_and_team_subscriber where stream_team_profile_id = 83647 and subscriber_profile_id not in (select student_profile_id from student_action_to_log where stream_team_profile_id = 83647 and event_type = 'TELEGRAM_CHAT'));

--userData тех, кто заджойнился в тг
select profile.id, users.first_and_last_name, users.phone from profile
join users 
ON profile.user_id = users.user_id
where profile.id in (select subscriber_profile_id from stream_and_team_subscriber where stream_team_profile_id = 83647 and subscriber_profile_id in (select student_profile_id from student_action_to_log where stream_team_profile_id = 83647 and event_type = 'TELEGRAM_CHAT'));

--profile_id тех, кто был на лекции
select distinct student_profile_id from student_action_to_log where timelineuuid = 'b2310932-9bc4-45c2-99d2-d4db1eb7ca77' and timestamp between '2021-02-23 19:00:00' and '2021-02-23 20:00:00';

--userData тех, кто заджойнился в тг и был на лекции
select profile.id, users.first_and_last_name, users.phone from profile
join users 
ON profile.user_id = users.user_id
where profile.id in (select subscriber_profile_id from stream_and_team_subscriber where stream_team_profile_id = 83647 and subscriber_profile_id in (select student_profile_id from student_action_to_log where stream_team_profile_id = 83647 and event_type = 'TELEGRAM_CHAT'))
and profile.id in (select distinct student_profile_id from student_action_to_log where timelineuuid = 'b2310932-9bc4-45c2-99d2-d4db1eb7ca77' and timestamp between '2021-02-23 19:00:00' and '2021-02-23 20:00:00');

--userData тех, кто заджойнился в тг и не был на лекции
select profile.id, users.first_and_last_name, users.phone from profile
join users 
ON profile.user_id = users.user_id
where profile.id in (select subscriber_profile_id from stream_and_team_subscriber where stream_team_profile_id = 83647 and subscriber_profile_id in (select student_profile_id from student_action_to_log where stream_team_profile_id = 83647 and event_type = 'TELEGRAM_CHAT'))
and profile.id not in (select distinct student_profile_id from student_action_to_log where timelineuuid = 'b2310932-9bc4-45c2-99d2-d4db1eb7ca77' and timestamp between '2021-02-23 19:00:00' and '2021-02-23 20:00:00');

--userData тех, кто не заджойнился в тг и не был на лекции
select profile.id, users.first_and_last_name, users.phone from profile
join users 
ON profile.user_id = users.user_id
where profile.id in (select subscriber_profile_id from stream_and_team_subscriber where stream_team_profile_id = 83647 and subscriber_profile_id not in (select student_profile_id from student_action_to_log where stream_team_profile_id = 83647 and event_type = 'TELEGRAM_CHAT'))
and
profile.id not in (select distinct student_profile_id from student_action_to_log where timelineuuid = 'b2310932-9bc4-45c2-99d2-d4db1eb7ca77' and timestamp between '2021-02-23 19:00:00' and '2021-02-23 20:00:00');






