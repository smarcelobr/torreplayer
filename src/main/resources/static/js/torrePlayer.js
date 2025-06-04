function infoUpdated(info) {
    if (!info) {
        info = {status:"INDEFINIDO"};
    }
    if (!info.arquivo) {
        info.arquivo = {nome: ""};
    }

    const torreMusicaNome = document.getElementsByClassName("torreMusicaNome");
    Array.from(torreMusicaNome)
        .forEach((element) => element.innerText = info.arquivo.nome || "");

    const mostrarQdoTocando = document.getElementsByClassName("torreTocando");
    Array.from(mostrarQdoTocando)
        .forEach((element) => element.style.display = info.status==="TOCANDO"?"block":"none");

    const mostrarQdoNaoTocando = document.getElementsByClassName("torreEmSilencio");
    Array.from(mostrarQdoNaoTocando)
        .forEach((element) => element.style.display = info.status==="TOCANDO"?"none":"block");

    const torreOutput = document.getElementById("torreOutput");
    if (torreOutput) {
        torreOutput.innerText = info.output || "";
    }

    const torrePlayButton = document.getElementById("torrePlayButton");
    if (torrePlayButton) {
        torrePlayButton.style.display = (info.status!=="TOCANDO" && info.arquivo.nome)?
            "block":"none";
        if (info.arquivo) {
            if (info.arquivo.tipo === 'MUSICA') {
                torrePlayButton.onclick = function (ev) {
                    torrePlay(info.arquivo.album.nome, info.arquivo.nome);
                }
            } else if (info.arquivo.tipo === 'ALBUM') {
                torrePlayButton.onclick = function (ev) {
                    torrePlayAlbum(info.arquivo.nome);
                }
            }
        }
    }
}

function xhrInfoOnLoad() {
    let info = null;
    if (this.responseText) {
        try {
            info = JSON.parse(this.responseText);
        } catch (e) {
            // nada a fazer, mantem valores default;
        }
    }
    infoUpdated(info);
}

function verificaMusicaTocando() {
    const xhr = new XMLHttpRequest();
    xhr.open("GET", URL.apiTorreInfo, true);
    xhr.setRequestHeader("Accept", "application/json");

    xhr.onload = xhrInfoOnLoad;

    xhr.onreadystatechange=function(){
        if (xhr.readyState===4){ // oncomplete
            setTimeout(function () {
                verificaMusicaTocando()
            }, 5000);
        }
    }

    xhr.send();
}

function torrePlay(albumNome, musicaNome) {
    const xhr = new XMLHttpRequest();
    xhr.open("POST", URL.apiTorrePlay, true);
    xhr.setRequestHeader("Accept", "application/json");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onload = xhrInfoOnLoad;

    const req = {albumNome: albumNome, musicaNome: musicaNome};

    xhr.send(JSON.stringify(req));
}

function torrePlayAlbum(albumNome) {
    const xhr = new XMLHttpRequest();
    xhr.open("POST", URL.apiTorrePlay, true);
    xhr.setRequestHeader("Accept", "application/json");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onload = xhrInfoOnLoad;

    const req = {albumNome: albumNome};

    xhr.send(JSON.stringify(req));
}

function torreStop() {
    const xhr = new XMLHttpRequest();
    xhr.open("POST", URL.apiTorreStop, true);
    xhr.setRequestHeader("Accept", "application/json");

    xhr.onload = xhrInfoOnLoad;

    xhr.send();
}

// quando a pagina estiver carregada:
document.addEventListener("DOMContentLoaded", function(e) {
    verificaMusicaTocando();
});