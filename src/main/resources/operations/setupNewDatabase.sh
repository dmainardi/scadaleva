#!/bin/sh

readonly APP_NAME=scadaleva
readonly DB_NAME="${APP_NAME}"
readonly DB_USER_NAME="${DB_NAME}"

createuser -d -P scadaleva
createdb --owner=scadaleva --encoding=UTF8 scadaleva
