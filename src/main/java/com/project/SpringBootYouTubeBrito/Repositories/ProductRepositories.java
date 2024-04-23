package com.project.SpringBootYouTubeBrito.Repositories;

import com.project.SpringBootYouTubeBrito.models.ProductModels;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepositories extends JpaRepository<ProductModels, UUID> {

}
