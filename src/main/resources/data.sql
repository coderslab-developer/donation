INSERT INTO USERS(userId, archive, authority, email, enabled, password, photo, username) 
SELECT 1, 0, 'ROLE_ADMIN', 'admin@gmail.com', 1, '1234', NULL, 'admin'
WHERE NOT EXISTS 
(SELECT 1 FROM USERS WHERE userId = 1);

INSERT INTO AUTHORITIES (id, archive, authority, username) 
SELECT 1, 0, 'ROLE_ADMIN', 'admin'
WHERE NOT EXISTS 
(SELECT 1 FROM AUTHORITIES WHERE username = 'admin');

INSERT INTO ADMIN (adminId, adminName, archive, email, password, registerDate, status, username) 
SELECT 1, 'Admin', 0, 'admin@gmail.com', '1234', '2018-02-21', 1, 'admin'
WHERE NOT EXISTS 
(SELECT 1 FROM ADMIN WHERE username = 'admin'); 
