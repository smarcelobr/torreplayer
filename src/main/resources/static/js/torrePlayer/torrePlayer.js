function infoUpdated(info) {
    if (!info) {
        info = {status:"INDEFINIDO"};
    }
    if (!info.musica) {
        info.musica = {nome: ""};
    }

    const mostrarQdoNaoTocando = document.getElementsByClassName("torreEmSilencio");
    const mostrarQdoTocando = document.getElementsByClassName("torreTocando");
    const torreMusicaNome = document.getElementsByClassName("torreMusicaNome");
    const torreOutput = document.getElementsByClassName("torreOutput");
    Array.from(torreMusicaNome)
        .forEach((element) => element.innerText = info.musica.nome || "");
    Array.from(mostrarQdoTocando)
        .forEach((element) => element.style.display = info.status==="TOCANDO"?"block":"none");
    Array.from(mostrarQdoNaoTocando)
        .forEach((element) => element.style.display = info.status==="TOCANDO"?"none":"block");
    Array.from(torreOutput)
        .forEach((element) => element.innerText = info.output || "");
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