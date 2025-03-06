#!/usr/bin/env bash

# Crea l'utente che gestirà il server
apt-get install -qy sudo
addgroup ${AS_GROUP}
adduser --disabled-login --shell /bin/bash --gecos "" --ingroup ${AS_GROUP} ${AS_USER_NAME}
usermod -a -G sudo ${AS_USER_NAME}
export as_user_password=$(cut -d "=" -f 2 asUserPassword)
echo ${AS_USER_NAME}:$as_user_password | chpasswd
rm asUserPassword
mkdir -p /home/${AS_USER_NAME}/${APP_NAME}/documents
chown -R ${AS_USER_NAME}:${AS_GROUP} /home/${AS_USER_NAME}/${APP_NAME}

