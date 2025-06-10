#!/bin/bash

# garante que quando este script for encerrado forçadamente, o
# cvlc (subprocesso) também seja encerrado.
trap "pkill vlc" SIGINT SIGHUP SIGTERM EXIT

# Verifica se o número de parâmetros está correto (2 ou 3)
if [ $# -lt 2 ] || [ $# -gt 3 ]; then
    echo "Erro: Número incorreto de parâmetros"
    echo "Uso: $0 <volume> <caminho-da-musica> [random]"
    echo "Exemplo: $0 70 'Music/populares/musica.mp3'"
    echo "Exemplo com modo aleatório: $0 70 'Music/populares/musica.mp3' random"
    exit 1
fi

# Captura os parâmetros
VOLUME="$1"
MUSICA="$2"
RANDOM_MODE="${3:-}"  # Se não fornecido, será vazio
LOG_FILE=/opt/musica/log/vlc.log

# Verifica se o volume é um número entre 0 e 100
if ! [[ "$VOLUME" =~ ^[0-9]+$ ]] || [ "$VOLUME" -lt 0 ] || [ "$VOLUME" -gt 100 ]; then
    echo "Erro: Volume deve ser um número entre 0 e 100"
    exit 1
fi

# Verifica se o arquivo ou diretório da música existe
if [ ! -e "$MUSICA" ]; then
    echo "Erro: Arquivo ou diretório [$MUSICA] não encontrado"
    exit 1
fi

# Altera o volume
# Para saber qual é o card, use o comando `aplay -l`
# Para saber o numid do volume, use o comando
#   `amixer --card 0 controls` (altere o card, se necessário)
#   e busque por numid=1,iface=MIXER,name='PCM Playback Volume'
echo "Volume = ${VOLUME}%"
amixer --card 0 cset numid=1 ${VOLUME}%

# Define os parâmetros do VLC
VLC_PARAMS="--no-dbus -I http --http-password=abc123 --http-port=19034 --file-logging --logfile=\"$LOG_FILE\" --play-and-exit --no-video-title-show"

# Adiciona o parâmetro random se necessário
if [ "$RANDOM_MODE" = "random" ]; then
    echo "Modo aleatório ativado"
    VLC_PARAMS="$VLC_PARAMS --random"
fi

# Toca a música
echo "Música = ${MUSICA}"
# ativa o controle de jobs
set -m

# vlc executado em um job
echo "$(date "+%Y-%m-%d %H:%M:%S") - Iniciando VLC" >> "$LOG_FILE"
eval "vlc $VLC_PARAMS \"$MUSICA\" &"
vlc_process_id=$!

# aguarda o job terminar
wait -f ${vlc_process_id}