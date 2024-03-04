package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    private FileMapper fileMapper;
    private UserMapper userMapper;

    public FileService(FileMapper fileMapper, UserMapper userMapper) {
        this.fileMapper = fileMapper;
        this.userMapper = userMapper;
    }

    File getFileById(Integer fileId) {
        return fileMapper.getFileById(fileId);
    }

    public File getFileOfUser(Integer userId, String fileName) {
        return fileMapper.getFileOfUser(userId, fileName);
    }

    public List<File> getUserFiles(Integer userId) {
        return fileMapper.getFileListByUserId(userId);
    }

    public Integer addFile(MultipartFile uploadFile, String userName) throws IOException {
        String fileName = uploadFile.getOriginalFilename();
        Integer userId = userMapper.getUser(userName).getUserId();
        String contentType = uploadFile.getContentType();
        String fileSize = String.valueOf(uploadFile.getSize());
        byte[] fileData = uploadFile.getBytes();
        return fileMapper.insertFile(
            new File(
                null,
                fileName,
                contentType,
                fileSize,
                userId,
                fileData));
    }

    public Integer deleteFile(Integer fileId) {
        return fileMapper.deleteFileById(fileId);
    }

}