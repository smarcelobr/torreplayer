#!/bin/bash

# garante que quando este script for encerrado forçadamente, o
# cvlc (subprocesso) também seja encerrado.
trap "pkill vlc" SIGINT SIGHUP SIGTERM EXIT

# Verifica se foram passados exatamente 2 parâmetros
if [ $# -ne 2 ]; then
    echo "Erro: Número incorreto de parâmetros"
    echo "Uso: $0 <volume> <caminho-da-musica>"
    echo "Exemplo: $0 70 'Music/populares/musica.mp3'"
    exit 1
fi

# Captura os parâmetros
VOLUME="$1"
MUSICA="$2"

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
echo "Volume = ${VOLUME}%"
amixer --card 1 cset numid=1 ${VOLUME}%

# Toca a música
echo "Música = ${MUSICA}"
# ativa o controle de jobs
set -m
# vlc executado em um job
cvlc --play-and-exit --no-video-title-show "$MUSICA" &
cvlc_process_id=$!

# aguarda o job terminar
wait -f ${cvlc_process_id}