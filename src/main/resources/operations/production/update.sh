#!/usr/bin/env bash

source config.shlib; # load the config library functions

usage()
{
    echo "Usage"
    echo $0" [options]"
    echo
    echo "Options:"
    echo "  -a  --application           Update application"
    echo "  -d  --database              Update database"
    echo "  -l  --libraries             Update libraries"
    echo "  -A  --applicationserver     Update application server"
    echo "  -h  --help                  Read this help and exit"
    echo
}

updateApplication()
{
    echo "Updating application..."

    scp -P $(config_get SSH_PORT) $(config_get IDE_WORKSPACE)/$(config_get APP_NAME)/target/$(config_get APP_NAME).war root@$(config_get IP_ADDRESS):/home/$(config_get AS_USER_NAME)/

    # Rimuovere la vecchia applicazione
    ssh -p $(config_get SSH_PORT) $(config_get AS_USER_NAME)@$(config_get IP_ADDRESS) \
        AS_HOME=$(config_get AS_HOME) \
        APP_NAME=$(config_get APP_NAME) \
        AS_DOMAIN_NAME=$(config_get AS_DOMAIN_NAME) \
        'bash -s' < 0_undeployApp.sh

    # Riavviare il servizio che gestisce Payara per permettere alle macchine lente di poter subito fare il deploy
    #ssh -p $(config_get SSH_PORT) root@$(config_get IP_ADDRESS) 'bash -s' < 0_riavvioServizioPayara.sh

    # Pubblicare l'applicazione
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

    # Riavviare il servizio che gestisce Payara
    ssh -p $(config_get SSH_PORT) root@$(config_get IP_ADDRESS) 'bash -s' < 0_riavvioServizioPayara.sh
}

updateDatabase()
{
    echo "Updating database..."

    scp -P $(config_get SSH_PORT) $(config_get DB_INITIAL_FILENAME_COMPRESSED) postgres@$(config_get IP_ADDRESS):

    # Eliminare il database
    ssh -p $(config_get SSH_PORT) postgres@$(config_get IP_ADDRESS) \
        DB_NAME=$(config_get DB_NAME) \
        'bash -s' < 0_rimozioneDatabase.sh

    #Creare il database e copiarne i dati
    ssh -p $(config_get SSH_PORT) postgres@$(config_get IP_ADDRESS) \
        DB_USER_NAME=$(config_get DB_USER_NAME) \
        DB_NAME=$(config_get DB_NAME) \
        DB_INITIAL_FILENAME_COMPRESSED=$(config_get DB_INITIAL_FILENAME_COMPRESSED) \
        DB_INITIAL_FILENAME=$(config_get DB_INITIAL_FILENAME) \
        'bash -s' < 0_creazioneDatabase.sh

    # Riavviare il servizio che gestisce Payara
    ssh -p $(config_get SSH_PORT) root@$(config_get IP_ADDRESS) 'bash -s' < 0_riavvioServizioPayara.sh
}

