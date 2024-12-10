#!/usr/bin/env bash

# Installa e configura postgresql
apt-get install -qy postgresql
export postgres_user_password=$(cut -d "=" -f 2 postgresUserPassword)
echo postgres:$postgres_user_password | chpasswd
rm postgresUserPassword

