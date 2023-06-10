package ru.skypro.homework.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.model.dto.CommentDto;
import ru.skypro.homework.model.entity.CommentEntity;


@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "pk", source = "id")
    @Mapping(target = "author", source = "author.id")
    CommentDto commentEntityToCommentDto(CommentEntity entity);

    @Mapping(target ="author",ignore = true)
    CommentEntity commentDtoToCommentEntity(CommentDto dto);
}
