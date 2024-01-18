package com.miagem2.cinema_booking.Repository;

import com.miagem2.cinema_booking.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByIdIn(List<Long> categoryIds);

}
