package com.vnpost.elearning.api.chat;
//
//import eln.backend.dto.ChatRoomDTO;
//import eln.backend.dto.ServiceResult;
//import eln.backend.dto.UsersDTO;
//import eln.backend.services.ChatRoomService;
import com.vnpost.elearning.dto.ChatRoomDTO;
import com.vnpost.elearning.dto.ServiceResult;
import com.vnpost.elearning.dto.UsersDTO;
import com.vnpost.elearning.security.MyUser;
import com.vnpost.elearning.service.ChatRoomService;
import com.vnpost.elearning.service.UserService;
import eln.common.core.entities.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chatroom")
public class ChatRoomApi {

    @Autowired
    private ChatRoomService chatRoomService;
    
    @Autowired private UserService userService;


    @GetMapping
    public ChatRoomDTO getChatRoomById(@RequestParam(name = "id") Long id) {
        return chatRoomService.getById(id);
    }

    @GetMapping("/users")
    public List<UsersDTO> getUsersByChatRoomId(@RequestParam(name = "id") Long id, @RequestParam(required = false, defaultValue = "10") Integer size,
                                               @RequestParam(required = false, defaultValue = "1") Integer page) {


        return chatRoomService.findAllUserByChatRoomId(id);
    }

    @PostMapping("/users")
    public ResponseEntity<ServiceResult> getAllUserByChatRoomId(@RequestParam(required = false, defaultValue = "5") Integer size,
                                                                @RequestParam(required = false, defaultValue = "1") Integer page,
                                                                @RequestBody ChatRoomDTO chatRoomDTO) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Long totalItem = chatRoomService.countUser(chatRoomDTO);
        Integer totalPage = (int) Math.ceil((double) totalItem / size);
        List<UsersDTO> listUser = chatRoomService.findAllUser(chatRoomDTO,pageable);
        ServiceResult serviceResult = new ServiceResult(listUser, totalPage, page);
        return ResponseEntity.ok(serviceResult);
    }

    @DeleteMapping("/user")
    public Map<String, String> removeUserFromChatRoom(@RequestParam(name = "user_id") Long userId, @RequestParam(name = "chatroom_id") Long chatroomId) {
        chatRoomService.removeUserFromChatRoom(userId, chatroomId);
        Map<String, String> result = new HashMap<>();
        result.put("code", "205");
        result.put("message", "success");
        return result;
    }

    @PostMapping("/user")
    public Map<String, String> addUserToChatRoom(@RequestBody ChatRoomDTO chatRoomDTO) {
        chatRoomService.addUserToChatRoom(chatRoomDTO);
        Map<String, String> result = new HashMap<>();
        result.put("code", "205");
        result.put("message", "success");
        return result;
    }
    @GetMapping("/all")
    public List<ChatRoomDTO> listChatRoomByCourseId(@RequestParam(name = "id") Long id) {
        return chatRoomService.findAllByCourseId(id);
    }
    @GetMapping("/all/user")
    public ResponseEntity<ServiceResult> listChatRoomByCourseIdAndCurrentUser(@RequestParam(name = "id") Long id) {
        MyUser curUser = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (curUser.getId() == null) {
            return ResponseEntity.ok(new ServiceResult("No User.java","400"));
        }
        User curUserEntity = userService.findById(curUser.getId()).get();
        return ResponseEntity.ok(new ServiceResult(chatRoomService.findAllByCourseIdAndUsersContains(id,curUserEntity),"success","200"));
    }
}
