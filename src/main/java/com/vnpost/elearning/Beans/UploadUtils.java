package com.vnpost.elearning.Beans;


import com.vnpost.elearning.dto.UploadDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
 

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.util.UUID;

@Component
public class UploadUtils {
    @Value("${image.upload.folder}")
    private String imageUploadFolder;
    @Value("${excel.upload.folder}")
    private String excelUploadFolder;
    @Value("${diskpart}")
    private String diskPart;

    public String saveFile(MultipartFile multipartFile, String filePath) {
        String originalName = UUID.randomUUID() + multipartFile.getOriginalFilename();
        File file = new File(filePath + originalName);
        try {
            if (file.exists()) {
                file.delete();
            }
            if (!originalName.equals("")) {
                multipartFile.transferTo(file);
                return originalName;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
        }
        return "";
    }

    public void removeFile(String filePath) {
        File file = new File(filePath);
        file.delete();
    }

    public UploadDTO saveImageFile(UploadDTO uploadDTO) {
        MultipartFile multipartFile = uploadDTO.getMultipartFile();

        String originName = multipartFile.getOriginalFilename();


        StringBuilder nameFileRoot = new StringBuilder(imageUploadFolder + UUID.randomUUID() + originName);
        StringBuilder nameFile = new StringBuilder(imageUploadFolder+originName);
        File file = new File(nameFile.toString());
        try {
            multipartFile.transferTo(file);
            String typeImage = StringUtils.substringAfterLast(originName, ".");
            ImageUtils.compressImage(nameFile.toString(),nameFileRoot.toString(),0.3f,typeImage);
            file.delete();
            String src = nameFileRoot.toString();
            src = src.replace("//", "/");
            src = src.replace(diskPart, "");
            uploadDTO.setSrc(src);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uploadDTO;
    }
    public UploadDTO saveImageBase64(UploadDTO uploadDTO) throws Exception {
        if (StringUtils.isBlank(uploadDTO.getBase64()) || StringUtils.isBlank(uploadDTO.getExtension()) || StringUtils.isBlank(uploadDTO.getName())) throw new Exception("Not enough information");
        StringBuilder nameFileRoot = new StringBuilder(imageUploadFolder + UUID.randomUUID() + uploadDTO.getName());
        StringBuilder nameFile = new StringBuilder(imageUploadFolder + UUID.randomUUID() +uploadDTO.getName());

        //write file

        String[] base64Code = uploadDTO.getBase64().split(",");
        byte[] data = DatatypeConverter.parseBase64Binary((base64Code.length > 1) ? base64Code[1] : base64Code[0]);
        File file = new File(nameFile.toString());
        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
        outputStream.write(data);

        //compress file
        String src;
        if (( file.length() / 1048576 ) > 5) {
            ImageUtils.compressImage(nameFile.toString(),nameFileRoot.toString(),0.3f,uploadDTO.getExtension());
            file.delete();
            src = nameFileRoot.toString();
        }
        else {
            src = nameFile.toString();
        }
        src = src.replace("//", "/");
        src = src.replace(diskPart, "");
        uploadDTO.setSrc(src.replace("/data",""));
        return uploadDTO;
    }

    public UploadDTO saveExcelFile(UploadDTO uploadDTO) {
        MultipartFile multipartFile = uploadDTO.getMultipartFile();

        StringBuilder originName =new StringBuilder(excelUploadFolder + UUID.randomUUID() +multipartFile.getOriginalFilename());
        File file = new File(originName.toString());
        try {
            multipartFile.transferTo(file);
            String src = originName.toString();
            uploadDTO.setSrc(src);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uploadDTO;
    }
}
