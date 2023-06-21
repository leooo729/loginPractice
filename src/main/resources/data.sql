-- 1.1 spring mvc 初始化練習，users 資料表
CREATE TABLE IF NOT EXISTS users ( id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(100) NOT NULL );

INSERT INTO users (name) VALUES ('John');
INSERT INTO users (name) VALUES ('Jane');

-- 2.1. spring mvc 初始化練習，member 資料表
CREATE TABLE IF NOT EXISTS member (
id INT AUTO_INCREMENT PRIMARY KEY
, email VARCHAR(60) UNIQUE NOT NULL
, email_code VARCHAR(20)
, email_code_expire TIMESTAMP
, name VARCHAR(100) NOT NULL
, address VARCHAR(200)
, birthday VARCHAR(8)
, phone VARCHAR(20) UNIQUE
, account VARCHAR(20)
, password VARCHAR(20)
, mobile_code VARCHAR(20)
, mobile_code_expire TIMESTAMP
, status INTEGER NOT NULL);