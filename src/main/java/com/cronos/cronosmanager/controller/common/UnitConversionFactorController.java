package com.cronos.cronosmanager.controller.common;

import com.cronos.cronosmanager.dto.common.request.UnitConversionFactorRequestDto;
import com.cronos.cronosmanager.dto.common.response.UnitConversionFactorResponseDto;
import com.cronos.cronosmanager.exception.common.ResourceNotFoundException;
import com.cronos.cronosmanager.model.response.ResponseApi;
import com.cronos.cronosmanager.service.common.UnitConversionFactorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/unit-conversion-factor")
public class UnitConversionFactorController {

    private final UnitConversionFactorService unitConversionFactorService;

    @GetMapping
    public ResponseEntity<ResponseApi<PagedModel<EntityModel<UnitConversionFactorResponseDto>>>> getAllUnitConversionFactor(Pageable pageable, PagedResourcesAssembler<UnitConversionFactorResponseDto> assembler) {
        Page<UnitConversionFactorResponseDto> pageUnits = unitConversionFactorService.findAll(pageable);
        PagedModel<EntityModel<UnitConversionFactorResponseDto>> pagedModel = assembler.toModel(pageUnits, unit ->
                EntityModel.of(unit,
                        linkTo(methodOn(UnitConversionFactorController.class).getUnitConversionById(unit.getId())).withSelfRel(),
                        linkTo(methodOn(UnitConversionFactorController.class).getAllUnitConversionFactor(null, null)).withRel("all-units-conversion-factor")
                )
        );

        return ResponseEntity.ok(new ResponseApi<>(pagedModel, HttpStatus.OK));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseApi<EntityModel<UnitConversionFactorResponseDto>>> getUnitConversionById(@PathVariable UUID id) {
        UnitConversionFactorResponseDto unit = unitConversionFactorService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("The Unit Conversion Factor with ID: %s is not found in our records.", id)));

        EntityModel<UnitConversionFactorResponseDto> entityModel = EntityModel.of(unit,
                linkTo(methodOn(UnitConversionFactorController.class).getUnitConversionById(id)).withSelfRel(),
                linkTo(methodOn(UnitConversionFactorController.class).getAllUnitConversionFactor(null, null)).withRel("all-units-conversion-factor")
        );

        return ResponseEntity.ok(new ResponseApi<>(entityModel, HttpStatus.OK));
    }

    @PostMapping
    public ResponseEntity<ResponseApi<EntityModel<UnitConversionFactorResponseDto>>> saveUnitConversionFactor(@Valid @RequestBody UnitConversionFactorRequestDto request) {
        log.info(request.toString());
        UnitConversionFactorResponseDto unit = unitConversionFactorService.save(request);

        EntityModel<UnitConversionFactorResponseDto> entityModel = EntityModel.of(unit,
                linkTo(methodOn(UnitConversionFactorController.class).getUnitConversionById(unit.getId())).withSelfRel(),
                linkTo(methodOn(UnitConversionFactorController.class).getAllUnitConversionFactor(null, null)).withRel("all-units-conversion-factor")
        );

        return ResponseEntity.ok(new ResponseApi<>(entityModel, HttpStatus.OK));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseApi<EntityModel<UnitConversionFactorResponseDto>>> updateUnitMeasure(@PathVariable UUID id, @RequestBody UnitConversionFactorRequestDto requestDto) {
        UnitConversionFactorResponseDto unitUpdated = unitConversionFactorService.update(id, requestDto)
                .orElseThrow(() -> new ResourceNotFoundException("Could not update. Unit Conversion Factor not found with ID: " + id));

        EntityModel<UnitConversionFactorResponseDto> entityModel = EntityModel.of(unitUpdated,
                linkTo(methodOn(UnitConversionFactorController.class).getUnitConversionById(id)).withSelfRel(),
                linkTo(methodOn(UnitConversionFactorController.class).getAllUnitConversionFactor(null, null)).withRel("all-units-conversion-factor")
        );

        return ResponseEntity.ok(new ResponseApi<>(entityModel, HttpStatus.OK));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseApi<Void>> deleteUnitMeasure(@PathVariable UUID id) {
        unitConversionFactorService.deleteById(id);
        ResponseApi<Void> response = new ResponseApi<>(HttpStatus.OK, "The Unit Conversion Factor has been successfully deleted.");
        return ResponseEntity.ok(response);
    }
}
