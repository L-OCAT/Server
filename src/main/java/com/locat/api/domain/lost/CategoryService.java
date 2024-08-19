package com.locat.api.domain.lost;

import com.locat.api.domain.lost.entity.Category;

import java.util.List;

public interface CategoryService {

  List<Category> findAll();
}
