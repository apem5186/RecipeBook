package com.recipe.recipebook.controller;

import com.recipe.recipebook.entity.ErrorResponse;
import com.recipe.recipebook.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PlaylistNotFoundException.class)
    public ModelAndView handlePlaylistNotFoundException(PlaylistNotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView("errorPage");
        modelAndView.addObject("ErrorStatus", HttpStatus.NOT_FOUND);
        modelAndView.addObject("errorMessage", ex.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(OutOfPageRangeException.class)
    public Object handleOutOfPageRangeException(OutOfPageRangeException ex, HttpServletRequest request) {
        String requestedWithHeader = request.getHeader("X-Requested-With");
        boolean isAjax = "XMLHttpRequest".equals(requestedWithHeader);

        if (isAjax) {
            // AJAX 요청인 경우, JSON 응답 반환
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } else {
            // 일반 요청인 경우, ModelAndView를 사용하여 에러 페이지 렌더링
            ModelAndView modelAndView = new ModelAndView("errorPage");
            modelAndView.addObject("ErrorStatus", HttpStatus.BAD_REQUEST);
            modelAndView.addObject("errorMessage", ex.getMessage());
            return modelAndView;
        }
    }

    @ExceptionHandler(NetworkErrorException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleNetworkError(NetworkErrorException ex) {

        return new ErrorResponse("An unexpected error occurred. Please try again later.", ex.getStatus()).toResponseEntity();
    }

    @ExceptionHandler(YouTubeAPIKeyIncorrectException.class)
    public ModelAndView handleYoutubeAPIKeyWrong(YouTubeAPIKeyIncorrectException ex) {
        ModelAndView modelAndView = new ModelAndView("errorPage");
        modelAndView.addObject("errorMessage", ex.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(YouTubePlaylistIDWrongException.class)
    public ModelAndView handleYoutubePlaylistIdWrong(YouTubePlaylistIDWrongException ex) {
        ModelAndView modelAndView = new ModelAndView("errorPage");
        modelAndView.addObject("errorMessage", ex.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(YouTubeAPIRequestException.class)
    public ModelAndView handleYoutubeAPIRequestException(YouTubeAPIRequestException ex) {
        ModelAndView modelAndView = new ModelAndView("errorPage");
        modelAndView.addObject("errorMessage", ex.getMessage());
        return modelAndView;
    }
}
