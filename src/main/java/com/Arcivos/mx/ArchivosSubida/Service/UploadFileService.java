package com.Arcivos.mx.ArchivosSubida.Service;


import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class UploadFileService {

    private String uploadFolder = "./files/";

        public void saveFile(MultipartFile file) throws IOException {
            if (!file.isEmpty()) {
                byte[] bytes = file.getBytes();
                Path path = Paths.get(uploadFolder).resolve(file.getOriginalFilename());
                Files.write(path, bytes);
            }
        }


    public List<String> getAllFileNames() {
    File folder = new File(uploadFolder);
    File[] listOfFiles = folder.listFiles();

    List<String> fileNames = new ArrayList<>();
    if (listOfFiles != null) {
        for (File file : listOfFiles) {
            if (file.isFile()) {
                fileNames.add(file.getName());
            }
        }
    }

    return fileNames;
}

public String getFilePath(String fileName) {
    return uploadFolder + fileName;
}
 public Resource loadFileAsResource(String fileName) throws MalformedURLException, FileNotFoundException {
        Path filePath = Paths.get(uploadFolder).resolve(fileName).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        
        if (resource.exists()) {
            return resource;
        } else {
            throw new FileNotFoundException("Archivo no encontrado: " + fileName);
        }

    }
    public void deleteFile(String fileName) throws IOException {
        Path filePath = Paths.get(uploadFolder).resolve(fileName);
        Files.deleteIfExists(filePath);
    }
}

