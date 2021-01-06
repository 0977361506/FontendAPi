package com.vnpost.elearning.converter;

import com.vnpost.elearning.dto.ChatRoomDTO;
import eln.common.core.entities.chat.ChatRoom;
import eln.common.core.entities.user.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ChatRoomConverter implements IDTO<ChatRoomDTO>, IEntity<ChatRoom> {
  @Autowired private ModelMapper modelMapper;

  public ChatRoomDTO convertToDTO(Object entity) {
    ChatRoom chatroom = (ChatRoom) entity;
    if (chatroom == null) return null;
    List<Long> userIds = new ArrayList<>();
    if (!chatroom.getUsers().isEmpty()) {
      for (User user : chatroom.getUsers()) {
        userIds.add(user.getId());
      }
    }

    ChatRoomDTO dto = modelMapper.map(chatroom, ChatRoomDTO.class);
    dto.setUserIds(userIds);
    return dto;
  }

  @Override
  public ChatRoom convertToEntity(Object dto) {
    ChatRoomDTO chatRoomDTO = (ChatRoomDTO) dto;

    ChatRoom chatRoom = modelMapper.map(chatRoomDTO, ChatRoom.class);
    return chatRoom;
  }
}
