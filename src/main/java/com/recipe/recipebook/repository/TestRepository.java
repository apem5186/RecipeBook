package com.recipe.recipebook.repository;

import com.recipe.recipebook.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<TestEntity, Long> {

    TestEntity findByStr(String str);
}
