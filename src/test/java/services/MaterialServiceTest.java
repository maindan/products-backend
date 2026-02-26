package services;

import com.products.products.DTOs.*;
import com.products.products.models.Material;
import com.products.products.repositories.MaterialRepository;
import com.products.products.services.MaterialService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MaterialServiceTest {

    @Mock
    private MaterialRepository materialRepository;

    @InjectMocks
    private MaterialService materialService;

    private Material material;

    @BeforeEach
    void setUp() {
        material = new Material();
        material.setId("1");
        material.setCode("MAT-001");
        material.setName("Material Teste");
        material.setStockQuantity(BigDecimal.valueOf(10));
    }

    @Test
    void shouldCreateMaterial() {
        MaterialCreateRequestDTO request =
                new MaterialCreateRequestDTO("MAT-001", "Material Teste", BigDecimal.valueOf(10));

        when(materialRepository.save(any(Material.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MaterialResponseDTO response = materialService.create(request);

        assertEquals("MAT-001", response.getCode());
        assertEquals("Material Teste", response.getName());
        assertEquals(BigDecimal.valueOf(10), response.getStockQuantity());

        verify(materialRepository, times(1)).save(any(Material.class));
    }

    @Test
    void shouldReturnMaterialById() {
        when(materialRepository.findById("1"))
                .thenReturn(Optional.of(material));

        MaterialResponseDTO response = materialService.getById("1");

        assertEquals("1", response.getId());
        assertEquals("MAT-001", response.getCode());
    }

    @Test
    void shouldThrowExceptionWhenMaterialNotFoundById() {
        when(materialRepository.findById("1"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> materialService.getById("1")
        );

        assertEquals("Material não encontrado", exception.getMessage());
    }

    @Test
    void shouldReturnAllMaterials() {
        when(materialRepository.findAll())
                .thenReturn(List.of(material));

        List<MaterialResponseDTO> response = materialService.getAll();

        assertEquals(1, response.size());
        assertEquals("MAT-001", response.get(0).getCode());
    }

    @Test
    void shouldUpdateMaterial() {

        MaterialUpdateRequestDTO request =
                new MaterialUpdateRequestDTO("MAT-002", "Material Atualizado", BigDecimal.valueOf(20));

        when(materialRepository.findById("1"))
                .thenReturn(Optional.of(material));

        when(materialRepository.save(any(Material.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MaterialResponseDTO response = materialService.update("1", request);

        assertEquals("MAT-002", response.getCode());
        assertEquals("Material Atualizado", response.getName());
        assertEquals(BigDecimal.valueOf(20), response.getStockQuantity());

        assertEquals(BigDecimal.valueOf(20), material.getStockQuantity());

        verify(materialRepository).save(material);
    }

    @Test
    void shouldDeleteMaterial() {
        when(materialRepository.existsById("1"))
                .thenReturn(true);

        materialService.delete("1");

        verify(materialRepository, times(1)).deleteById("1");
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingMaterial() {
        when(materialRepository.existsById("1"))
                .thenReturn(false);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> materialService.delete("1")
        );

        assertEquals("Material não encontrado", exception.getMessage());
    }
}