updateLibraries()
{
    echo "Updating libraries..."

    # Rimuovere la vecchia applicazione
    ssh -p $(config_get SSH_PORT) $(config_get AS_USER_NAME)@$(config_get IP_ADDRESS) \
        AS_HOME=$(config_get AS_HOME) \
        APP_NAME=$(config_get APP_NAME) \
        AS_DOMAIN_NAME=$(config_get AS_DOMAIN_NAME) \
        'bash -s' < 0_undeployApp.sh

    mvn -f $(config_get IDE_WORKSPACE)/$(config_get APP_NAME)/pom.xml -DincludeScope=provided -DexcludeArtifactIds=javax.mail,jakarta.jakartaee-api,activation -DoutputDirectory=$(config_get IDE_WORKSPACE)/$(config_get APP_NAME)/target/libs-temp/ dependency:copy-dependencies
    scp -P $(config_get SSH_PORT) $(config_get IDE_WORKSPACE)/$(config_get APP_NAME)/target/libs-temp/* $(config_get AS_USER_NAME)@$(config_get IP_ADDRESS):

    # Rimuovere le vecchie librerie
    ssh -p $(config_get SSH_PORT) $(config_get AS_USER_NAME)@$(config_get IP_ADDRESS) \
        AS_HOME=$(config_get AS_HOME) \
        AS_DOMAIN_NAME=$(config_get AS_DOMAIN_NAME) \
        'bash -s' < 0_rimozioneLibreriePayara.sh

    # Riavviare il servizio che gestisce Payara
    ssh -p $(config_get SSH_PORT) root@$(config_get IP_ADDRESS) 'bash -s' < 0_riavvioServizioPayara.sh

    # Aggiungere le nuove librerie
    ssh -p $(config_get SSH_PORT) $(config_get AS_USER_NAME)@$(config_get IP_ADDRESS) \
        AS_HOME=$(config_get AS_HOME) \
        'bash -s' < 0_aggiuntaLibreriePayara.sh

    # Pubblicare l'applicazione
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

    # Riavviare il servizio che gestisce Payara
    ssh -p $(config_get SSH_PORT) root@$(config_get IP_ADDRESS) 'bash -s' < 0_riavvioServizioPayara.sh
}

updateApplicationServer()
{
    echo "Updating application server..."

    # Rimuovere la vecchia applicazione
    ssh -p $(config_get SSH_PORT) $(config_get AS_USER_NAME)@$(config_get IP_ADDRESS) \
        AS_HOME=$(config_get AS_HOME) \
        APP_NAME=$(config_get APP_NAME) \
        AS_DOMAIN_NAME=$(config_get AS_DOMAIN_NAME) \
        'bash -s' < 0_undeployApp.sh

    # Eliminare payara
    ssh -p $(config_get SSH_PORT) root@$(config_get IP_ADDRESS) 'rm -rf '$(config_get AS_DIR)

    # Riavviare il server per liberare la RAM
    ssh -p $(config_get SSH_PORT) root@$(config_get IP_ADDRESS) 'reboot & exit'

    # Wait for server to start up and then continue - solution found on https://serverfault.com/a/995377
    RESULT=1 # 0 upon success
    TIMEOUT=30 # number of iterations (5 minutes?)
    echo "Wait three seconds ..."
    sleep 3
    while :; do 
        echo "waiting for server ping ..."
        # https://serverfault.com/questions/152795/linux-command-to-wait-for-a-ssh-server-to-be-up
        # https://unix.stackexchange.com/questions/6809/how-can-i-check-that-a-remote-computer-is-online-for-ssh-script-access
        # https://stackoverflow.com/questions/1405324/how-to-create-a-bash-script-to-check-the-ssh-connection
        status=$(ssh -o ConnectTimeout=5 root@"$(config_get IP_ADDRESS)" -p "$(config_get SSH_PORT)" echo ok 2>&1)
        RESULT=$?
        if [ $RESULT -eq 0 ]; then
            # this is not really expected unless a key lets you log in
            echo "connected ok"
            break
        fi
        if [ $RESULT -eq 255 ]; then
            # connection refused also gets you here
            if [[ $status == *"Permission denied"* ]] ; then
                # permission denied indicates the ssh link is okay
                echo "server response found"
                break
            fi
        fi
        TIMEOUT=$((TIMEOUT-1))
        if [ $TIMEOUT -eq 0 ]; then
            echo "timed out"
            # error for jenkins to see
            exit 1 
        fi
        sleep 10
    done

    # Installare Payara
    ssh -p $(config_get SSH_PORT) root@$(config_get IP_ADDRESS) \
        AS_DIR=$(config_get AS_DIR) \
        AS_GROUP=$(config_get AS_GROUP) \
        AS_USER_NAME=$(config_get AS_USER_NAME) \
        AS_VERSION=$(config_get AS_VERSION) \
        'bash -s' < 0_installazionePayara.sh

    # Configurare Payara
    scp -P $(config_get SSH_PORT) $(config_get IDE_WORKSPACE)/Payara/appserver/admin/production_domain_template/target/production-domain.jar $(config_get AS_USER_NAME)@$(config_get IP_ADDRESS):$(config_get AS_HOME)/glassfish/common/templates/gf/
    scp -P $(config_get SSH_PORT) *Password $(config_get AS_USER_NAME)@$(config_get IP_ADDRESS):
    mvn -f $(config_get IDE_WORKSPACE)/$(config_get APP_NAME)/pom.xml -DincludeScope=provided -DexcludeArtifactIds=javax.mail,jakarta.jakartaee-api,activation -DoutputDirectory=$(config_get IDE_WORKSPACE)/$(config_get APP_NAME)/target/libs-temp/ dependency:copy-dependencies
    scp -P $(config_get SSH_PORT) $(config_get IDE_WORKSPACE)/$(config_get APP_NAME)/target/libs-temp/* $(config_get AS_USER_NAME)@$(config_get IP_ADDRESS):
    ssh -p $(config_get SSH_PORT) $(config_get AS_USER_NAME)@$(config_get IP_ADDRESS) \
        AS_HOME=$(config_get AS_HOME) \
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

    # Avviare il Payara tramite il servizio
    ssh -p $(config_get SSH_PORT) root@$(config_get IP_ADDRESS) 'systemctl start payara'

    # Pubblicare l'applicazione
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
    ssh -p $(config_get SSH_PORT) root@$(config_get IP_ADDRESS) 'reboot & exit'
}

prompt_confirm() {
  while true; do
    read -r -p "${1:-Do you want to continue?} [y/N]: " response
    response=${response,,}    # tolower
    if [[ "$response" =~ ^(yes|y)$ ]]
        then
            return 0
        else
            return 1
    fi
  done  
}

update_application=false
update_database=false
update_libraries=false
update_application_server=false

if [ $# -ne 0 ]; then
    echo Target system is $(config_get IP_ADDRESS)
    prompt_confirm || exit 0
    for arg
    do
        case "$arg" in
            -a | --application)
                update_application=true
                ;;
            -d | --database)
                update_database=true
                ;;
            -l | --libraries)
                update_libraries=true
                ;;
            -A | --applicationserver)
                update_application_server=true
                ;;
            -h | --help)
                usage
                ;;
            *)
                echo "Unknown option "\"$arg\"
                echo
                usage
                exit 1
                ;;
         esac
    done
    if [ "$update_application_server" = true ]; then
        updateApplicationServer
    else
        if [ "$update_libraries" = true ]; then
            updateLibraries
        fi
        if [ "$update_database" = true ]; then
            updateDatabase
        fi
    fi
    if [ "$update_application" = true ]; then
        if [ "$update_database" = true ]; then
            echo "Just give Payara 30 seconds to start..."
            sleep 30    # Aspetta che payara si riavvii dopo aver aggiornato il database
        fi
        updateApplication
    fi
else
    echo "Must provide an option"
    echo
    usage
    exit 1
fi

exit 0

