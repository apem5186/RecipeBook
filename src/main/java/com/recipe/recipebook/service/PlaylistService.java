package com.recipe.recipebook.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.Thumbnail;
import com.recipe.recipebook.dto.EditVideoDTO;
import com.recipe.recipebook.dto.OpenAiRequestDTO;
import com.recipe.recipebook.dto.PlaylistDTO;
import com.recipe.recipebook.entity.Playlist;
import com.recipe.recipebook.repository.PlaylistRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PlaylistService {

    @Value("${youtube.api.key}")
    private String API_KEY;

    @Value("${youtube.api.playlist-id}")
    private String PLAYLIST_ID;

    @Value("${youtube.api.application-name}")
    private String APPLICATION_NAME;

    @Value("${openai.api.key}")
    private String CHATGPT_API_KEY;

    @Value("${openai.api.model}")
    private String CHATGPT_API_MODEL;

    private final YouTube youTube;

    private final PlaylistRepository playlistRepository;

    private final EntityManager entityManager;

    private final RestTemplate restTemplate = new RestTemplate();

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
     */
    public List<PlaylistDTO> getPlaylist(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        List<Playlist> playlists = playlistRepository.findAll(pageable).getContent();
        if (playlists.isEmpty()) {
            log.info("==============================");
            log.info("Playlist is Empty, fetching from YouTube...");
            log.info("==============================");
            try {
                youtubePlaylist();
                playlists = playlistRepository.findAll(pageable).getContent();
            } catch (IOException e) {
                log.error("Failed to fetch playlists from YouTube", e);
            }
        }

        return playlists.stream()
                .map(PlaylistDTO::new)
                .collect(Collectors.toList());
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
    public void refresh() {
        List<Playlist> playlists = new ArrayList<>();
        try {
            playlists = getList();
        } catch (IOException e) {
            log.error("getList Method Error while refreshing...\n" + e);
        }
        for (Playlist list : playlists) {
            if (!playlistRepository.existsPlaylistByVideoId(list.getVideoId())) {
                log.info(list.getVideoId());
                playlistRepository.save(list);
            }
        }
    }

    @Transactional
    public String summarizeRecipe(String videoId) {
        Playlist playlist = playlistRepository.findByVideoId(videoId);
        String description = playlist.getDescription();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(CHATGPT_API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        OpenAiRequestDTO openAIRequest = OpenAiRequestDTO.builder()
                .model(CHATGPT_API_MODEL)
                .messages(Arrays.asList(
                        OpenAiRequestDTO.Message.builder()
                                .role("system")
                                .content("Please extract and list only the ingredients and recipes in Korean in this cooking description.")
                                .build(),
                        OpenAiRequestDTO.Message.builder()
                                .role("user")
                                .content(description)
                                .build()
                ))
                .temperature(0.5)
                .max_tokens(2000)
                .top_p(1.0)
                .frequency_penalty(0.0)
                .presence_penalty(0.0)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String body = null;
        try {
            body = objectMapper.writeValueAsString(openAIRequest);
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException : " + e);
        }

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("https://api.openai.com/v1/chat/completions", entity, String.class);

        log.info("Extracted Text : \n " + response.getBody());
        try {
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            String newDescription = rootNode.path("choices").get(0).path("message").path("content").asText();
            if (Objects.equals(rootNode.path("choices").get(0).path("finish_reason").asText(), "length"))
                newDescription = newDescription + "\n\n max_tokens 초과로 텍스트 짤림";
            playlist.setDescription(newDescription);

            playlistRepository.save(playlist);
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException : " + e);
        }

        return response.getBody();
    }

    public List<Map<String, Object>> modelList() {
        log.debug("[+] 모델 리스트를 조회합니다.");
        List<Map<String, Object>> resultList = null;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(CHATGPT_API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // ResponseEntity<String>으로 변경하여 응답 타입을 명확히 합니다.
        ResponseEntity<String> response = restTemplate.exchange(
                "https://api.openai.com/v1/models",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );

        // 응답 본문이 null인 경우를 처리합니다.
        if (response.getBody() == null) {
            log.debug("응답 본문이 비어 있습니다.");
            return Collections.emptyList(); // 또는 적절한 예외 처리
        }

        try {
            ObjectMapper om = new ObjectMapper();
            Map<String, Object> data = om.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});

            // resultList에 대한 안전한 타입 캐스팅을 수행합니다.
            resultList = (List<Map<String, Object>>) data.get("data");
            if (resultList != null) {
                for (Map<String, Object> object : resultList) {
                    log.debug("ID: " + object.get("id"));
                    log.debug("Object: " + object.get("object"));
                    log.debug("Created: " + object.get("created"));
                    log.debug("Owned By: " + object.get("owned_by"));
                }
            } else {
                log.debug("데이터 리스트가 비어 있습니다.");
                return Collections.emptyList();
            }
        } catch (JsonMappingException e) {
            log.error("JsonMappingException :: " + e.getMessage());
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException :: " + e.getMessage());
        }

        return resultList;
    }

    public Map<String, Object> isValidModel(String modelName) {
        log.debug("[+] 모델이 유효한지 조회합니다. 모델 : " + modelName);
        Map<String, Object> result;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(CHATGPT_API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        ResponseEntity<String> response = restTemplate
                .exchange(
                        "https://api.openai.com/v1/models/" + modelName,
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        String.class);

        try {
            ObjectMapper om = new ObjectMapper();
            result = om.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * Mobile은 10 PC는 30, pageSize를 결정함
     * @param userAgent Mobile로 접속한 건지 PC로 접속한 건지
     * @return pageSize
     */
    public int determinePageSize(String userAgent) {
        if (userAgent != null && userAgent.contains("Mobi")) {
            return 10;
        } else {
            return 5;
        }
    }
}
