package com.effigo.learningportalv2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.effigo.learningportalv2.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
