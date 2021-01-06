package com.vnpost.elearning.service;

import com.querydsl.core.BooleanBuilder;
import com.vnpost.elearning.converter.ChatRoomConverter;
import com.vnpost.elearning.converter.UserConverter;
import com.vnpost.elearning.dto.ChatRoomDTO;
import com.vnpost.elearning.dto.UsersDTO;
import eln.common.core.entities.chat.ChatRoom;
import eln.common.core.entities.user.QUser;
import eln.common.core.entities.user.User;
import eln.common.core.repository.ChatRoomRepository;
import eln.common.core.repository.course.CourseRepository;
import eln.common.core.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatRoomService extends CommonRepository<ChatRoom, ChatRoomRepository> {

  private final QUser Q = QUser.user;
  @Autowired private ChatRoomRepository chatRoomRepository;
  @Autowired private CourseRepository courseRepository;
  @Autowired private ChatRoomConverter converter;
  @Autowired private UserRepository userRepository;
  @Autowired private UserConverter userConverter;

  public ChatRoomService(ChatRoomRepository repo) {
    super(repo);
  }

  public List<ChatRoomDTO> findAllByCourseId(Long id) {
    return chatRoomRepository.findByCourseId(id).stream()
        .map(item -> converter.convertToDTO(item))
        .collect(Collectors.toList());
  }

  public ChatRoomDTO getById(Long id) {
    return converter.convertToDTO(chatRoomRepository.findById(id).get());
  }

  public List<UsersDTO> findAllUserByChatRoomId(Long id) {
    return chatRoomRepository.findById(id).get().getUsers().stream()
        .map(item -> userConverter.convertToDTO(item))
        .collect(Collectors.toList());
  }

  public void removeUserFromChatRoom(Long userId, Long chatRoomId) {
    chatRoomRepository.removeUserFromChatRoom(userId, chatRoomId);
  }

  public void addUserToChatRoom(ChatRoomDTO chatRoomDTO) {
    chatRoomRepository.addUserToChatRoom(chatRoomDTO.getUserId(), chatRoomDTO.getId());
  }

  public void addUserToChatRoom(Long userId, Long chatRomId) {
    chatRoomRepository.addUserToChatRoom(userId, chatRomId);
  }

  public List<ChatRoomDTO> findAllByCourseIdAndUsersContains(Long courseId, User user) {
    List<ChatRoom> chatRooms = chatRoomRepository.findAllByCourseIdAndUsersContains(courseId, user);
    ChatRoom chatRoomForAll = chatRoomRepository.findByCourseIdAndType(courseId, "ALL");
    if (!isListContainChatRoomForAll(chatRooms, chatRoomForAll)) {
      chatRooms.add(chatRoomForAll);
    }

    return chatRooms.stream()
        .map(item -> converter.convertToDTO(item))
        .collect(Collectors.toList());
  }

  public boolean isListContainChatRoomForAll(List<ChatRoom> chatRooms, ChatRoom chatRoomForAll) {
    if (chatRooms.isEmpty()) return false;
    if (chatRooms == null) return false;
    for (ChatRoom chatRoom : chatRooms) {
      if (chatRoom.getId() == chatRoomForAll.getId()) return true;
    }
    return false;
  }

  public Long countUser(ChatRoomDTO chatRoomDTO) {
    BooleanBuilder booleanBuilder = booleanBuilder(chatRoomDTO);
    return userRepository.count(booleanBuilder);
  }

  public List<UsersDTO> findAllUser(ChatRoomDTO chatRoomDTO, Pageable pageable) {
    BooleanBuilder booleanBuilder = booleanBuilder(chatRoomDTO);
    return userRepository.findAll(booleanBuilder, pageable).getContent().stream()
        .map(userConverter::convertToDTO)
        .collect(Collectors.toList());
  }

  public Optional<ChatRoom> findByCourseIdAndName(Long courseId, String name) {
    return repo.findByCourseIdAndName(courseId, name);
  }

  private BooleanBuilder booleanBuilder(ChatRoomDTO chatRoomDTO) {
    BooleanBuilder builder = new BooleanBuilder();
    if (chatRoomDTO.getId() != null) {
      builder.and(Q.chatRooms.contains(chatRoomRepository.findById(chatRoomDTO.getId()).get()));
    }
    if (chatRoomDTO.getSearchUser() != null) {
      builder.and(Q.username.contains(chatRoomDTO.getSearchUser()));
    }
    return builder;
  }
}
