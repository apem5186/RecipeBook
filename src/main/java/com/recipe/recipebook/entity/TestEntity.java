package com.recipe.recipebook.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "test_entity")
@NoArgsConstructor
@Getter
@Setter
public class TestEntity {

    @Id
    private long id;

    @Column
    private String str;

    @Column
    private long number;
}
