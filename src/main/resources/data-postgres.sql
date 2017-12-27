/*
insert into T_APPLICATION(code) values('RADAR');
insert into T_APPLICATION(code) values('SLA');

insert into T_USER(login, password) values('olivier.terrien', '5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8');

insert into T_PRIVILEGE(code) values('READ_ONLY');
insert into T_PRIVILEGE(code) values('READ_WRITE');

insert into T_PERIMETER(code) values('DEAL');
insert into T_PERIMETER(code, parent_id) values('GLE', (select id from T_PERIMETER where code = 'DEAL'));

insert into T_PERIMETER_PRIVILEGE(perimeter_id, privilege_id) values ((select id from T_PERIMETER where code='DEAL'), (select id from T_PRIVILEGE where code='READ_ONLY'));
insert into T_PERIMETER_PRIVILEGE(perimeter_id, privilege_id) values ((select id from T_PERIMETER where code='GLE'), (select id from T_PRIVILEGE where code='READ_WRITE'));

insert into T_USER_RIGHT(user_id, application_id) values ((select id from T_USER where login = 'olivier.terrien'), (select id from T_APPLICATION where code = 'SLA'));

insert into T_USER_RIGHT_PERIMETER(user_right_id, perimeter_id) values ((select id from T_USER_RIGHT where user_id = (select id from T_USER where login = 'olivier.terrien')), (select id from T_PERIMETER where code ='DEAL'));
insert into T_USER_RIGHT_PERIMETER(user_right_id, perimeter_id) values ((select id from T_USER_RIGHT where user_id = (select id from T_USER where login = 'olivier.terrien')), (select id from T_PERIMETER where code ='GLE'));
*/
