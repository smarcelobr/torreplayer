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

## Tocar um MP3 com o cvlc

```bash
cvlc --play-and-exit --no-video-title-show file:///home/pi/Music/05\ Faixa\ 5.mp3
cvlc --play-and-exit --no-video-title-show 'Music/populares/47-Attention - (Atención).mp3'
```

Se for uma playlist:

```bash
cvlc --play-and-exit --no-video-title-show --random /home/pi/Music/pop.m3u
```

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
