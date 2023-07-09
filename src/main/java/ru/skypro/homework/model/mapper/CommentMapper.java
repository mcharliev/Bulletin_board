package ru.skypro.homework.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.model.dto.CommentDto;
import ru.skypro.homework.model.entity.CommentEntity;

import java.util.Collection;
import java.util.List;


@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "pk", source = "id")
    @Mapping(target = "author", source = "author.id")
    @Mapping(target = "createAt", ignore = true)
    CommentDto commentEntityToCommentDto(CommentEntity entity);

    @Mapping(target ="author",ignore = true)
    @Mapping(target = "createAt", ignore = true)
    CommentEntity commentDtoToCommentEntity(CommentDto dto);

    List<CommentEntity> toEntityList(Collection<CommentDto> commentDtoList);

    List<CommentDto> toDtoList(Collection<CommentEntity> commentEntitiesList);
}
