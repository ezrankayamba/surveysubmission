--Roles
INSERT INTO `survey`.`tbl_role` (id,`name`, `description`) VALUES (1,'Administrator', 'The system administrator') ON DUPLICATE KEY UPDATE name=name;
INSERT INTO `survey`.`tbl_role` (id, `name`, `description`) VALUES (2, 'Customer', 'The customer login') ON DUPLICATE KEY UPDATE name=name;
INSERT INTO `survey`.`tbl_role` (id, `name`, `description`) VALUES (3, 'API', 'Api credentials') ON DUPLICATE KEY UPDATE name=name;

--Users | default pwd: pwd@123
INSERT INTO `survey`.`tbl_user` (`username`, `password`, role_id,enabled) VALUES ('admin@naninegw.net', '$2a$11$K8xOc/PcWVdrDW09k2kTaO8jSGJcCGY6QZZXgbUJmyh2qOuaECHq2',1,1) ON DUPLICATE KEY UPDATE username=username;
INSERT INTO `survey`.`tbl_user` (`username`, `password`, role_id, enabled) VALUES ('customer@naninegw.net', '$2a$11$K8xOc/PcWVdrDW09k2kTaO8jSGJcCGY6QZZXgbUJmyh2qOuaECHq2',2,1) ON DUPLICATE KEY UPDATE username=username;
INSERT INTO `survey`.`tbl_user` (`username`, `password`, role_id, enabled) VALUES ('test.api', '$2a$11$K8xOc/PcWVdrDW09k2kTaO8jSGJcCGY6QZZXgbUJmyh2qOuaECHq2',3,1) ON DUPLICATE KEY UPDATE username=username;
