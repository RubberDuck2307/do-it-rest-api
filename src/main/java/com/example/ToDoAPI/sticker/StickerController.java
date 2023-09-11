package com.example.ToDoAPI.sticker;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "${app.url}", allowCredentials = "true")
@RequestMapping("api/v1/sticker")
@RequiredArgsConstructor
@RestController
public class StickerController {
    private final StickerService stickerService;
    @PostMapping("/")
    public ResponseEntity<StickerDTO> createSticker(@RequestBody StickerCreateDTO dto){
       return stickerService.createSticker(dto);
    }

    @GetMapping("/")
    public ResponseEntity<List<StickerDTO>> getAllStickers(){
        return stickerService.getAllStickers();
    }


    @PutMapping("/{id}")
    public ResponseEntity<StickerDTO> modifySticker(@Valid @RequestBody StickerDTO stickerDTO, @PathVariable Long id){
        return stickerService.modifySticker(stickerDTO, id);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSticker (@PathVariable Long id){
        return stickerService.deleteSticker(id);
    }

}
