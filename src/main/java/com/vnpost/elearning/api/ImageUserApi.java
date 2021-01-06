package com.vnpost.elearning.api;

import com.vnpost.elearning.Beans.CoreConstant;
import com.vnpost.elearning.Beans.UploadUtils;
import com.vnpost.elearning.dto.ServiceResult;
import com.vnpost.elearning.dto.UploadDTO;
import com.vnpost.elearning.dto.UsersDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/font/")
public class ImageUserApi {
  @Value("${image.upload.folder}")
  private String imageUploadFolder;


  @Value("${diskpart}")
  private String diskPart;

  @Autowired private UploadUtils uploadUtils;

  @PostMapping("/image")
  public String uploadFile(@ModelAttribute UsersDTO usersDTO) {
    String fileName = uploadUtils.saveFile(usersDTO.getImage(), imageUploadFolder);
    String dir =  CoreConstant.URL_IMAGE_VIEW + fileName;
    dir = dir.replace("//", "/");
    dir = dir.replace(diskPart, "");
    return dir;
  }
  @PostMapping("/image/base64")
  public ResponseEntity<ServiceResult> uploadImageBase64(@RequestBody UploadDTO uploadDTO) {

    try {
      UploadDTO result = uploadUtils.saveImageBase64(uploadDTO);
      return new ResponseEntity<>(new ServiceResult(result,"Success","200"), HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>(new ServiceResult(e.getMessage(),"400"), HttpStatus.BAD_REQUEST);
    }

  }
}
