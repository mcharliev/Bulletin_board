package ru.skypro.homework.model.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.model.dto.CommentDto;
import ru.skypro.homework.model.dto.ResponseWrapperCommentDto;
import ru.skypro.homework.model.entity.CommentEntity;
import ru.skypro.homework.model.entity.ImageEntity;
import ru.skypro.homework.model.entity.UserEntity;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper {

    public CommentDto commentEntityToCommentDto(CommentEntity entity) {
        UserEntity author = entity.getAuthor();
        CommentDto dto = new CommentDto();
        dto.setAuthorFirstName(author.getFirstName());
        dto.setAuthor(author.getId());
        dto.setText(entity.getText());
        dto.setPk(entity.getId());
        dto.setCreatedAt(entity.getCreateAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        ImageEntity image = author.getImage();
        if (image != null) {
            dto.setAuthorImage(String.format("/users/%s/image", author.getImage().getId()));
        }
        return dto;
    }

    public ResponseWrapperCommentDto commentEntityListToWrapperCommentDto(List<CommentEntity> entityList){
        List<CommentDto> commentDtoList = entityList.stream()
                .map(this::commentEntityToCommentDto)
                .collect(Collectors.toList());
        ResponseWrapperCommentDto wrapperCommentDto = new ResponseWrapperCommentDto();
        wrapperCommentDto.setCount(commentDtoList.size());
        wrapperCommentDto.setResults(commentDtoList);
        return wrapperCommentDto;
    }
}
