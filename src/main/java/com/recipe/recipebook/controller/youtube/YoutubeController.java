package com.recipe.recipebook.controller.youtube;

import com.recipe.recipebook.dto.PlaylistDTO;
import com.recipe.recipebook.service.PlaylistService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class YoutubeController {

    private final PlaylistService playlistService;

    @Operation
    @GetMapping("/playlist")
    public List<PlaylistDTO> getPlaylistItems() throws IOException {
        return playlistService.getPlaylist();
    }
}
