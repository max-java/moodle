start transaction ;


${userId} = select * from hibernate_sequence;

insert into users (user_id, active, email, last_name, name, password, user_name, phone)
values(
          ${userId},
          true,
          'alexandrtulai13@gmail.com',
          'How to start developer Journey',
          '[5.19]DEVjOURNEY',
          '$2a$10$Y94tACz0A8hXmdSF59veQ.XjKt8Ox8xGO1dn2ME716byioUI.rmeG',
          'dJourney-5.19',
          '+375298469919'
      );

insert into user_role (user_id, role_id)
values(${userId}, 228);

insert into profile (id, course_id, owner_profile_id, user_id, free)
values(${userId+1}, 149805, 76159, ${userId}, true);

update hibernate_sequence set next_val = ${userId+2}
