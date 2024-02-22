package com.recipe.recipebook.controller;

import com.recipe.recipebook.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PlaylistNotFoundException.class)
    public String handlePlaylistNotFoundException(PlaylistNotFoundException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/";
    }

    @ExceptionHandler(YouTubePlaylistEmptyException.class)
    public String handleYoutubePlaylistIsEmpty(YouTubePlaylistEmptyException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/";
    }

    @ExceptionHandler(NetworkErrorException.class)
    public String handleNetworkError(NetworkErrorException ex, RedirectAttributes redirectAttributes,
                                     HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
        redirectAttributes.addFlashAttribute("ErrorStatus", ex.getStatus());

        String referrer = request.getHeader("Referer");

        // URL에 직접 액세스 했을 때 "/", 아니면 그 페이지에 남음
        return "redirect:" + (referrer != null ? referrer : "/");
    }

    @ExceptionHandler(YouTubeAPIKeyIncorrectException.class)
    public String handleYoutubeAPIKeyWrong(YouTubeAPIKeyIncorrectException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        // 메인 페이지로 또 가면 무한 루프 빠질 수 있으므로 에러 페이지 생성 후 보내야 함
        // TODO : error 페이지 만들기
        return "";
    }

    @ExceptionHandler(YouTubePlaylistIDWrongException.class)
    public String handleYoutubePlaylistIdWrong(YouTubePlaylistIDWrongException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        // TODO : 에러 페이지 만들기
        return "";
    }

    @ExceptionHandler(YouTubeAPIRequestException.class)
    public String handleYoutubeAPIRequestException(YouTubeAPIRequestException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        // TODO : 에러 페이지 만들기
        return "";
    }
}
