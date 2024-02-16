package com.recipe.recipebook.controller;

import com.recipe.recipebook.dto.EditVideoDTO;
import com.recipe.recipebook.dto.PlaylistDTO;
import com.recipe.recipebook.service.PlaylistService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
    
    private final PlaylistService playlistService;
    
    // TODO : 페이지 불러오는 거 캐싱 활용 고려해보기
    @Operation(summary = "메인페이지", description = "재생목록 띄워주는 곳")
    @GetMapping("/")
    public String home(Model model, @RequestParam(value = "page", required = false) Integer page,
                       WebRequest webRequest) {
        if (page == null) page = 0;
        List<PlaylistDTO> totalPlaylistDTOS = new ArrayList<>();
        String userAgent = webRequest.getHeader("User-Agent");
        for (int i = 0; i <= page; i++) {
            List<PlaylistDTO> playlistDTOS = playlistService.getPlaylist(i, playlistService.determinePageSize(userAgent));
            totalPlaylistDTOS.addAll(playlistDTOS);
        }
        model.addAttribute("items", totalPlaylistDTOS);
        log.info("================================================");
        log.info("page : " + page);
        log.info("================================================");
        return "home";
    }

    @Operation(summary = "개별페이지", description = "각 동영상 상세페이지")
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable(name = "id") String id, Model model) {
        PlaylistDTO playlistDTO = playlistService.getVideo(id);

        model.addAttribute("item", playlistDTO);
        return "detail";
    }

    @Operation(summary = "수정페이지", description = "각 동영상 수정페이지")
    @GetMapping("/detail/{id}/edit")
    public String edit(@PathVariable(name = "id") String id, Model model) {
        PlaylistDTO playlistDTO = playlistService.getVideo(id);
        EditVideoDTO editVideoDTO = new EditVideoDTO();
        editVideoDTO.setTitle(playlistDTO.getTitle());
        editVideoDTO.setDescription(playlistDTO.getDescription());

        model.addAttribute("item", playlistDTO);
        model.addAttribute("videoDTO", editVideoDTO);
        return "edit";
    }
}
