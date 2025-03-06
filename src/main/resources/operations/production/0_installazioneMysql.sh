#!/usr/bin/env bash

# Installa e configura MySql
apt-get install -qy mariadb-server
systemctl stop mariadb
export mysql_user_password=$(cut -d "=" -f 2 mysqlUserPassword)
echo mysql:$mysql_user_password | chpasswd
mkdir /home/mysql
usermod -s /bin/bash -d /home/mysql mysql
chown -R mysql:mysql /home/mysql/
rm mysqlUserPassword
systemctl start mariadb
