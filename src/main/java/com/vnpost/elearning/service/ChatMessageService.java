package com.vnpost.elearning.service;

import com.vnpost.elearning.dto.ChatMessage;
import eln.common.core.entities.chat.ChatMessageEntity;
import eln.common.core.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatMessageService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;


    public ChatMessage save(ChatMessage chatMessage, Long tableId) {
        ChatMessageEntity entity = new ChatMessageEntity();
        entity.setSender(chatMessage.getSender());
        entity.setContent(chatMessage.getContent());
        entity.setTableId(tableId);
        chatMessageRepository.save(entity);
        return chatMessage;
    }


    public List<ChatMessage> findAllByTableId(Long tableId) {
        List<ChatMessageEntity> chatMessageEntities = chatMessageRepository.findByTableId(tableId);
        List<ChatMessage> result = new ArrayList<>();
        for (ChatMessageEntity entity : chatMessageEntities) {
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setSender(entity.getSender());
            chatMessage.setContent(entity.getContent());
            chatMessage.setType(ChatMessage.MessageType.CHAT);
            result.add(chatMessage);
        }
        return result;
    }
}
