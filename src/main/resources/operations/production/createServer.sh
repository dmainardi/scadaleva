#!/usr/bin/env bash

source config.shlib; # load the config library functions

# Scaricare Debian 12
# impostare locale, tzdata, hostname, aggiornare
# copiare la chiave pubblica sul server (utente root) in modo che non sia necessario accedere con la password

# Creare l'utente che gestirà il server
scp -P $(config_get SSH_PORT) asUserPassword root@$(config_get IP_ADDRESS):
ssh -p $(config_get SSH_PORT) root@$(config_get IP_ADDRESS) \
    AS_GROUP=$(config_get AS_GROUP) \
    AS_USER_NAME=$(config_get AS_USER_NAME) \
    APP_NAME=$(config_get APP_NAME) \
    'bash -s' < 0_creazioneUtente.sh
ssh-copy-id -p $(config_get SSH_PORT) $(config_get AS_USER_NAME)@$(config_get IP_ADDRESS)

# Installare e configurare mysql
scp -P $(config_get SSH_PORT) mysqlUserPassword root@$(config_get IP_ADDRESS):
ssh -p $(config_get SSH_PORT) root@$(config_get IP_ADDRESS) \
    'bash -s' < 0_installazioneMysql.sh
ssh-copy-id -p $(config_get SSH_PORT) mysql@$(config_get IP_ADDRESS)

# Creare la cartella condivisa per la connessione con Access
#scp -P $(config_get SSH_PORT) accessCredential root@$(config_get IP_ADDRESS):
#ssh -p $(config_get SSH_PORT) root@$(config_get IP_ADDRESS) \
#    AS_USER_NAME=$(config_get AS_USER_NAME) \
#    APP_NAME=$(config_get APP_NAME) \
#    DB_ACCESS_READ_ONLY_FOLDER_NAME=$(config_get DB_ACCESS_READ_ONLY_FOLDER_NAME) \
#    IP_ACCESS_READ_ONLY_ADDRESS=$(config_get IP_ACCESS_READ_ONLY_ADDRESS) \
#    'bash -s' < 0_creazioneCartellaCondivisa.sh

# Installare Java 17 (predefinito) se si usa Debian 12
ssh -p $(config_get SSH_PORT) root@$(config_get IP_ADDRESS) 'apt-get -qy install unzip default-jdk-headless'

# Installare Payara
ssh -p $(config_get SSH_PORT) root@$(config_get IP_ADDRESS) \
    AS_DIR=$(config_get AS_DIR) \
    AS_GROUP=$(config_get AS_GROUP) \
    AS_USER_NAME=$(config_get AS_USER_NAME) \
    AS_VERSION=$(config_get AS_VERSION) \
    'bash -s' < 0_installazionePayara.sh

# Creare l'utente del database di produzione
scp -P $(config_get SSH_PORT) db*UserPassword mysql@$(config_get IP_ADDRESS):
ssh -p $(config_get SSH_PORT) mysql@$(config_get IP_ADDRESS) \
    DB_USER_NAME=$(config_get DB_USER_NAME) \
    DB_NAME=$(config_get DB_NAME) \
    DB_ACCOUNTING_USER_NAME=$(config_get DB_ACCOUNTING_USER_NAME) \
    'bash -s' < 0_creazioneUtenteDatabase.sh

# Creare il database di produzione e copiarne i dati
scp -P $(config_get SSH_PORT) $(config_get DB_INITIAL_FILENAME_COMPRESSED) mysql@$(config_get IP_ADDRESS):
ssh -p $(config_get SSH_PORT) mysql@$(config_get IP_ADDRESS) \
    DB_USER_NAME=$(config_get DB_USER_NAME) \
    DB_ACCOUNTING_USER_NAME=$(config_get DB_ACCOUNTING_USER_NAME) \
    DB_NAME=$(config_get DB_NAME) \
    DB_INITIAL_FILENAME_COMPRESSED=$(config_get DB_INITIAL_FILENAME_COMPRESSED) \
    DB_INITIAL_FILENAME=$(config_get DB_INITIAL_FILENAME) \
    'bash -s' < 0_creazioneDatabase.sh

