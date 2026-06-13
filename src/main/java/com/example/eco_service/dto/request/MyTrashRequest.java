package com.example.eco_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос для сущности MyTrash")
public class MyTrashRequest {

    @Schema(description = "ID класса опасности", example = "3")
    @NotNull(message = "ID класса опасности обязателен")
    private Long id_class_danger;

    @Schema(description = "ID отхода из справочника", example = "12")
    @NotNull(message = "ID отхода обязателен")
    private Long id_magazin_trash;

    @Schema(description = "ID предприятия", example = "7")
    @NotNull(message = "ID предприятия обязателен")
    private Long id_magasin_factory;

    @Schema(description = "Количество отхода (тонн)", example = "150.75")
    @NotNull(message = "Количество отхода обязательно")
    @DecimalMin(value = "0.0", inclusive = false, message = "Количество должно быть больше 0")
    private Float value_trash;
}
