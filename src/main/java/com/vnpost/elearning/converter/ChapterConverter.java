package com.vnpost.elearning.converter;

import com.vnpost.elearning.dto.course.ChapterDTO;
import com.vnpost.elearning.service.CompetitionChapterService;
import eln.common.core.entities.chapter.Chapter;
import eln.common.core.entities.competition.CompetitionChapter;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class ChapterConverter implements IDTO<ChapterDTO>, IEntity<Chapter> {

  private ModelMapper modelMapper;
  private CompetitionChapterService competitionChapterService;

  @Override
  public ChapterDTO convertToDTO(Object entity) {
    Chapter chapter = (Chapter) entity;
    ChapterDTO chapterDTO = modelMapper.map(chapter, ChapterDTO.class);
    chapterDTO.setRoundId(competitionChapterService.findidRoundByidChapter(chapter.getId()));
    Optional<CompetitionChapter> optional =
        competitionChapterService.findByChapterId(chapter.getId());
    chapterDTO.setCompetitionId(optional.map(CompetitionChapter::getCompetitionId).orElse(null));
    return chapterDTO;
  }

  public List<ChapterDTO> convertToList(List<Chapter> chapters) {
    List<ChapterDTO> chapterDTOS = new ArrayList<>();
    for (Chapter c : chapters) {
      chapterDTOS.add(convertToDTO(c));
    }
    return chapterDTOS;
  }

  @Override
  public Chapter convertToEntity(Object dto) {
    ChapterDTO chapterDTO = (ChapterDTO) dto;
    Chapter chapter = modelMapper.map(chapterDTO, Chapter.class);
    return chapter;
  }
}
