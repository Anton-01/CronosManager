package com.cronos.cronosmanager.controller.common;

import com.cronos.cronosmanager.dto.common.request.ProductTypeRequestDto;
import com.cronos.cronosmanager.dto.common.response.ProductTypeResponseDto;
import com.cronos.cronosmanager.exception.common.ResourceNotFoundException;
import com.cronos.cronosmanager.model.response.ResponseApi;
import com.cronos.cronosmanager.service.common.ProductTypeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@AllArgsConstructor
@RestController
@RequestMapping("/product-type")
public class ProductTypeController {

    private final ProductTypeService productTypeService;

    @GetMapping
    public ResponseEntity<ResponseApi<PagedModel<EntityModel<ProductTypeResponseDto>>>> getAllProductTypes(Pageable pageable, PagedResourcesAssembler<ProductTypeResponseDto> assembler) {
        Page<ProductTypeResponseDto> products = productTypeService.findAll(pageable);
        PagedModel<EntityModel<ProductTypeResponseDto>> pagedModel = assembler.toModel(products, product ->
                EntityModel.of(product,
                        linkTo(methodOn(ProductTypeController.class).getProductById(product.getId())).withSelfRel(),
                        linkTo(methodOn(ProductTypeController.class).getAllProductTypes(null, null)).withRel("all-product-types")
                )
        );

        return ResponseEntity.ok(new ResponseApi<>(pagedModel, HttpStatus.OK));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseApi<EntityModel<ProductTypeResponseDto>>> getProductById(@PathVariable UUID id) {
        ProductTypeResponseDto product = productTypeService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("The Product Type with ID: %s is not found in our records.", id)));

        EntityModel<ProductTypeResponseDto> entityModel = EntityModel.of(product,
                linkTo(methodOn(ProductTypeController.class).getProductById(id)).withSelfRel(),
                linkTo(methodOn(ProductTypeController.class).getAllProductTypes(null, null)).withRel("all-product-types")
        );

        return ResponseEntity.ok(new ResponseApi<>(entityModel, HttpStatus.OK));
    }

    @PostMapping
    public ResponseEntity<ResponseApi<EntityModel<ProductTypeResponseDto>>> saveProductType(@Valid @RequestBody ProductTypeRequestDto request) {
        ProductTypeResponseDto product = productTypeService.save(request);

        EntityModel<ProductTypeResponseDto> entityModel = EntityModel.of(product,
                linkTo(methodOn(ProductTypeController.class).getProductById(product.getId())).withSelfRel(),
                linkTo(methodOn(ProductTypeController.class).getAllProductTypes(null, null)).withRel("all-product-types")
        );

        return ResponseEntity.ok(new ResponseApi<>(entityModel, HttpStatus.OK));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseApi<EntityModel<ProductTypeResponseDto>>> updateProductType(@PathVariable UUID id, @RequestBody ProductTypeRequestDto requestDto) {
        ProductTypeResponseDto productUpdated = productTypeService.update(id, requestDto)
                .orElseThrow(() -> new ResourceNotFoundException("Could not update. Product Type not found with ID: " + id));

        EntityModel<ProductTypeResponseDto> entityModel = EntityModel.of(productUpdated,
                linkTo(methodOn(ProductTypeController.class).getProductById(id)).withSelfRel(),
                linkTo(methodOn(ProductTypeController.class).getAllProductTypes(null, null)).withRel("all-product-types")
        );

        return ResponseEntity.ok(new ResponseApi<>(entityModel, HttpStatus.OK));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseApi<Void>> deleteUnitMeasure(@PathVariable UUID id) {
        productTypeService.deleteById(id);
        ResponseApi<Void> response = new ResponseApi<>(HttpStatus.OK, "The Product Type has been successfully deleted.");
        return ResponseEntity.ok(response);
    }
}
