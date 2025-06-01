package br.nom.figueiredo.sergio.torreplayer.controller;

import br.nom.figueiredo.sergio.torreplayer.model.Album;
import br.nom.figueiredo.sergio.torreplayer.service.MusicaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HomeControllerTest {

    @Mock
    private MusicaService musicaService;

    @Mock
    private Model model;

    @InjectMocks
    private HomeController homeController;

    @Test
    void listAlbums() {
        // Arrange
        Album album1 = new Album();
        album1.setNome("Album 1");
        Album album2 = new Album();
        album1.setNome("Album 2");

        List<Album> expectedAlbums = Arrays.asList(
                album1, album2
        );
        when(musicaService.getAllAlbums()).thenReturn(expectedAlbums);

        // Act
        String viewName = homeController.listAlbums(model);

        // Assert
        assertEquals("index.html", viewName);
        verify(model).addAttribute("albums", expectedAlbums);
        verify(musicaService).getAllAlbums();
    }
}