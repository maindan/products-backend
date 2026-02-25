package com.products.products.repositories;

import com.products.products.models.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface MaterialRepository extends JpaRepository<Material, String> {
}
