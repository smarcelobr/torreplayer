package br.nom.figueiredo.sergio.torreplayer.service;

import br.nom.figueiredo.sergio.torreplayer.exception.MusicaException;
import br.nom.figueiredo.sergio.torreplayer.model.Configuracoes;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ConfigService {
    private final Logger logger = LoggerFactory.getLogger(ConfigService.class);

    private final String albumFolder;

    private final ObjectMapper om;

    private Configuracoes configuracoes;

    @Autowired
    public ConfigService(ObjectMapper objectMapper, @Value(value = "${album.path}") String albumFolder) {
        this.om = objectMapper;
        this.albumFolder = albumFolder;
        readConfigFile();
    }

    private void readConfigFile() {
        this.configuracoes = new Configuracoes();
        Path configFile = getConfigFilePath();
        if (Files.exists(configFile)) {
            // carrega as configuracoes do arquivo
            try {
                this.configuracoes = om.readValue(configFile.toFile(), Configuracoes.class);
            } catch (Exception ex) {
                // usa configuracoes padrao
                logger.error("Falha ao ler configuracoes", ex);
            }
        } else {
            // salva a primeira versão do arquivo de configurações com valores padrão.
            saveConfigFile();
        }
    }

    private Path getConfigFilePath() {
        return Paths.get(albumFolder, "torrePlayer.json");
    }

    public void setVolumePadrao(Integer volume) {
        this.configuracoes.setMasterVolume(volume);
    }

    public void saveConfigFile() {
        try {
            this.om.writeValue(getConfigFilePath().toFile(), this.configuracoes);
        } catch (IOException e) {
            throw new MusicaException("Falha ao salvar configuracoes", e);
        }
    }

    public Configuracoes getConfiguracoes() {
        return this.configuracoes;
    }

    public void setCmdLabel(String cmdLabel) {
        // altera o label do botão de toque externo
        this.configuracoes.setCmdLabel(cmdLabel);
    }
}
