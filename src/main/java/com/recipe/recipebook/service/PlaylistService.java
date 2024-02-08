package com.recipe.recipebook.service;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.Thumbnail;
import com.recipe.recipebook.dto.PlaylistDTO;
import com.recipe.recipebook.entity.Playlist;
import com.recipe.recipebook.repository.PlaylistRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PlaylistService {

    @Value("${youtube.api.key}")
    private String API_KEY;

    @Value("${youtube.api.playlist-id}")
    private String PLAYLIST_ID;

    @Value("${youtube.api.application-name}")
    private String APPLICATION_NAME;

    private final YouTube youTube;

    private final PlaylistRepository playlistRepository;

    public PlaylistService(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
        this.youTube = new YouTube.Builder(
                new com.google.api.client.http.javanet.NetHttpTransport(),
                new com.google.api.client.json.jackson2.JacksonFactory(),
                httpRequest -> {}
        ).setApplicationName(APPLICATION_NAME).build();
    }

    public void youtubePlaylist() throws IOException {
        YouTube.PlaylistItems.List request = youTube.playlistItems().list("snippet");
        request.setKey(API_KEY);
        request.setPlaylistId(PLAYLIST_ID);
        request.setMaxResults(50L);

        List<Playlist> playlists = new ArrayList<>();
        String nextToken = "";

        do {
            request.setPageToken(nextToken);
            PlaylistItemListResponse response = request.execute();
            if (response.isEmpty()) {
                log.error("=================================");
                log.error("list is empty");
                log.error("=================================");
            }
            for (PlaylistItem item : response.getItems()) {
                String videoId = item.getSnippet().getResourceId().getVideoId();
                String title = item.getSnippet().getTitle();
                String description = item.getSnippet().getDescription();
                Thumbnail highQualityThumbnail = item.getSnippet().getThumbnails().getDefault();
                String thumbnailUrl = highQualityThumbnail != null ? highQualityThumbnail.getUrl() : null;

                Playlist playlist = new Playlist(videoId, title, description, thumbnailUrl);
                playlists.add(playlist);
            }

            nextToken = response.getNextPageToken();
        } while (nextToken != null);

        playlistRepository.saveAll(playlists);

    }

    public List<PlaylistDTO> getPlaylist() throws IOException {
        List<Playlist> playlists = playlistRepository.findAll();
        if (playlists == null || playlists.isEmpty()) {
            log.info("==============================");
            log.info("Playlist is Empty");
            log.info("==============================");
            youtubePlaylist();
            playlists = playlistRepository.findAll();
        }

        return playlists.stream()
                .map(PlaylistDTO::new)
                .toList();
    }

    public PlaylistDTO getVideo(String videoId) {
        Playlist playlist = playlistRepository.findByVideoId(videoId);
        return new PlaylistDTO(playlist);
    }
}
