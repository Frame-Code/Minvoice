
insert into item (code,[name], price) values
('001', 'Servicios prestados programacion', '400'),
('002', 'Programacion servicios prestados', '200'),
('003', 'Desarrollo software', '400')

insert into general_status values
('pnd', 'Pendiente', 'invoice'),
('cnd', 'Cancelada', 'invoice'),
('pp', 'Parcialmente pagada', 'invoice'),
('bkt', 'Transferencia bancaria', 'payment'),
('chq', 'Cheque', 'payment')
