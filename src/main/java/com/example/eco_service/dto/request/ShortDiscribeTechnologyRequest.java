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
@Schema(description = "Запрос для сущности ShortDiscribeTechnology")
public class ShortDiscribeTechnologyRequest {

    @Schema(description = "Краткое описание технологии", example = "Мокрая очистка газов")
    @NotEmpty(message = "Описание технологии обязательно")
    @Size(max = 50, message = "Максимум 50 символов")
    private String technology;
}
