INSERT INTO users(userId, archive, authority, email, enabled, password, photo, username) 
SELECT 1, 0, 'ROLE_ADMIN', 'admin@gmail.com', 1, 'admin', NULL, 'admin'
WHERE NOT EXISTS 
(SELECT 1 FROM users WHERE userId = 1);

INSERT INTO authorities (id, archive, authority, username) 
SELECT 1, 0, 'ROLE_ADMIN', 'admin'
WHERE NOT EXISTS 
(SELECT 1 FROM authorities WHERE username = 'admin');

INSERT INTO admin (adminId, adminName, archive, email, password, registerDate, status, username, address, mobile) 
SELECT 1, 'Admin', 0, 'admin@gmail.com', 'admin', '2018-02-21', 1, 'admin', 'Dhaka', '01515634889'
WHERE NOT EXISTS 
(SELECT 1 FROM admin WHERE username = 'admin'); 
