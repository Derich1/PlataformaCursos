package derich.com.br.Curso.controller;

import derich.com.br.Curso.service.S3Service;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/s3")
public class S3Controller {

    private final S3Service s3Service;

    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    /**
     * Endpoint para upload de arquivo:
     * Parâmetro: pasta (folder) e arquivo.
     * Retorna a key do objeto no S3.
     */
    @PostMapping("/upload/{folder}")
    public ResponseEntity<String> uploadFile(
            @PathVariable String folder,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        String key = s3Service.uploadFile(folder, file);
        return ResponseEntity.ok(key);
    }

    /**
     * Endpoint para download do arquivo pelo key.
     * Retorna o conteúdo binário para o cliente (com headers adequados).
     */
    @GetMapping("/download/{key}")
    public ResponseEntity<ByteArrayResource> downloadFile(
            @PathVariable String key
    ) {
        byte[] data = s3Service.downloadFile(key);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity.ok()
                .contentLength(data.length)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + key + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    /**
     * Endpoint para deletar um arquivo do S3.
     */
    @DeleteMapping("/delete/{key}")
    public ResponseEntity<Void> deleteFile(@PathVariable String key) {
        s3Service.deleteFile(key);
        return ResponseEntity.noContent().build();
    }

    /**
     * Listar todos os arquivos (keys) dentro de uma "pasta" (prefix).
     */
    @GetMapping("/list/{prefix}")
    public ResponseEntity<List<String>> listFiles(@PathVariable String prefix) {
        List<String> keys = s3Service.listFiles(prefix);
        return ResponseEntity.ok(keys);
    }

    /**
     * Retorna uma URL pré-assinada para download (válida por 15 minutos, por exemplo).
     */
    @GetMapping("/presigned-url/{key}")
    public ResponseEntity<String> getPresignedUrl(@PathVariable String key) {
        URL url = s3Service.generatePresignedUrl(key, Duration.ofMinutes(15));
        return ResponseEntity.ok(url.toString());
    }
}

