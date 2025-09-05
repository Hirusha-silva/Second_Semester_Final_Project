package com.example.back_end.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class FileUploadUtil {
    public static void saveFile(String filePath, String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get("/Users/hirushasilva/Documents/study/Second Semester/second semester final project/ADD Final Project/Second Semester Final Project/Back_End/src/main/java/com/example/back_end/uploads"+filePath);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        try(InputStream inputStream = multipartFile.getInputStream();) {
            Path filepath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filepath, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {

        }

    }
}
