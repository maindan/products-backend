package services;

import com.products.products.DTOs.*;
import com.products.products.models.Material;
import com.products.products.models.Product;
import com.products.products.models.ProductMaterial;
import com.products.products.repositories.MaterialRepository;
import com.products.products.repositories.ProductRepository;
import com.products.products.services.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MaterialRepository materialRepository;

    private Product createProductWithMaterials() {
        Product product = new Product();
        product.setId("1");
        product.setName("Produto A");
        product.setCode("P001");
        product.setPrice(BigDecimal.TEN);

        Material material = new Material();
        material.setId("10");
        material.setName("Material X");
        material.setStockQuantity(BigDecimal.valueOf(100));

        ProductMaterial pm = new ProductMaterial();
        pm.setProduct(product);
        pm.setMaterial(material);
        pm.setQuantityRequired(BigDecimal.ONE);

        product.setMaterials(new ArrayList<>(List.of(pm)));

        return product;
    }

    @Test
    void shouldReturnAllProducts() {
        Product product = createProductWithMaterials();

        when(productRepository.findAll()).thenReturn(List.of(product));

        List<ProductResponseDTO> response = productService.getAllProducts();

        assertEquals(1, response.size());
    }

    @Test
    void shouldReturnProductById() {
        Product product = createProductWithMaterials();

        when(productRepository.findById("1")).thenReturn(Optional.of(product));

        ProductResponseDTO response = productService.getById("1");

        assertNotNull(response);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        when(productRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.getById("1"));
    }

    @Test
    void shouldReturnAllProductsWithMaterials() {
        Product product = createProductWithMaterials();

        when(productRepository.findAll()).thenReturn(List.of(product));

        List<ProductWithMaterialsResponseDTO> response =
                productService.getAllProductsWithMaterials();

        assertFalse(response.isEmpty());
    }

    @Test
    void shouldCreateProduct() {
        Product product = createProductWithMaterials();

        ProductCreateRequestDTO request = new ProductCreateRequestDTO();
        ProductMaterialRequestDTO materialRequest = new ProductMaterialRequestDTO();
        materialRequest.setMaterialId("10");
        materialRequest.setQuantityRequired(BigDecimal.ONE);

        request.setCode("P001");
        request.setName("Produto A");
        request.setPrice(BigDecimal.TEN);
        request.setMaterials(List.of(materialRequest));

        when(materialRepository.findById("10"))
                .thenReturn(Optional.of(product.getMaterials().get(0).getMaterial()));

        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponseDTO response = productService.create(request);

        assertNotNull(response);
    }

    @Test
    void shouldUpdateProduct() {
        Product product = createProductWithMaterials();

        ProductCreateRequestDTO request = new ProductCreateRequestDTO();
        request.setCode("NEW");
        request.setName("Novo");
        request.setPrice(BigDecimal.ONE);
        request.setMaterials(new ArrayList<>());

        when(productRepository.findById("1")).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponseDTO response = productService.update("1", request);

        assertNotNull(response);
    }

    @Test
    void shouldDeleteProduct() {

        String productId = "1";

        when(productRepository.existsById(productId))
                .thenReturn(true);

        doNothing().when(productRepository)
                .deleteById(productId);

        productService.delete(productId);

        verify(productRepository, times(1))
                .deleteById(productId);
    }

    @Test
    void shouldReturnProductionSuggestion() {
        Product product = createProductWithMaterials();

        when(productRepository.findAll()).thenReturn(List.of(product));

        ProductSuggestionResponseDTO response =
                productService.suggestionProduction();

        assertNotNull(response);
    }
}