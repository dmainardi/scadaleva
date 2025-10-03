#!/usr/bin/env bash

# Install software
apt-get -qy install cifs-utils

# Create folder that will be used to mount SBM shared folder
mkdir -p /home/${AS_USER_NAME}/${APP_NAME}/${DB_ACCESS_READ_ONLY_FOLDER_NAME}
chown -R ${AS_USER_NAME}:${AS_GROUP} /home/${AS_USER_NAME}/${APP_NAME}/${DB_ACCESS_READ_ONLY_FOLDER_NAME}
chmod -R 770 /home/${AS_USER_NAME}/${APP_NAME}/${DB_ACCESS_READ_ONLY_FOLDER_NAME}
mv accessCredential /home/${AS_USER_NAME}/
chown -R ${AS_USER_NAME}:${AS_GROUP} /home/${AS_USER_NAME}/accessCredential
chmod 400 /home/${AS_USER_NAME}/accessCredential

tee -a /etc/fstab > /dev/null <<EOT

//${IP_ACCESS_READ_ONLY_ADDRESS}/ma-164_scadasync  /home/${AS_USER_NAME}/${APP_NAME}/${DB_ACCESS_READ_ONLY_FOLDER_NAME}  cifs credentials=/home/${AS_USER_NAME}/accessCredential,x-systemd.automount,vers=3.0,iocharset=utf8,file_mode=0755,dir_mode=0755  0  0
EOT
