package com.recipe.recipebook.controller;

import com.recipe.recipebook.entity.ErrorResponse;
import com.recipe.recipebook.exception.*;
import org.springframework.http.HttpStatus;
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
        modelAndView.addObject("errorMessage", ex.getMessage());
        return modelAndView;
    }

//    @ExceptionHandler(YouTubePlaylistEmptyException.class)
//    public String handleYoutubePlaylistIsEmpty(YouTubePlaylistEmptyException ex, Model model) {
//        model.addAttribute("errorMessage", ex.getMessage());
//        return "redirect:/";
//    }

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
