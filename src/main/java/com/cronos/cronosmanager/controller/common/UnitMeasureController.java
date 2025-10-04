package com.cronos.cronosmanager.controller.common;

import com.cronos.cronosmanager.dto.common.response.UnitOfMeasureResponseDto;
import com.cronos.cronosmanager.dto.common.request.UnitOfMeasureRequest;
import com.cronos.cronosmanager.exception.common.ResourceNotFoundException;
import com.cronos.cronosmanager.model.response.ResponseApi;
import com.cronos.cronosmanager.service.common.IUnitMeasure;
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
@RequestMapping("/unit-measure")
public class UnitMeasureController {

    private final IUnitMeasure unitMeasureService;

    @GetMapping
    public ResponseEntity<ResponseApi<PagedModel<EntityModel<UnitOfMeasureResponseDto>>>> getAllUnitMeasures(Pageable pageable, PagedResourcesAssembler<UnitOfMeasureResponseDto> assembler) {
        Page<UnitOfMeasureResponseDto> pageUnits = unitMeasureService.findAll(pageable);
        PagedModel<EntityModel<UnitOfMeasureResponseDto>> pagedModel = assembler.toModel(pageUnits, unit ->
                EntityModel.of(unit,
                        linkTo(methodOn(UnitMeasureController.class).getUnitMeasureById(unit.getId())).withSelfRel(),
                        linkTo(methodOn(UnitMeasureController.class).getAllUnitMeasures(null, null)).withRel("all-units-of-measure")
                )
        );

        return ResponseEntity.ok(new ResponseApi<>(pagedModel, HttpStatus.OK));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseApi<EntityModel<UnitOfMeasureResponseDto>>> getUnitMeasureById(@PathVariable UUID id) {
        UnitOfMeasureResponseDto unit = unitMeasureService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("The Unit Of Measure with ID: %s is not found in our records.", id)));

        EntityModel<UnitOfMeasureResponseDto> entityModel = EntityModel.of(unit,
                linkTo(methodOn(UnitMeasureController.class).getUnitMeasureById(id)).withSelfRel(),
                linkTo(methodOn(UnitMeasureController.class).getAllUnitMeasures(null, null)).withRel("all-units-of-measure")
        );

        return ResponseEntity.ok(new ResponseApi<>(entityModel, HttpStatus.OK));
    }

    @PostMapping
    public ResponseEntity<ResponseApi<EntityModel<UnitOfMeasureResponseDto>>> saveUnitMeasure(@Valid @RequestBody UnitOfMeasureRequest request) {
        UnitOfMeasureResponseDto unit = unitMeasureService.save(request);

        EntityModel<UnitOfMeasureResponseDto> entityModel = EntityModel.of(unit,
                linkTo(methodOn(UnitMeasureController.class).getUnitMeasureById(unit.getId())).withSelfRel(),
                linkTo(methodOn(UnitMeasureController.class).getAllUnitMeasures(null, null)).withRel("all-units-of-measure")
        );

        return ResponseEntity.ok(new ResponseApi<>(entityModel, HttpStatus.OK));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseApi<EntityModel<UnitOfMeasureResponseDto>>> updateUnitMeasure(@PathVariable UUID id, @RequestBody UnitOfMeasureRequest unitOfMeasureRequest) {
        UnitOfMeasureResponseDto unitUpdated = unitMeasureService.update(id, unitOfMeasureRequest)
                .orElseThrow(() -> new ResourceNotFoundException("Could not update. Unit Of Measure not found with ID: " + id));

        EntityModel<UnitOfMeasureResponseDto> entityModel = EntityModel.of(unitUpdated,
                linkTo(methodOn(UnitMeasureController.class).getUnitMeasureById(id)).withSelfRel(),
                linkTo(methodOn(UnitMeasureController.class).getAllUnitMeasures(null, null)).withRel("all-units-of-measure")
        );

        return ResponseEntity.ok(new ResponseApi<>(entityModel, HttpStatus.OK));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseApi<Void>> deleteUnitMeasure(@PathVariable UUID id) {
        unitMeasureService.deleteById(id);
        ResponseApi<Void> response = new ResponseApi<>(HttpStatus.OK, "The Unit Of Measure has been successfully deleted.");
        return ResponseEntity.ok(response);
    }
}
