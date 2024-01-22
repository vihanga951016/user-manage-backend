insert into permissions(id, permission)
values (1, 'add');
insert into permissions(id, permission)
values (2, 'update');
insert into permissions(id, permission)
values (3, 'delete');
insert into permissions(id, permission)
values (4, 'view');
insert into permissions(id, permission)
values (5, 'all');

insert into roles(id, role) values (1, 'SUPER_ADMIN');
insert into roles(id, role) values (2, 'ADMIN');
insert into roles(id, role) values (3, 'USER');

insert into user_permissions(id, permission_id, role_id)
values (1, 5, 1);
insert into user_permissions(id, permission_id, role_id)
values (2, 1, 2);
insert into user_permissions(id, permission_id, role_id)
values (3, 2, 2);
insert into user_permissions(id, permission_id, role_id)
values (4, 4, 2);
insert into user_permissions(id, permission_id, role_id)
values (5, 4, 3);
