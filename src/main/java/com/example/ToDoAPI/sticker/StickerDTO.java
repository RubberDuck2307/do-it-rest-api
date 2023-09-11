package com.example.ToDoAPI.sticker;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class StickerDTO {
    @NotNull(message = "{validation.id.not_null}")
    private Long id;
    @Size(max = 255, message = "{validation.name.size.too_long}")
    private String headline;
    private String text;
    private String color;


}
