run xampp
sudo /opt/lampp/manager-linux-x64.run


POSTMAN
connect mysql to postman
https://github.com/o1lab/xmysql?tab=readme-ov-file
xmysql -h localhost -u mysqlUsername -p mysqlPassword -d databaseName
xmysql -h localhost -u root -d registration_db

run http://localhost:3000 in browser

Add new user
POST http://localhost:9191/register
body -> raw
{
    "firstName" : "Name",
    "lastName" : "Last Name",
    "email" : "test@gmail.com",
    "password" : "123456",
    "role" : "USER"
}


to login
http://localhost:9191/login
