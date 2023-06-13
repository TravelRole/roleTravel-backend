# insert into user_info(user_id, created_at, modified_at, birth, email, name, password)
# values (1, '2023-01-01 09:00:00', '2023-01-01 09:00:00', '1995-03-16',
#         'gy@naver.com', '공유', '1234');
# insert into user_info(user_id, created_at, modified_at, birth, email, name, password)
# values (2, '2023-01-02 09:00:00', '2023-01-02 09:00:00', '1996-05-05',
#         'kmimi@naver.com', '김미미', '1234');
# insert into user_info(user_id, created_at, modified_at, birth, email, name, password)
# values (3, '2023-01-03 09:00:00', '2023-01-03 09:00:00', '1997-02-21',
#         'mads@naver.com', '마동석', '1234');
# insert into user_info(user_id, created_at, modified_at, birth, email, name, password)
# values (4, '2023-01-04 09:00:00', '2023-01-04 09:00:00', '1998-06-20',
#         'songjk@naver.com', '송중기', '1234');
# insert into user_info(user_id, created_at, modified_at, birth, email, name, password)
# values (5, '2023-01-05 09:00:00', '2023-01-05 09:00:00', '1999-07-14',
#         'songhg@naver.com', '송혜교', '1234');
# insert into user_info(user_id, created_at, modified_at, birth, email, name, password)
# values (6, '2023-01-06 09:00:00', '2023-01-06 09:00:00', '2000-02-01',
#         'kimmg@naver.com', '김민교', '1234');
# insert into user_info(user_id, created_at, modified_at, birth, email, name, password)
# values (7, '2023-01-01 09:00:00', '2023-01-01 09:00:00', '1999-03-16',
#         'haechan@naver.com', '유해찬', '1234');
# insert into user_info(user_id, created_at, modified_at, birth, email, name, password)
# values (8, '2023-01-02 09:00:00', '2023-01-02 09:00:00', '1999-05-05',
#         'chulsu@naver.com', '김철수', '1234');
# insert into user_info(user_id, created_at, modified_at, birth, email, name, password)
# values (9, '2023-01-03 09:00:00', '2023-01-03 09:00:00', '1999-02-21',
#         'younghee@naver.com', '김영희', '1234');
# insert into user_info(user_id, created_at, modified_at, birth, email, name, password)
# values (10, '2023-01-04 09:00:00', '2023-01-04 09:00:00', '1999-06-20',
#         'Junsik@naver.com', '유준식', '1234');
# insert into user_info(user_id, created_at, modified_at, birth, email, name, password)
# values (11, '2023-01-05 09:00:00', '2023-01-05 09:00:00', '1999-07-14',
#         'mogu@naver.com', '김모구', '1234');
# insert into user_info(user_id, created_at, modified_at, birth, email, name, password)
# values (12, '2023-01-06 09:00:00', '2023-01-06 09:00:00', '1999-02-01',
#         'dsl@naver.com', '김보성', '1234');
#
# insert into room_info(room_id, created_at, location, room_expired_time, room_image, room_invite_code, room_name,
#                       travel_end_date, tarvel_start_date, travel_expense)
# values (1, '2023-01-03 09:00:00', '가평군', '2023-01-03 09:00:00', 2, 'D1gvDteD24HcY5Zc2N53', '가아아아아평', '2023-08-15',
#         '2023-08-10', 300000);
# insert into room_info(room_id, created_at, location, room_expired_time, room_image, room_invite_code, room_name,
#                       travel_end_date, tarvel_start_date, travel_expense)
# values (2, '2023-01-04 09:00:00', '부산시', '2023-01-04 09:00:00', 3, 'K1vGh36D5DQvxv7az8nq', '부우우우우산', '2023-12-26',
#         '2023-12-25', 600000);
# insert into room_info(room_id, created_at, location, room_expired_time, room_image, room_invite_code, room_name,
#                       travel_end_date, tarvel_start_date, travel_expense)
# values (3, '2023-01-01 09:00:00', '광양시', '2023-01-01 09:00:00', 1, 'YUHywrA8qPKeZFZvqqzH', '광양에서 불고기먹자', '2023-06-15',
#         '2023-06-11', 550000);
# insert into room_info(room_id, created_at, location, room_expired_time, room_image, room_invite_code, room_name,
#                       travel_end_date, tarvel_start_date, travel_expense)
# values (4, '2023-01-02 09:00:00', '순천시', '2023-01-02 09:00:00', 1, 'KWOENdA8qPKeZFZvqqzH', '여수에서 간장게장', '2023-06-26',
#         '2023-06-25', 420000);
#
# insert into room_info(room_id, created_at, location, room_expired_time, room_image, room_invite_code, room_name,
#                       travel_end_date, tarvel_start_date, travel_expense)
# values (5, '2023-01-04 09:00:00', '서울특별시', '2023-01-04 09:00:00', 1, 'KWOENdA8qPKeZFZvqqzH', '서울 남산 투어', '2023-07-01',
#         '2023-07-02', 300000);
#
#
# insert into room_participant_info(room_participant_id, is_paid, joined_at, room_id, user_id)
# values (1, false, '2023-01-03 09:00:00', 1, 1);
# insert into room_participant_info(room_participant_id, is_paid, joined_at, room_id, user_id)
# values (2, false, '2023-01-03 09:00:00', 1, 2);
# insert into room_participant_info(room_participant_id, is_paid, joined_at, room_id, user_id)
# values (3, false, '2023-01-03 09:00:00', 1, 3);
# insert into room_participant_info(room_participant_id, is_paid, joined_at, room_id, user_id)
# values (4, false, '2023-01-03 09:00:00', 1, 4);
# insert into room_participant_info(room_participant_id, is_paid, joined_at, room_id, user_id)
# values (5, false, '2023-01-03 09:00:00', 1, 5);
# insert into room_participant_info(room_participant_id, is_paid, joined_at, room_id, user_id)
# values (6, false, '2023-01-04 09:00:00', 2, 3);
# insert into room_participant_info(room_participant_id, is_paid, joined_at, room_id, user_id)
# values (7, false, '2023-01-04 09:00:00', 2, 4);
# insert into room_participant_info(room_participant_id, is_paid, joined_at, room_id, user_id)
# values (8, false, '2023-01-04 09:00:00', 2, 5);
# insert into room_participant_info(room_participant_id, is_paid, joined_at, room_id, user_id)
# values (9, false, '2023-01-04 09:00:00', 2, 6);
# insert into room_participant_info(room_participant_id, is_paid, joined_at, room_id, user_id)
# values (10, false, '2023-01-01 09:00:00', 3, 7);
# insert into room_participant_info(room_participant_id, is_paid, joined_at, room_id, user_id)
# values (11, false, '2023-01-01 09:00:00', 3, 8);
# insert into room_participant_info(room_participant_id, is_paid, joined_at, room_id, user_id)
# values (12, false, '2023-01-01 09:00:00', 3, 9);
# insert into room_participant_info(room_participant_id, is_paid, joined_at, room_id, user_id)
# values (13, false, '2023-01-02 09:00:00', 4, 7);
# insert into room_participant_info(room_participant_id, is_paid, joined_at, room_id, user_id)
# values (14, false, '2023-01-02 09:00:00', 4, 10);
# insert into room_participant_info(room_participant_id, is_paid, joined_at, room_id, user_id)
# values (15, false, '2023-01-02 09:00:00', 4, 11);
# insert into room_participant_info(room_participant_id, is_paid, joined_at, room_id, user_id)
# values (16, false, '2023-01-02 09:00:00', 4, 12);
# insert into room_participant_info(room_participant_id, is_paid, joined_at, room_id, user_id)
# values (17, false, '2023-01-02 09:00:00', 5, 1);
#
# insert into participant_role(participant_role_id, created_at, modified_at, room_role, room_id, user_id)
# values (1, '2023-01-03 09:00:00', '2023-01-03 09:00:00', 'ADMIN', 1, 1);
# insert into participant_role(participant_role_id, created_at, modified_at, room_role, room_id, user_id)
# values (2, '2023-01-03 09:00:00', '2023-01-03 09:00:00', 'ACCOUNTING', 1, 2);
# insert into participant_role(participant_role_id, created_at, modified_at, room_role, room_id, user_id)
# values (3, '2023-01-03 09:00:00', '2023-01-03 09:00:00', 'SCHEDULE', 1, 3);
# insert into participant_role(participant_role_id, created_at, modified_at, room_role, room_id, user_id)
# values (4, '2023-01-03 09:00:00', '2023-01-03 09:00:00', 'RESERVATION', 1, 4);
# insert into participant_role(participant_role_id, created_at, modified_at, room_role, room_id, user_id)
# values (5, '2023-01-03 09:00:00', '2023-01-03 09:00:00', 'NONE', 1, 5);
# insert into participant_role(participant_role_id, created_at, modified_at, room_role, room_id, user_id)
# values (6, '2023-01-04 09:00:00', '2023-01-04 09:00:00', 'ADMIN', 2, 3);
# insert into participant_role(participant_role_id, created_at, modified_at, room_role, room_id, user_id)
# values (7, '2023-01-04 09:00:00', '2023-01-04 09:00:00', 'ACCOUNTING', 2, 4);
# insert into participant_role(participant_role_id, created_at, modified_at, room_role, room_id, user_id)
# values (8, '2023-01-04 09:00:00', '2023-01-04 09:00:00', 'SCHEDULE', 2, 5);
# insert into participant_role(participant_role_id, created_at, modified_at, room_role, room_id, user_id)
# values (9, '2023-01-04 09:00:00', '2023-01-04 09:00:00', 'RESERVATION', 2, 6);
# insert into participant_role(participant_role_id, created_at, modified_at, room_role, room_id, user_id)
# values (10, '2023-01-01 09:00:00', '2023-01-01 09:00:00', 'ADMIN', 3, 7);
# insert into participant_role(participant_role_id, created_at, modified_at, room_role, room_id, user_id)
# values (11, '2023-01-01 09:00:00', '2023-01-01 09:00:00', 'ACCOUNTING', 3, 8);
# insert into participant_role(participant_role_id, created_at, modified_at, room_role, room_id, user_id)
# values (12, '2023-01-01 09:00:00', '2023-01-01 09:00:00', 'SCHEDULE', 3, 8);
# insert into participant_role(participant_role_id, created_at, modified_at, room_role, room_id, user_id)
# values (13, '2023-01-01 09:00:00', '2023-01-01 09:00:00', 'RESERVATION', 3, 9);
# insert into participant_role(participant_role_id, created_at, modified_at, room_role, room_id, user_id)
# values (14, '2023-01-03 09:00:00', '2023-01-03 09:00:00', 'NONE', 4, 7);
# insert into participant_role(participant_role_id, created_at, modified_at, room_role, room_id, user_id)
# values (15, '2023-01-02 09:00:00', '2023-01-02 09:00:00', 'ADMIN', 4, 10);
# insert into participant_role(participant_role_id, created_at, modified_at, room_role, room_id, user_id)
# values (16, '2023-01-03 09:00:00', '2023-01-03 09:00:00', 'RESERVATION', 4, 11);
# insert into participant_role(participant_role_id, created_at, modified_at, room_role, room_id, user_id)
# values (17, '2023-01-04 09:00:00', '2023-01-04 09:00:00', 'SCHEDULE', 4, 11);
# insert into participant_role(participant_role_id, created_at, modified_at, room_role, room_id, user_id)
# values (18, '2023-01-05 09:00:00', '2023-01-05 09:00:00', 'ACCOUNTING', 4, 12);
# insert into participant_role(participant_role_id, created_at, modified_at, room_role, room_id, user_id)
# values (19, '2023-01-03 09:00:00', '2023-01-03 09:00:00', 'SCHEDULE', 1, 2);
# insert into participant_role(participant_role_id, created_at, modified_at, room_role, room_id, user_id)
# values (20, '2023-01-03 09:00:00', '2023-01-03 09:00:00', 'ADMIN', 5, 1);
#
# insert into board(board_id, category, link, schedule_date, room_id)
# values (1, 'ACCOMMODATION', 'https://www.naver.com', '2023-08-10 10:00:00', 1);
# insert into board(board_id, category, link, schedule_date, room_id)
# values (2, 'FOOD', 'https://www.naver.com', '2023-08-10 12:00:00', 1);
# insert into board(board_id, category, schedule_date, room_id)
# values (3, 'FOOD', '2023-08-10 18:30:00', 1);
# insert into board(board_id, category, schedule_date, room_id)
# values (4, 'FOOD', '2023-08-11 12:00:00', 1);
# insert into board(board_id, category, schedule_date, room_id)
# values (5, 'TOUR', '2023-08-11 15:20:00', 1);
# insert into board(board_id, category, schedule_date, room_id)
# values (6, 'FOOD', '2023-08-11 18:00:00', 1);
# insert into board(board_id, category, schedule_date, room_id)
# values (7, 'FOOD', '2023-08-12 12:00:00', 1);
# insert into board(board_id, category, schedule_date, room_id)
# values (8, 'SHOPPING', '2023-08-12 16:40:00', 1);
# insert into board(board_id, category, schedule_date, room_id)
# values (9, 'FOOD', '2023-08-12 18:00:00', 1);
# insert into board(board_id, category, schedule_date, room_id)
# values (10, 'FOOD', '2023-08-13 12:00:00', 1);
# insert into board(board_id, category, schedule_date, room_id)
# values (11, 'ETC', '2023-08-13 14:45:00', 1);
# insert into board(board_id, category, schedule_date, room_id)
# values (12, 'FOOD', '2023-08-13 18:00:00', 1);
# insert into board(board_id, category, schedule_date, room_id)
# values (13, 'FOOD', '2023-08-14 12:00:00', 1);
# insert into board(board_id, category, schedule_date, room_id)
# values (14, 'SHOPPING', '2023-08-14 17:25:00', 1);
# insert into board(board_id, category, schedule_date, room_id)
# values (15, 'FOOD', '2023-08-14 18:00:00', 1);;
#
# insert into schedule_info(board_id, latitude, longitude, place_address, place_name, schedule_etc, map_place_id)
# values (1, 34.123, 127.232, '경기 가평군 상면 축령로45번길 114-22', '워터파크 아이린 키즈풀빌라', '꼭 9일전에 예약할것', 4444);
# insert into schedule_info(board_id, latitude, longitude, place_address, place_name, schedule_etc, map_place_id)
# values (2, 34.123, 127.232, '경기 가평군 가평읍 북한강변로 1078-8', '남이금강막국수', null, 3333);
# insert into schedule_info(board_id, latitude, longitude, place_address, place_name, schedule_etc, map_place_id)
# values (3, 34.123, 127.232, '경기 가평군 상면 수목원로 209 1층', '본가곱돌숯불닭갈비', null, 2222);
# insert into schedule_info(board_id, latitude, longitude, place_address, place_name, schedule_etc, map_place_id)
# values (4, 34.123, 127.232, '경기 가평군 청평면 양진길 7', '도선재', null, 1111);
# insert into schedule_info(board_id, latitude, longitude, place_address, place_name, schedule_etc, map_place_id)
# values (5, 34.123, 127.232, '강원 춘천시 남산면 남이섬길 1', '남이섬', null, 5655);
# insert into schedule_info(board_id, latitude, longitude, place_address, place_name, schedule_etc, map_place_id)
# values (6, 34.123, 127.232, '경기 가평군 설악면 음동길 23', '금강막국수 가평막국수', null, 5678);
# insert into schedule_info(board_id, latitude, longitude, place_address, place_name, schedule_etc, map_place_id)
# values (7, 34.123, 127.232, '경기 가평군 조종면 청군로 1323', '진주회관', null, 3468);
# insert into schedule_info(board_id, latitude, longitude, place_address, place_name, schedule_etc, map_place_id)
# values (8, 34.123, 127.232, '경기 가평군 가평읍 장터2길 10', '가평잣고을전통시장', null, 3462);
# insert into schedule_info(board_id, latitude, longitude, place_address, place_name, schedule_etc, map_place_id)
# values (9, 34.123, 127.232, '경기 가평군 상면 행현리 384-2', '도담숯불닭갈비', null, 3457);
# insert into schedule_info(board_id, latitude, longitude, place_address, place_name, schedule_etc, map_place_id)
# values (10, 34.123, 127.232, '경기 가평군 설악면 자잠로 229', '스위티안 스테이크하우스', null, 3456);
# insert into schedule_info(board_id, latitude, longitude, place_address, place_name, schedule_etc, map_place_id)
# values (11, 34.123, 127.232, '경기 가평군 가평읍 북한강변로 813', '가평빠지', null, 2346);
# insert into schedule_info(board_id, latitude, longitude, place_address, place_name, schedule_etc, map_place_id)
# values (12, 34.123, 127.232, '경기 가평군 상면 수목원로 123', '가평달맞이빵', null, 2345);
# insert into schedule_info(board_id, latitude, longitude, place_address, place_name, schedule_etc, map_place_id)
# values (13, 34.123, 127.232, '경기 가평군 설악면 어비산길99번길 75-7 산골농원', '산골농원', null, 12356);
# insert into schedule_info(board_id, latitude, longitude, place_address, place_name, schedule_etc, map_place_id)
# values (14, 34.123, 127.232, '경기 가평군 가평읍 금대리 85-5', '클럽비발디 수상레저', null, 1235);
# insert into schedule_info(board_id, latitude, longitude, place_address, place_name, schedule_etc, map_place_id)
# values (15, 34.123, 127.232, '경기 가평군 가평읍 보납로 459-158 동기간', '동기간', null, 1234);
#
# insert into book_info(book_info_id, book_etc, is_booked)
# values (1, '예매 입금날짜가 다름', true);
# insert into book_info(book_info_id, book_etc, is_booked)
# values (2, null, false);
# insert into book_info(book_info_id, book_etc, is_booked)
# values (3, true, true);
# insert into book_info(book_info_id, book_etc, is_booked)
# values (4, '분할결제 확인바람', false);
# insert into book_info(book_info_id, book_etc, is_booked)
# values (5, '현장 결제임', true);
# insert into book_info(book_info_id, book_etc, is_booked)
# values (6, true, true);
#
# insert into accounting_info(accounting_info_id, accounting_etc, category, payment_method, payment_name, payment_time,
#                             price, board_id, book_info_id, room_id)
# values (1, null, 'ACCOMMODATION', 'CARD', null, '2023-04-10', 450000, 1, 1, 1);
# insert into accounting_info(accounting_info_id, accounting_etc, category, payment_method, payment_name, payment_time,
#                             price, board_id, book_info_id, room_id)
# values (2, null, 'FOOD', 'CARD', null, '2023-04-10', 170000, 2, 2, 1);
# insert into accounting_info(accounting_info_id, accounting_etc, category, payment_method, payment_name, payment_time,
#                             price, board_id, book_info_id, room_id)
# values (3, null, 'TOUR', 'CARD', null, '2023-04-12', 200000, 5, 3, 1);
# insert into accounting_info(accounting_info_id, accounting_etc, category, payment_method, payment_name, payment_time,
#                             price, board_id, book_info_id, room_id)
# values (4, null, 'FOOD', 'CARD', null, '2023-04-10', 120000, 10, 4, 1);
# insert into accounting_info(accounting_info_id, accounting_etc, category, payment_method, payment_name, payment_time,
#                             price, board_id, book_info_id, room_id)
# values (5, null, 'ETC', 'CREDIT', null, '2023-04-13', 290000, 11, 5, 1);
# insert into accounting_info(accounting_info_id, accounting_etc, category, payment_method, payment_name, payment_time,
#                             price, board_id, book_info_id, room_id)
# values (6, null, 'SHOPPING', 'CARD', null, '2023-05-10', 130000, 14, 6, 1);
#
# insert into travel_essential(travel_essential_id, category, is_checked, item_name, room_id, user_id)
# values (1, 'ABOARD', false, '차키', 1, 1);
# insert into travel_essential(travel_essential_id, category, is_checked, item_name, room_id, user_id)
#     values (2, 'CLOTHES', false, '예비 외투', 1, 1);
# insert into travel_essential(travel_essential_id, category, is_checked, item_name, room_id, user_id)
#     values (3, 'MEDICINE', false, '멀미약', 1, 1);
# insert into travel_essential(travel_essential_id, category, is_checked, item_name, room_id, user_id)
#     values (4, 'COOKWARE', false, '허브솔트', 1, 1);
#
# insert into want_place(place_id, category, latitude, link, longitude, lot_number_address, map_place_id, phone_number, place_address, place_name, room_id)
# values (1, '카테고리', 34.2131, 'https://www.naver.com', 127.2312, '서울 동작구 상도동 500-1', 123421, '010-1111-2222', '주소주소1', '테스트1', 1);
# insert into want_place(place_id, category, latitude, link, longitude, lot_number_address, map_place_id, phone_number, place_address, place_name, room_id)
# values (2, '카테고리', 34.2131, 'https://www.naver.com', 127.2312, '서울 동작구 상도동 500-2', 123421, '010-2222-3333', '주소주소2', '테스트2', 1);
# insert into want_place(place_id, category, latitude, link, longitude, lot_number_address, map_place_id, phone_number, place_address, place_name, room_id)
# values (3, '카테고리', 34.2131, 'https://www.naver.com', 127.2312, '서울 동작구 상도동 500-3', 123421, '010-4444-5555', '주소주소3', '테스트3', 1);