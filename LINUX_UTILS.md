# Comandos úteis para Linux

## Listar os dispositivos de áudio:

```bash
pi@gregorio:~/Music $ aplay -l
**** List of PLAYBACK Hardware Devices ****
card 0: vc4hdmi [vc4-hdmi], device 0: MAI PCM i2s-hifi-0 [MAI PCM i2s-hifi-0]
Subdevices: 1/1
Subdevice #0: subdevice #0
card 1: Headphones [bcm2835 Headphones], device 0: bcm2835 Headphones [bcm2835 Headphones]
Subdevices: 8/8
Subdevice #0: subdevice #0
Subdevice #1: subdevice #1
Subdevice #2: subdevice #2
Subdevice #3: subdevice #3
Subdevice #4: subdevice #4
Subdevice #5: subdevice #5
Subdevice #6: subdevice #6
Subdevice #7: subdevice #7
pi@gregorio:~/Music $
```

## Alterar o volume do áudio:

```bash
pi@gregorio:~/Music $ amixer --card 1 controls
numid=2,iface=MIXER,name='PCM Playback Switch'
numid=1,iface=MIXER,name='PCM Playback Volume'
pi@gregorio:~/Music $ sudo amixer --card 1 cset numid=1 70%
numid=1,iface=MIXER,name='PCM Playback Volume'
  ; type=INTEGER,access=rw---R--,values=1,min=-10239,max=400,step=0
  : values=-2792
  | dBscale-min=-102.39dB,step=0.01dB,mute=1
```

## Tocar um MP3 com o `cvlc` ou `vlc -I dummy`

```bash
cvlc --no-dbus --play-and-exit --no-video-title-show file:///home/pi/Music/05\ Faixa\ 5.mp3
vlc -I dummy --play-and-exit --no-video-title-show 'Music/populares/47-Attention - (Atención).mp3'
```

Se for uma playlist:

```bash
cvlc --play-and-exit --no-video-title-show --random /home/pi/Music/pop.m3u
```

## Tocar um MP3 com o `vlc -I rc`

    vlc -I rc --no-dbus --play-and-exit --no-video-title-show file:///home/pi/Music/05\ Faixa\ 5.mp3

Com isso, você pode enviar comandos para o VLC, através da linha de comando.

## Gerenciando processos no Linux

Para listar todos os processos:

```bash
ps -a
```

Para encerrar um processo no Linux sabendo apenas o nome, basta usar o comando:

```bash
pkill -SIGTERM java
```
Se quiser encerrá-lo remotamente, usando o ssh:

```bash
ssh pi@gregorio.local "pkill -SIGTERM java"
```

## Copiando arquivos do PC Windows para o Raspberry Pi via scp

```cmd
scp D:\code\github\smarcelobr\torreplayer\src\main\scripts\tocaMusica.sh pi@gregorio.local:/opt/torreplayer
```
## Iniciando o VLC no cron (do usuário) ao iniciar o raspberry

Inicia o VLC no modo HTTP, com a playlist carregada a partir da pasta.

    $ crontab -e

Incluir a seguinte linha:

    @reboot vlc --no-dbus -I http --http-password=abc123 --http-port=20000 --file-logging --logfile=/opt/musica/log/vlc.log --logmode=text --no-playlist-autostart --play-and-stop /opt/musica/midias/Brasileiras/* /opt/musica/midias/Canto\ Gregoriano/*


## Rede Wifi

Listar as redes wifi disponíveis:

    $ sudo nmcli dev wifi list

Criar uma conexão para uma rede wifi:

    $ sudo nmcli dev wifi connect “SEU_SSID” password "SUA_SENHA" 

ou, se quiser que pergunte a senha:

    $ sudo nmcli dev wifi connect “SEU_SSID” -a 

Para listas as conexões registradas:

    $ sudo nmcli c show

Para ativar um wifi específico:

    $ sudo nmcli c up “NOME_CONEXAO”