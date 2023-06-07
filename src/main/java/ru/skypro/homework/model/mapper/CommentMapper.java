package ru.skypro.homework.model.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.skypro.homework.model.dto.CommentDto;
import ru.skypro.homework.model.entity.CommentEntity;


@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "author", source = "author.id")
//    @Mapping(target = "pk", ignore = true)
    CommentDto commentEntityToCommentDto(CommentEntity entity);



    @Mapping(target = "author.id", source = "author")
    CommentEntity commentDtoToCommentEntity(CommentDto dto);

    @AfterMapping
    default void afterCommentDtoToCommentEntity(@MappingTarget CommentDto commentDto, CommentEntity commentEntity){
      commentDto.setAuthor(commentEntity.getAuthor().getId());
    }
}
