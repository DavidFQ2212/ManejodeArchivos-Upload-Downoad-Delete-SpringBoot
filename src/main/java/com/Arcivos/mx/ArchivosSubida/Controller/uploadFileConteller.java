package com.Arcivos.mx.ArchivosSubida.Controller;




import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.Arcivos.mx.ArchivosSubida.Service.UploadFileService;

import jakarta.servlet.http.HttpServletRequest;



import java.io.FileNotFoundException;
import java.io.IOException;

import java.net.MalformedURLException;
import java.util.List;

@Controller
public class uploadFileConteller {

    @Autowired
    private UploadFileService uploadFileService;

     @GetMapping("/")
    public String listFiles(Model model) {
        List<String> fileNames = uploadFileService.getAllFileNames();
        model.addAttribute("files", fileNames);
        return "uploadfileView";
    }

    @PostMapping("/upload")
public  String uploadFile(@RequestParam("file") MultipartFile[] files) {
    try {
        for (MultipartFile file : files) {
            uploadFileService.saveFile(file);
        }
        return "redirect:/";
    } catch (IOException e) {
        e.printStackTrace();
        return "redirect:/";
    }
}
@GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) throws MalformedURLException, FileNotFoundException {
        // Cargar el archivo como recurso
        Resource resource = uploadFileService.loadFileAsResource(fileName);

        // Obtener el tipo MIME del archivo
        String contentType;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            contentType = "application/octet-stream";
        }

        // Devolver la respuesta de descarga
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/delete/{fileName}")
    public String deleteFile(@PathVariable String fileName) {
        try {
            uploadFileService.deleteFile(fileName);
            return "redirect:/";
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }
}

    

