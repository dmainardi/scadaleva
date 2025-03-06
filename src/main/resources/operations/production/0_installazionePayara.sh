#!/usr/bin/env bash

# Installa Payara
mkdir -p ${AS_DIR}
cd ${AS_DIR}
wget https://repo1.maven.org/maven2/fish/payara/distributions/payara/${AS_VERSION}/payara-${AS_VERSION}.zip
unzip payara-${AS_VERSION}.zip
rm payara-${AS_VERSION}.zip
chown -R ${AS_USER_NAME}:${AS_GROUP} ${AS_DIR}

