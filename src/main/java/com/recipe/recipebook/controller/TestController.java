package com.recipe.recipebook.controller;

import com.recipe.recipebook.entity.TestEntity;
import com.recipe.recipebook.repository.TestRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("/v3")
@Slf4j
public class TestController {

    @Autowired
    TestRepository testRepository;

    @PostConstruct
    public void initData() {
        TestEntity testEntity = new TestEntity();
        testEntity.setStr("Test");
        testEntity.setNumber(1);

        testRepository.save(testEntity);
    }

    @Operation(description = "swagger test")
    @GetMapping("/homeTest")
    public String home(Model model) {

        TestEntity testEntity = testRepository.findByStr("Test");

        model.addAttribute("Test", testEntity.getStr());
        model.addAttribute("Number", testEntity.getNumber());

        log.info("Test Data : " + testEntity.getStr() + " and " + testEntity.getNumber());

        return "home";
    }
}
