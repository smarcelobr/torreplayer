function configUpdated(config) {
    if (!config) {
        config = {masterVolume:0};
    }
    if (!config.masterVolume) {
        config.masterVolume =0;
    }

    const masterVolumeInput = document.getElementById("masterVolumeInput");
    masterVolumeInput.value = config.masterVolume;
    masterVolumeInput.disabled = false;
    const volumeLabel = document.getElementById("volume");
    volumeLabel.innerText = ""+config.masterVolume;
}

function xhrConfigOnLoad() {
    let config = null;
    if (this.responseText) {
        try {
            config = JSON.parse(this.responseText);
        } catch (e) {
            // nada a fazer, mantem valores default;
        }
    }
    configUpdated(config);
}

function verificaConfiguracoes() {
    const masterVolumeInput = document.getElementById("masterVolumeInput");
    masterVolumeInput.disabled = true;

    const xhr = new XMLHttpRequest();
    xhr.open("GET", URL.apiConfig, true);
    xhr.setRequestHeader("Accept", "application/json");

    xhr.onload = xhrConfigOnLoad;

    xhr.onreadystatechange=function(){
        if (xhr.readyState===4){ // oncomplete
            setTimeout(function () {
                verificaConfiguracoes()
            }, 24000);
        }
    }

    xhr.send();
}

function masterVolumeInputChange() {
    const masterVolumeInput = document.getElementById("masterVolumeInput");
    masterVolumeInput.disabled = true;

    const xhr = new XMLHttpRequest();
    xhr.open("POST", URL.apiConfig, true);
    xhr.setRequestHeader("Accept", "application/json");
    xhr.setRequestHeader("Content-Type", "application/json");

    xhr.onload = xhrConfigOnLoad;

    let novoConfig = {masterVolume: parseInt(masterVolumeInput.value)};

    xhr.send(JSON.stringify(novoConfig));
}

// quando a pagina estiver carregada:
document.addEventListener("DOMContentLoaded", function(e) {
    verificaConfiguracoes();
});