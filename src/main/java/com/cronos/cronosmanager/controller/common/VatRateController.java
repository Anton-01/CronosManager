package com.cronos.cronosmanager.controller.common;

import com.cronos.cronosmanager.dto.common.request.VatRateRequestDto;
import com.cronos.cronosmanager.dto.common.response.VatRateResponseDto;
import com.cronos.cronosmanager.exception.common.ResourceNotFoundException;
import com.cronos.cronosmanager.model.response.ResponseApi;
import com.cronos.cronosmanager.service.common.VatRateService;
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
@RequestMapping("/vat-rate")
public class VatRateController {

    private VatRateService vatRateService;

    @GetMapping
    public ResponseEntity<ResponseApi<PagedModel<EntityModel<VatRateResponseDto>>>> getAllVatRates(Pageable pageable, PagedResourcesAssembler<VatRateResponseDto> assembler) {
        Page<VatRateResponseDto> pageRates = vatRateService.findAll(pageable);
        PagedModel<EntityModel<VatRateResponseDto>> pagedModel = assembler.toModel(pageRates, rate ->
                EntityModel.of(rate,
                        linkTo(methodOn(VatRateController.class).getVatRateById(rate.getId())).withSelfRel(),
                        linkTo(methodOn(VatRateController.class).getAllVatRates(null, null)).withRel("all-vat-rates")
                )
        );

        return ResponseEntity.ok(new ResponseApi<>(pagedModel, HttpStatus.OK));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseApi<EntityModel<VatRateResponseDto>>> getVatRateById(@PathVariable UUID id) {
        VatRateResponseDto rate = vatRateService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("The Vat Rate with ID: %s is not found in our records.", id)));

        EntityModel<VatRateResponseDto> entityModel = EntityModel.of(rate,
                linkTo(methodOn(VatRateController.class).getVatRateById(id)).withSelfRel(),
                linkTo(methodOn(VatRateController.class).getAllVatRates(null, null)).withRel("all-vat-rates")
        );

        return ResponseEntity.ok(new ResponseApi<>(entityModel, HttpStatus.OK));
    }

    @PostMapping
    public ResponseEntity<ResponseApi<EntityModel<VatRateResponseDto>>> saveVatRate(@Valid @RequestBody VatRateRequestDto request) {
        VatRateResponseDto rate = vatRateService.save(request);

        EntityModel<VatRateResponseDto> entityModel = EntityModel.of(rate,
                linkTo(methodOn(VatRateController.class).getVatRateById(rate.getId())).withSelfRel(),
                linkTo(methodOn(VatRateController.class).getAllVatRates(null, null)).withRel("all-vat-rates")
        );

        return ResponseEntity.ok(new ResponseApi<>(entityModel, HttpStatus.OK));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseApi<EntityModel<VatRateResponseDto>>> updateUnitMeasure(@PathVariable UUID id, @RequestBody VatRateRequestDto requestDto) {
        VatRateResponseDto rateUpdated = vatRateService.update(id, requestDto)
                .orElseThrow(() -> new ResourceNotFoundException("Could not update. Vat Rate not found with ID: " + id));

        EntityModel<VatRateResponseDto> entityModel = EntityModel.of(rateUpdated,
                linkTo(methodOn(VatRateController.class).getVatRateById(id)).withSelfRel(),
                linkTo(methodOn(VatRateController.class).getAllVatRates(null, null)).withRel("all-vat-rates")
        );

        return ResponseEntity.ok(new ResponseApi<>(entityModel, HttpStatus.OK));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseApi<Void>> deleteUnitMeasure(@PathVariable UUID id) {
        vatRateService.deleteById(id);
        ResponseApi<Void> response = new ResponseApi<>(HttpStatus.OK, "The Vat Rate has been successfully deleted.");
        return ResponseEntity.ok(response);
    }
}
