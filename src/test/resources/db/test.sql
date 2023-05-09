insert into user_info(user_id,created_at, modified_at, birth, email, name, password)
values (1,'2023-01-01 09:00:00', '2023-01-01 09:00:00', '1995-03-16',
        'gy@naver.com', '공유', '1234');
insert into user_info(user_id, created_at, modified_at, birth, email, name, password)
values (2, '2023-01-02 09:00:00', '2023-01-02 09:00:00', '1996-05-05',
        'kmimi@naver.com', '김미미', '1234');
insert into user_info(user_id, created_at, modified_at, birth, email, name, password)
values (3, '2023-01-03 09:00:00', '2023-01-03 09:00:00', '1997-02-21',
        'mads@naver.com', '마동석', '1234');
insert into user_info(user_id, created_at, modified_at, birth, email, name, password)
values (4, '2023-01-04 09:00:00', '2023-01-04 09:00:00', '1998-06-20',
        'songjk@naver.com', '송중기', '1234');
insert into user_info(user_id, created_at, modified_at, birth, email, name, password)
values (5, '2023-01-05 09:00:00', '2023-01-05 09:00:00', '1999-07-14',
        'songhg@naver.com', '송혜교', '1234');
insert into user_info(user_id, created_at, modified_at, birth, email, name, password)
values (6, '2023-01-06 09:00:00', '2023-01-06 09:00:00', '2000-02-01',
        'kimmg@naver.com', '김민교', '1234');
insert into user_info(user_id,created_at, modified_at, birth, email, name, password)
values (7,'2023-01-01 09:00:00', '2023-01-01 09:00:00', '1999-03-16',
        'haechan@naver.com', '유해찬', '1234');
insert into user_info(user_id, created_at, modified_at, birth, email, name, password)
values (8, '2023-01-02 09:00:00', '2023-01-02 09:00:00', '1999-05-05',
        'chulsu@naver.com', '김철수', '1234');
insert into user_info(user_id, created_at, modified_at, birth, email, name, password)
values (9, '2023-01-03 09:00:00', '2023-01-03 09:00:00', '1999-02-21',
        'younghee@naver.com', '김영희', '1234');
insert into user_info(user_id, created_at, modified_at, birth, email, name, password)
values (10, '2023-01-04 09:00:00', '2023-01-04 09:00:00', '1999-06-20',
        'Junsik@naver.com', '유준식', '1234');
insert into user_info(user_id, created_at, modified_at, birth, email, name, password)
values (11, '2023-01-05 09:00:00', '2023-01-05 09:00:00', '1999-07-14',
        'mogu@naver.com', '김모구', '1234');
insert into user_info(user_id, created_at, modified_at, birth, email, name, password)
values (12, '2023-01-06 09:00:00', '2023-01-06 09:00:00', '1999-02-01',
        'dsl@naver.com', '김보성', '1234');

insert into room_info(room_id, created_at, location, room_expired_time, room_image, room_invite_code, room_name, travel_end_date, tarvel_start_date, travel_expense)
values (1, '2023-01-03 09:00:00', '가평군', '2023-01-03 09:00:00' , 2, 'D1gvDteD24HcY5Zc2N53', '가아아아아평', '2023-08-10', '2023-08-15', 300000);
insert into room_info(room_id, created_at, location, room_expired_time, room_image, room_invite_code, room_name, travel_end_date, tarvel_start_date, travel_expense)
values (2, '2023-01-04 09:00:00', '부산시', '2023-01-04 09:00:00' , 3, 'K1vGh36D5DQvxv7az8nq', '부우우우우산', '2023-12-25', '2023-12-26', 600000);
insert into room_info(room_id, created_at, location, room_expired_time, room_image, room_invite_code, room_name, travel_end_date, tarvel_start_date, travel_expense)
values (3, '2023-01-01 09:00:00', '광양시', '2023-01-01 09:00:00' , 1, 'YUHywrA8qPKeZFZvqqzH', '광양에서 불고기먹자', '2023-06-11', '2023-06-15', 550000);
insert into room_info(room_id, created_at, location, room_expired_time, room_image, room_invite_code, room_name, travel_end_date, tarvel_start_date, travel_expense)
values (4, '2023-01-02 09:00:00', '순천시', '2023-01-02 09:00:00' , 1, 'KWOENdA8qPKeZFZvqqzH', '여수에서 간장게장', '2023-06-25', '2023-06-26', 420000);

