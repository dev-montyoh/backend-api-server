create database `payment`;
create user `payment` identified by 'welcome';
grant all privileges on `payment`.* to `payment`;
grant super on *.* to `payment`;