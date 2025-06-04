# Torre Player

## Instalação no Raspberry Pi

### Pré-requisitos

- Raspberry Pi 3 ou superior
- JAVA 11 ou superior

### Instalação do Java

```bash
sudo apt-get update
sudo sudo apt-get openjdk-17-jre
```
### Raspberry PI 3 antigo usa omxplayer

```properties
torre.cmd.play=omxplayer,-o,local,--vol,#musicaVolume,#musicaPath
torre.cmd.spaceEscape=\ 
torre.cmd.stop.mode=SEND_KEYS
torre.cmd.stop.keys=q
```

### Raspberry PI 3 novo usa cvlc

```properties
torre.cmd.play=cvlc,--play-and-exit,--no-video-title-show,--volume,#musicaVolume,file://#musicaPath
torre.cmd.spaceEscape=\ 
torre.cmd.stop.mode=KILL_PROCESS
```

## No Windows, pode usar o PowerShell

```properties
torre.cmd.play=powershell,-c,(New-Object Media.SoundPlayer '#musicaPath').PlaySync();
torre.cmd.spaceEscape=\ 
torre.cmd.stop.mode=KILL_PROCESS
```

### Instalação do Torre Player

1. Faça o build do projeto com o comando:

```bash 
./mvnw clean package -DskipTests
```

2. Copie para o Raspberry Pi o arquivo `target/torre-player-1.0.0-SNAPSHOT.jar` e os scripts para a pasta `/opt/torreplayer`.

O pom.xml tem um plugin para fazer o upload do arquivo jar para o servidor.

```bash
./mvnw wagon:upload-single@upload-jar wagon:upload@upload-scripts
```

3. Execute o comando no linux (para testes):

```bash
java -Dspring.profile.active=nave -jar /opt/torreplayer/musica.jar
```

## App as a Service no Raspberry Pi

    Cria grupo 
    $ sudo groupadd appjava
    Cria usuário (system) sem login e nem home
    $ sudo adduser appmusica --system
    Adiciona o usuário ao grupo.
    $ sudo adduser appmusica appjava
    $ sudo adduser <seu_user> appjava

Criar arquivo `/etc/sudoers.d/030_musica`:
```
appmusica       ALL=(ALL)       NOPASSWD: ALL
```

### MUSICA EM /OPT

```
$ sudo mkdir /opt/torreplayer
$ sudo chown appmusica:appjava /opt/torreplayer
```

Script `/opt/torreplayer/start.sh` bash para iniciar o java:

``` 
#!/bin/bash

echo INICIANDO MUSICA

JAVA_HOME=/usr/lib/jvm/java-17-openjdk-armhf
WORKDIR=/opt/torreplayer
JAVA_OPTIONS=" -Xms512m -Xmx768m -server -Dspring.profiles.active=nave"
# APP_OPTIONS=" -c /path/to/app.config -d /path/to/datadir "
APP_OPTIONS=""

cd $WORKDIR || exit
"${JAVA_HOME}/bin/java" $JAVA_OPTIONS -jar musica.jar $APP_OPTIONS
```

Dê permissão de execução ao script:
```
$ sudo chmod a+x /opt/torreplayer/start.sh
```

### MUSICA AS A LINUX SERVICE

Configurado no systemd para iniciar automaticamente ao iniciar o raspberry:
(ref.: https://www.baeldung.com/linux/run-java-application-as-service)
(ref.: https://www.auroria.io/spring-boot-as-systemd-service/)

Crie o arquivo `/etc/systemd/system/musica.service`:
```
[Unit]
Description=MUSICA
After=syslog.target network.target

[Service]
SuccessExitStatus=143

User=appmusica
Group=appjava

Type=forking

ExecStart=/opt/torreplayer/start.sh
ExecStop=/bin/kill -15 $MAINPID

[Install]
WantedBy=multi-user.target
```

Depois que colocar o 'musica.service' na pasta, registre-o:

    $ sudo systemctl daemon-reload

Pode startar e stopar agora:

    $ sudo systemctl start musica.service
    $ sudo systemctl status musica.service
    $ sudo systemctl stop musica.service
    $ sudo systemctl status musica.service

para ver o log:

    $ journalctl -xeu musica.service -f


para iniciar sempre que for ligado:

    $ sudo systemctl enable musica.service



