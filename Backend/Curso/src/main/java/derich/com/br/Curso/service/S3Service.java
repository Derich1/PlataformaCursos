package derich.com.br.Curso.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class S3Service {

    private final S3Client s3Client;

    private final Region region = Region.US_EAST_1;

    @Value("${aws.bucketName}")
    private String bucketName;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    /**
     * Faz upload de um arquivo Multipart (por ex., recebido via Controller).
     * Retorna a chave (key) gerada no bucket.
     */
    public String uploadFile(String folder, MultipartFile file) throws IOException {
        // Gerar um nome único para o objeto, ex: folder/uuid-nomeOriginal.ext
        String key = folder + "/" + System.currentTimeMillis() + "-" + file.getOriginalFilename();

        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();

        // Converte MultipartFile para RequestBody
        RequestBody requestBody = RequestBody.fromBytes(file.getBytes());

        s3Client.putObject(putRequest, requestBody);

        return key;
    }

    /**
     * Faz download: retorna um byte[] contendo o conteúdo do objeto.
     */
    public byte[] downloadFile(String key) {
        GetObjectRequest getRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(getRequest);
        return objectBytes.asByteArray();
    }

    /**
     * Deleta um objeto no bucket pelo key.
     */
    public void deleteFile(String key) {
        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        s3Client.deleteObject(deleteRequest);
    }

    /**
     * Lista todos os objetos dentro de um “prefix” (pasta):
     * retorna lista de keys.
     */
    public List<String> listFiles(String prefix) {
        ListObjectsV2Request listReq = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(prefix)
                .build();

        ListObjectsV2Response listRes = s3Client.listObjectsV2(listReq);
        return listRes.contents()
                .stream()
                .map(S3Object::key)
                .collect(Collectors.toList());
    }

    /**
     * Gera uma URL pré-assinada válida por um tempo (ex: 15 minutos)
     * para download público condicionado sem precisar ACL pública.
     */
    public URL generatePresignedUrl(String key, Duration duration) {
        GetObjectRequest getReq = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        // Precisa do S3Presigner (parte do SDK) para gerar a URL
        S3Presigner presigner = S3Presigner.builder()
                .region(region)
                .credentialsProvider(s3Client.serviceClientConfiguration().credentialsProvider())
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(duration)
                .getObjectRequest(getReq)
                .build();

        PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);
        presigner.close();

        return presignedRequest.url();
    }
}

