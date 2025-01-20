#!/bin/sh

readonly APP_NAME=scadaleva
readonly DB_NAME="${APP_NAME}"
readonly DB_USER_NAME="${DB_NAME}"
readonly DB_USER_PASSWORD=123Stella!!!

mysql

CREATE DATABASE scadaleva;
CREATE USER scadaleva IDENTIFIED BY '123Stella!!!';
GRANT ALL PRIVILEGES ON scadaleva.* TO scadaleva;

\q