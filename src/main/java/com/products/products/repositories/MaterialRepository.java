package com.products.products.repositories;

import com.products.products.models.Material;
import org.springframework.data.repository.CrudRepository;

public interface MaterialRepository extends CrudRepository<Material, String> {
}
