-- Create User
CREATE USER db2markdownuser WITH PASSWORD 'db2markdown123';

-- Create Database and assign owner
CREATE DATABASE db2markdown OWNER db2markdownuser;
GRANT ALL PRIVILEGES ON DATABASE db2markdown TO db2markdownuser;
