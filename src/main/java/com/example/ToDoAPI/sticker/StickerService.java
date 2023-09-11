package com.example.ToDoAPI.sticker;

import com.example.ToDoAPI.security.EntityAccessHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class StickerService {

    private final StickerRepository stickerRepository;
    private final ModelMapper modelMapper;
    private final EntityAccessHelper<Sticker> entityAccessHelper;
    public ResponseEntity<StickerDTO> createSticker(StickerCreateDTO stickerCreateDTO){
        String color = stickerCreateDTO.getColor();
        Sticker sticker = new Sticker();
        if (color != null) {
            sticker.setColor(color);
        }
        else {
            StickerColors[] colors = StickerColors.values();
            int rnd = new Random().nextInt(colors.length);
            sticker.setColor(colors[rnd].toString());
        }
        sticker.setUserId(entityAccessHelper.getLoggedUserId());
        sticker = stickerRepository.save(sticker);
        StickerDTO stickerDTO = modelMapper.map(sticker, StickerDTO.class);
        return new ResponseEntity<>(stickerDTO, HttpStatus.OK);
    }

    public ResponseEntity<List<StickerDTO>> getAllStickers(){
        List<Sticker> stickers = stickerRepository.findAllByUserIdOrderByIdAsc(entityAccessHelper.getLoggedUserId());
        List<StickerDTO>  stickerDTOS = new ArrayList<>();
        stickers.forEach(sticker -> {
            StickerDTO stickerDTO = modelMapper.map(sticker, StickerDTO.class);
            stickerDTOS.add(stickerDTO);
        });
        return new ResponseEntity<>(stickerDTOS, HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<StickerDTO> modifySticker(StickerDTO stickerDTO, long id){
        Sticker sticker = stickerRepository.findById(id).orElseThrow(NoSuchElementException::new);
        if (!entityAccessHelper.hasUserAccessTo(sticker)) {
           throw new AccessDeniedException("Access denied");
        }
        sticker.setText(stickerDTO.getText());
        sticker.setHeadline(stickerDTO.getHeadline());

        stickerDTO = modelMapper.map(sticker, StickerDTO.class);
        return new ResponseEntity<>(stickerDTO, HttpStatus.OK);
    }

    public ResponseEntity<String> deleteSticker(Long id){
        if (!entityAccessHelper.hasUserAccessTo(stickerRepository.findById(id).orElseThrow(NoSuchElementException::new))) {
            throw new AccessDeniedException("Access denied");
        }
        stickerRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
