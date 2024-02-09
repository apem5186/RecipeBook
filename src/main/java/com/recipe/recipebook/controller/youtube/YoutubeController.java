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

//    @Operation(summary = "동영상 삭제 요청")
//    @DeleteMapping("/delete/video/{id}")
//    public String deleteVideo(@PathVariable("id") String id) {
//        playlistService.deleteVideo(id);
//        return "home";
//    }
    
    @Operation(summary = "동영상 삭제 요청")
    @DeleteMapping("/delete/video/{videoId}")
    @ResponseBody
    public ResponseEntity<?> deleteVideo(@PathVariable String videoId) {
        try {
            playlistService.deleteVideo(videoId);
            // Your delete logic here
            return ResponseEntity.ok().body("{\"message\": \"Video deleted successfully\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
