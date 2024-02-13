package com.recipe.recipebook.controller.youtube;

import com.recipe.recipebook.dto.EditVideoDTO;
import com.recipe.recipebook.dto.PlaylistDTO;
import com.recipe.recipebook.service.PlaylistService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
public class YoutubeController {

    private final PlaylistService playlistService;

    @Operation
    @GetMapping("/playlist")
    public List<PlaylistDTO> getPlaylistItems() throws IOException {
        return playlistService.getPlaylist();
    }

    @Operation(summary = "동영상 수정 요청")
    @PostMapping("/edit/video/{id}")
    public String editVideo(@PathVariable("id") String id, @ModelAttribute("videoDTO") EditVideoDTO editVideoDTO,
                            BindingResult result, Model model) {

        editVideoDTO.setVideoId(id);
        playlistService.editVideo(editVideoDTO);

        return "redirect:/detail/" + id;
    }
    
    @Operation(summary = "동영상 삭제 요청")
    @DeleteMapping("/delete/video/{videoId}")
    @ResponseBody
    public ResponseEntity<?> deleteVideo(@PathVariable String videoId) {
        try {
            playlistService.deleteVideo(videoId);
            return ResponseEntity.ok().body("{\"message\": \"Video deleted successfully\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @Operation(summary = "재생목록 새로고침")
    @PostMapping("/refresh/video")
    @ResponseBody
    public ResponseEntity<?> refreshVideo() throws IOException {
        try {
            playlistService.refresh();
            return ResponseEntity.ok().body("{\"message\": \"Video refresh successful\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @Operation(summary = "재생목록 초기화")
    @PostMapping("/initialize/video")
    @ResponseBody
    public ResponseEntity<?> initVideo() throws IOException {
        try {
            playlistService.initialization();
            return ResponseEntity.ok().body("{\"message\": \"Playlist initialize successful\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @Operation(summary = "description 요약")
    @PostMapping("/summarize/description/{videoId}")
    @ResponseBody
    public ResponseEntity<?> summarizeDescription(@PathVariable String videoId) {
        String summary = playlistService.summarizeRecipe(videoId);

        return ResponseEntity.ok(summary);
    }

    @Operation(summary = "chatGPT의 사용 가능한 모델 조회")
    @GetMapping("/api/v1/chatGpt/modelList")
    public ResponseEntity<List<Map<String, Object>>> selectModelList() {
        List<Map<String, Object>> result = playlistService.modelList();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "chatGPT에서 특정 모델이 사용 가능한지 조회")
    @GetMapping("/api/v1/chatGpt/model")
    public ResponseEntity<Map<String, Object>> isValidModel(@RequestParam(name = "modelName") String modelName) {

        Map<String, Object> result = playlistService.isValidModel(modelName);
        return new ResponseEntity<>(result, HttpStatus.OK);}
}
