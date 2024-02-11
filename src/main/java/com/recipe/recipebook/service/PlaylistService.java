package com.recipe.recipebook.service;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.Thumbnail;
import com.recipe.recipebook.dto.EditVideoDTO;
import com.recipe.recipebook.dto.PlaylistDTO;
import com.recipe.recipebook.entity.Playlist;
import com.recipe.recipebook.repository.PlaylistRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
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

    private final EntityManager entityManager;

    public PlaylistService(PlaylistRepository playlistRepository, EntityManager entityManager) {
        this.playlistRepository = playlistRepository;
        this.entityManager = entityManager;
        this.youTube = new YouTube.Builder(
                new com.google.api.client.http.javanet.NetHttpTransport(),
                new com.google.api.client.json.jackson2.JacksonFactory(),
                httpRequest -> {}
        ).setApplicationName(APPLICATION_NAME).build();
    }

    /**
     * Youtube 재생목록 가져옴
     * @return List<Playlist>
     * @throws IOException
     */
    @Transactional
    public List<Playlist> getList() throws IOException {
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

        return playlists;
    }

    /**
     * Youtube 재생목록을 DB에 저장
     * @throws IOException
     */
    @Transactional
    public void youtubePlaylist() throws IOException {
        List<Playlist> playlists = getList();
        playlistRepository.saveAll(playlists);

    }

    /**
     * Youtube 재생목록을 DTO로 변환 후 반환
     * @return List<PlaylistDTO>
     * @throws IOException
     */
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

    /**
     * 매개변수로 videoId를 받아서 해당하는 playlist 객체를 DTO로 변환 후 반환
     * @param videoId
     * @return PlaylistDTO
     */
    public PlaylistDTO getVideo(String videoId) {
        Playlist playlist = playlistRepository.findByVideoId(videoId);
        return new PlaylistDTO(playlist);
    }

    /**
     * editVideoDTO를 매개변수로 받아서 Playlist 객체 수정
     * @param editVideoDTO
     */
    @Transactional
    public void editVideo(EditVideoDTO editVideoDTO) {
        Playlist playlist = playlistRepository.findByVideoId(editVideoDTO.getVideoId());

        log.info("===================================");
        log.info(playlist.getVideoId());
        log.info(editVideoDTO.getTitle());
        log.info(editVideoDTO.getVideoId());
        log.info("===================================");

        playlist.setTitle(editVideoDTO.getTitle());
        playlist.setDescription(editVideoDTO.getDescription());

        playlistRepository.save(playlist);
    }

    /**
     * videoId를 매개변수로 받아서 해당하는 Playlist 객체 삭제
     * @param videoId
     */
    @Transactional
    public void deleteVideo(String videoId) {
        log.info("delete video id : " + videoId);
        playlistRepository.deleteByVideoId(videoId);
    }

    /**
     * 재생목록 초기화, 테이블 데이터 삭제 후 다시 가져옴
     * @throws IOException
     */
    @Transactional
    public void initialization() throws IOException {
        if (playlistRepository.count() > 0) {
            log.info("실행됨");
            playlistRepository.deleteAll();
            entityManager.flush();
            entityManager.clear();
        }
        youtubePlaylist();
    }

    /**
     * Playlist 테이블에 없는 재생 목록을 가져옴
     * @throws IOException
     */
    @Transactional
    public void refresh() throws IOException {
        List<Playlist> playlists = getList();
        for (Playlist list : playlists) {
            if (!playlistRepository.existsPlaylistByVideoId(list.getVideoId())) {
                log.info(list.getVideoId());
                playlistRepository.save(list);
            }
        }
    }

}
