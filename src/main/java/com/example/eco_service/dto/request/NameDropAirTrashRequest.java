package com.example.eco_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос для сущности NameDropAirTrash")
public class NameDropAirTrashRequest {

    @Schema(description = "Наименование выброса", example = "Диоксид серы")
    @NotEmpty(message = "Наименование выброса обязательно")
    @Size(max = 50, message = "Максимум 50 символов")
    private String name_drop_air_trash;
}
