
insert into T_APPLICATION(code) values('RADAR');
insert into T_APPLICATION(code) values('SLA');

insert into T_USER(login, password) values('olivier.terrien', 'fb498295cc2562c653db9877d5b6b374f47247be19e659d0b7806e958313a25115c38d36b301fa10d0ab6ed2c761ec8a7eca618c109a029126f5c7fd31e1efb6');//password

insert into T_PRIVILEGE(code) values('READ_ONLY');
insert into T_PRIVILEGE(code) values('READ_WRITE');

insert into T_PERIMETER(code) values('DEAL');
insert into T_PERIMETER(code, parent_id) values('GLE', (select id from T_PERIMETER where code = 'DEAL'));

insert into T_PERIMETER_PRIVILEGE(perimeter_id, privilege_id) values ((select id from T_PERIMETER where code='DEAL'), (select id from T_PRIVILEGE where code='READ_ONLY'));
insert into T_PERIMETER_PRIVILEGE(perimeter_id, privilege_id) values ((select id from T_PERIMETER where code='GLE'), (select id from T_PRIVILEGE where code='READ_WRITE'));

insert into T_USER_RIGHT(user_id, application_id) values ((select id from T_USER where login = 'olivier.terrien'), (select id from T_APPLICATION where code = 'SLA'));

insert into T_USER_RIGHT_PERIMETER(user_right_id, perimeter_id) values ((select id from T_USER_RIGHT where user_id = (select id from T_USER where login = 'olivier.terrien')), (select id from T_PERIMETER where code ='DEAL'));
insert into T_USER_RIGHT_PERIMETER(user_right_id, perimeter_id) values ((select id from T_USER_RIGHT where user_id = (select id from T_USER where login = 'olivier.terrien')), (select id from T_PERIMETER where code ='GLE'));