insert into room_participant_info(room_participant_id, is_paid, joined_at, room_id, user_id)
values (1, false, '2023-01-03 09:00:00', 1, 1);
insert into room_participant_info(room_participant_id, is_paid, joined_at, room_id, user_id)
values (2, false, '2023-01-03 09:00:00', 1, 2);
insert into room_participant_info(room_participant_id, is_paid, joined_at, room_id, user_id)
values (3, false, '2023-01-03 09:00:00', 1, 3);
insert into room_participant_info(room_participant_id, is_paid, joined_at, room_id, user_id)
values (4, false, '2023-01-03 09:00:00', 1, 4);
insert into room_participant_info(room_participant_id, is_paid, joined_at, room_id, user_id)
values (5, false, '2023-01-03 09:00:00', 1, 5);
insert into room_participant_info(room_participant_id, is_paid, joined_at, room_id, user_id)
values (6, false, '2023-01-04 09:00:00', 2, 3);
insert into room_participant_info(room_participant_id, is_paid, joined_at, room_id, user_id)
values (7, false, '2023-01-04 09:00:00', 2, 4);
insert into room_participant_info(room_participant_id, is_paid, joined_at, room_id, user_id)
values (8, false, '2023-01-04 09:00:00', 2, 5);
insert into room_participant_info(room_participant_id, is_paid, joined_at, room_id, user_id)
values (9, false, '2023-01-04 09:00:00', 2, 6);
insert into room_participant_info(room_participant_id, is_paid, joined_at, room_id, user_id)
values (10, false, '2023-01-01 09:00:00', 3, 7);
insert into room_participant_info(room_participant_id, is_paid, joined_at, room_id, user_id)
values (11, false, '2023-01-01 09:00:00', 3, 8);
insert into room_participant_info(room_participant_id, is_paid, joined_at, room_id, user_id)
values (12, false, '2023-01-01 09:00:00', 3, 9);
insert into room_participant_info(room_participant_id, is_paid, joined_at, room_id, user_id)
values (13, false, '2023-01-02 09:00:00', 4, 7);
insert into room_participant_info(room_participant_id, is_paid, joined_at, room_id, user_id)
values (14, false, '2023-01-02 09:00:00', 4, 10);
insert into room_participant_info(room_participant_id, is_paid, joined_at, room_id, user_id)
values (15, false, '2023-01-02 09:00:00', 4, 11);
insert into room_participant_info(room_participant_id, is_paid, joined_at, room_id, user_id)
values (16, false, '2023-01-02 09:00:00', 4, 12);

insert into participant_role(participant_role_id, created_at, modified_at, room_role, room_id, user_id)
values (1, '2023-01-03 09:00:00', '2023-01-03 09:00:00', 'ADMIN', 1, 1);
insert into participant_role(participant_role_id, created_at, modified_at, room_role, room_id, user_id)
values (2, '2023-01-03 09:00:00', '2023-01-03 09:00:00', 'ACCOUNTING', 1, 2);
insert into participant_role(participant_role_id, created_at, modified_at, room_role, room_id, user_id)
values (3, '2023-01-03 09:00:00', '2023-01-03 09:00:00', 'SCHEDULE', 1, 3);
insert into participant_role(participant_role_id, created_at, modified_at, room_role, room_id, user_id)
values (4, '2023-01-03 09:00:00', '2023-01-03 09:00:00', 'RESERVATION', 1, 4);
insert into participant_role(participant_role_id, created_at, modified_at, room_role, room_id, user_id)
values (5, '2023-01-03 09:00:00', '2023-01-03 09:00:00', 'NONE', 1, 5);
insert into participant_role(participant_role_id, created_at, modified_at, room_role, room_id, user_id)
values (6, '2023-01-04 09:00:00', '2023-01-04 09:00:00', 'ADMIN', 2, 3);
insert into participant_role(participant_role_id, created_at, modified_at, room_role, room_id, user_id)
values (7, '2023-01-04 09:00:00', '2023-01-04 09:00:00', 'ACCOUNTING', 2, 4);
insert into participant_role(participant_role_id, created_at, modified_at, room_role, room_id, user_id)
values (8, '2023-01-04 09:00:00', '2023-01-04 09:00:00', 'SCHEDULE', 2, 5);
insert into participant_role(participant_role_id, created_at, modified_at, room_role, room_id, user_id)
values (9, '2023-01-04 09:00:00', '2023-01-04 09:00:00', 'RESERVATION', 2, 6);
insert into participant_role(participant_role_id, created_at, modified_at, room_role, room_id, user_id)
values (10, '2023-01-01 09:00:00', '2023-01-01 09:00:00', 'ADMIN', 3, 7);
insert into participant_role(participant_role_id, created_at, modified_at, room_role, room_id, user_id)
values (11, '2023-01-01 09:00:00', '2023-01-01 09:00:00', 'ACCOUNTING', 3, 8);
insert into participant_role(participant_role_id, created_at, modified_at, room_role, room_id, user_id)
values (12, '2023-01-01 09:00:00', '2023-01-01 09:00:00', 'SCHEDULE', 3, 8);
insert into participant_role(participant_role_id, created_at, modified_at, room_role, room_id, user_id)
values (13, '2023-01-01 09:00:00', '2023-01-01 09:00:00', 'RESERVATION', 3, 9);
insert into participant_role(participant_role_id, created_at, modified_at, room_role, room_id, user_id)
values (14, '2023-01-03 09:00:00', '2023-01-03 09:00:00', 'NONE', 4, 7);
insert into participant_role(participant_role_id, created_at, modified_at, room_role, room_id, user_id)
values (15, '2023-01-02 09:00:00', '2023-01-02 09:00:00', 'ADMIN', 4, 10);
insert into participant_role(participant_role_id, created_at, modified_at, room_role, room_id, user_id)
values (16, '2023-01-03 09:00:00', '2023-01-03 09:00:00', 'RESERVATION', 4, 11);
insert into participant_role(participant_role_id, created_at, modified_at, room_role, room_id, user_id)
values (17, '2023-01-04 09:00:00', '2023-01-04 09:00:00', 'SCHEDULE', 4, 11);
insert into participant_role(participant_role_id, created_at, modified_at, room_role, room_id, user_id)
values (18, '2023-01-05 09:00:00', '2023-01-05 09:00:00', 'ACCOUNTING', 4, 12);