INSERT INTO USERS() 
SELECT 
WHERE NOT EXISTS 
(SELECT 1 FROM USERS WHERE userId = 1);

INSERT INTO AUTHORITIES () 
SELECT 
WHERE NOT EXISTS 
(SELECT 'admin' FROM AUTHORITIES WHERE username = 'admin'); 
