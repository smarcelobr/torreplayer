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
java -jar musica.jar
```