# Configurare Payara
scp -P $(config_get SSH_PORT) $(config_get IDE_WORKSPACE)/Payara/appserver/admin/production_domain_template/target/production-domain.jar $(config_get AS_USER_NAME)@$(config_get IP_ADDRESS):$(config_get AS_HOME)/glassfish/common/templates/gf/
scp -P $(config_get SSH_PORT) *Password $(config_get AS_USER_NAME)@$(config_get IP_ADDRESS):
mvn -f $(config_get IDE_WORKSPACE)/$(config_get APP_NAME)/pom.xml -DincludeScope=provided -DexcludeGroupIds=jakarta.activation,jakarta.annotation,jakarta.authentication,jakarta.authorization,jakarta.batch,jakarta.ejb,jakarta.el,jakarta.enterprise,jakarta.faces,jakarta.inject,jakarta.interceptor,jakarta.jms,jakarta.json,jakarta.json.bind,jakarta.mail,jakarta.persistence,jakarta.platform,jakarta.resource,jakarta.security.enterprise,jakarta.servlet,jakarta.transaction,jakarta.validation,jakarta.websocket,jakarta.ws.rs -DoutputDirectory=$(config_get IDE_WORKSPACE)/$(config_get APP_NAME)/target/libs-temp/ dependency:copy-dependencies
scp -P $(config_get SSH_PORT) $(config_get IDE_WORKSPACE)/$(config_get APP_NAME)/target/libs-temp/* $(config_get AS_USER_NAME)@$(config_get IP_ADDRESS):
ssh -p $(config_get SSH_PORT) $(config_get AS_USER_NAME)@$(config_get IP_ADDRESS) \
    AS_HOME=$(config_get AS_HOME) \
    AS_USER_NAME=$(config_get AS_USER_NAME) \
    AS_DOMAIN_NAME=$(config_get AS_DOMAIN_NAME) \
    AS_PASSWORD_ALIAS_NAME=$(config_get AS_PASSWORD_ALIAS_NAME) \
    DB_USER_NAME=$(config_get DB_USER_NAME) \
    DB_NAME=$(config_get DB_NAME) \
    TCP_PORT=$(config_get TCP_PORT) \
    DB_READ_ONLY_USER_NAME=$(config_get DB_READ_ONLY_USER_NAME) \
    DB_READ_ONLY_USER_PASSWORD_ALIAS_NAME=$(config_get DB_READ_ONLY_USER_PASSWORD_ALIAS_NAME) \
    DB_READ_ONLY_NAME=$(config_get DB_READ_ONLY_NAME) \
    IP_READ_ONLY_ADDRESS=$(config_get IP_READ_ONLY_ADDRESS) \
    TCP_READ_ONLY_PORT=$(config_get TCP_READ_ONLY_PORT) \
    APP_NAME=$(config_get APP_NAME) \
    'bash -s' < 0_configurazionePayara.sh

# Fermare Payara e rendere l'utente che gestirà il server il proprietario dei file
ssh -p $(config_get SSH_PORT) root@$(config_get IP_ADDRESS) \
    AS_HOME=$(config_get AS_HOME) \
    AS_DOMAIN_NAME=$(config_get AS_DOMAIN_NAME) \
    AS_USER_NAME=$(config_get AS_USER_NAME) \
    AS_GROUP=$(config_get AS_GROUP) \
    AS_DIR=$(config_get AS_DIR) \
    'bash -s' < 0_permessiFilePayara.sh

# Creare il servizio in modo che Payara possa avviarsi ed arrestarsi insieme alla macchina
cp payara.service.template payara.service
ESCAPED_AS_DOMAIN_NAME=$(printf '%s\n' "$(config_get AS_DOMAIN_NAME)" | sed -e 's/[\/&]/\\&/g')
sed -i s/AS_DOMAIN_NAME/$ESCAPED_AS_DOMAIN_NAME/g payara.service
ESCAPED_AS_USER_NAME=$(printf '%s\n' "$(config_get AS_USER_NAME)" | sed -e 's/[\/&]/\\&/g')
sed -i s/AS_USER_NAME/$ESCAPED_AS_USER_NAME/g payara.service
ESCAPED_AS_GROUP=$(printf '%s\n' "$(config_get AS_GROUP)" | sed -e 's/[\/&]/\\&/g')
sed -i s/AS_GROUP/$ESCAPED_AS_GROUP/g payara.service
ESCAPED_AS_HOME=$(printf '%s\n' "$(config_get AS_HOME)" | sed -e 's/[\/&]/\\&/g')
sed -i s/AS_HOME/$ESCAPED_AS_HOME/g payara.service
scp -P $(config_get SSH_PORT) payara.service root@$(config_get IP_ADDRESS):/etc/systemd/system/

# Abilitare e avviare il servizio in modo che Payara possa avviarsi ed arrestarsi insieme alla macchina
ssh -p $(config_get SSH_PORT) root@$(config_get IP_ADDRESS) 'systemctl enable payara; systemctl start payara'

# Pubblicare l'applicazione
scp -P $(config_get SSH_PORT) $(config_get IDE_WORKSPACE)/$(config_get APP_NAME)/target/$(config_get APP_NAME).war root@$(config_get IP_ADDRESS):/home/$(config_get AS_USER_NAME)/
ssh -p $(config_get SSH_PORT) root@$(config_get IP_ADDRESS) \
    AS_HOME=$(config_get AS_HOME) \
    APP_NAME=$(config_get APP_NAME) \
    AS_USER_NAME=$(config_get AS_USER_NAME) \
    'bash -s' < 0_deployApp.sh

# Fermare Payara e rendere l'utente che gestirà il server il proprietario dei file
ssh -p $(config_get SSH_PORT) root@$(config_get IP_ADDRESS) \
    AS_HOME=$(config_get AS_HOME) \
    AS_DOMAIN_NAME=$(config_get AS_DOMAIN_NAME) \
    AS_USER_NAME=$(config_get AS_USER_NAME) \
    AS_GROUP=$(config_get AS_GROUP) \
    AS_DIR=$(config_get AS_DIR) \
    'bash -s' < 0_permessiFilePayara.sh

# Riavviare il server
ssh -p $(config_get SSH_PORT) root@$(config_get IP_ADDRESS) 'reboot && exit'

# Ricordarsi di mettere in sicurezza MariaDb col comando (da fare in modo interrattivo)
#ssh -p $(config_get SSH_PORT) $(config_get AS_USER_NAME)@$(config_get IP_ADDRESS)
#sudo mysql_secure_installation
