package com.project.SpringBootYouTubeBrito.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRecordDto(@NotBlank String nameProduct, @NotNull BigDecimal valueProduct, @NotBlank String descriptionProduct) {
}
