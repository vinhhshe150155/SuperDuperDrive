package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping
public class FileController {
    private FileService fileService;
    private UserService userService;

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("fileUpload") MultipartFile multipartFile,
                         Authentication authentication, Model model) throws IOException {
        Integer userId = userService.getUser(authentication.getName()).getUserId();
        boolean rs = false;
        String msg = null;
        if (userId != null) {
            if (multipartFile == null || multipartFile.isEmpty()) {
                msg = "File is empty.";
            } else {
                if (fileService.getFileOfUser(userId, multipartFile.getOriginalFilename()) != null) {
                    msg = "File is existed";
                } else {
                    if (fileService.addFile(multipartFile, authentication.getName()) > 0) {
                        msg = "Success";
                        rs = true;
                    } else {
                        msg = "An error occurred";
                    }
                    ;
                }
            }
        }

        model.addAttribute("rs", rs);
        model.addAttribute("msg", msg);
        return "result";
    }

    @GetMapping("/fileDownload")
    public ResponseEntity<Resource> downLoad(@RequestParam String fileName, Authentication authentication, Model model) {
        Integer userId = userService.getUserId(authentication.getName());
        File file = fileService.getFileOfUser(userId, fileName);
        if (userId == null || file == null) {
            return ResponseEntity.notFound().build();
        }
        byte[] fileData = file.getFileData();
        ByteArrayResource resource = new ByteArrayResource(fileData);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"");
        headers.add(HttpHeaders.CONTENT_TYPE, file.getContentType());

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    @GetMapping("/deleteFile")
    public String delete(@RequestParam Integer fileId, Authentication authentication, Model model) {
        Integer userId = userService.getUserId(authentication.getName());
        boolean rs = userId != null && fileService.deleteFile(fileId) > 0;
        model.addAttribute("rs", rs);
        return "result";
    }
}
