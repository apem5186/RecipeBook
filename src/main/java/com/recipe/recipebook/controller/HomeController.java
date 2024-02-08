package com.recipe.recipebook.controller;

import com.recipe.recipebook.dto.PlaylistDTO;
import com.recipe.recipebook.service.PlaylistService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
    
    private final PlaylistService playlistService;
    
    @Operation(summary = "메인페이지", description = "재생목록 띄워주는 곳")
    @GetMapping("/")
    public String home(Model model) throws IOException {
        List<PlaylistDTO> playlistDTOS = playlistService.getPlaylist();
        model.addAttribute("items", playlistDTOS);
        return "home";
    }

    @Operation(summary = "개별페이지", description = "각 동영상 상세페이지")
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable(name = "id") String id, Model model) {
        PlaylistDTO playlistDTO = playlistService.getVideo(id);

        model.addAttribute("item", playlistDTO);
        return "detail";
    }
}
