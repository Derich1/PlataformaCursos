package derich.com.br.Curso;

import derich.com.br.Curso.service.S3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class S3ServiceTest {

    private S3Client s3Client;
    private S3Service s3Service;

    @BeforeEach
    void setUp() {
        s3Client = Mockito.mock(S3Client.class);
        s3Service = new S3Service(s3Client);
        // você pode setar o bucket via reflection se precisar
        // s3Service.setBucketName("test-bucket"); — ou usar setter se disponível
    }

    @Test
    void testUploadFile() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Hello World".getBytes());

        String key = s3Service.uploadFile("uploads", file);

        assertTrue(key.contains("uploads/"));
        assertTrue(key.contains("test.txt"));

        verify(s3Client, times(1)).putObject(any(PutObjectRequest.class), any(RequestBody.class));
    }

    @Test
    void testDownloadFile() {
        String key = "uploads/test.txt";
        byte[] fakeData = "content".getBytes(StandardCharsets.UTF_8);

        ResponseBytes<GetObjectResponse> responseBytes = mock(ResponseBytes.class);
        when(responseBytes.asByteArray()).thenReturn(fakeData);

        when(s3Client.getObjectAsBytes(any(GetObjectRequest.class))).thenReturn(responseBytes);

        byte[] result = s3Service.downloadFile(key);

        assertArrayEquals(fakeData, result);
    }

    @Test
    void testDeleteFile() {
        String key = "uploads/delete-me.txt";

        s3Service.deleteFile(key);

        verify(s3Client, times(1)).deleteObject(any(DeleteObjectRequest.class));
    }

    @Test
    void testListFiles() {
        S3Object s3Object = S3Object.builder().key("uploads/file1.txt").build();
        ListObjectsV2Response response = ListObjectsV2Response.builder()
                .contents(Collections.singletonList(s3Object))
                .build();

        when(s3Client.listObjectsV2(any(ListObjectsV2Request.class))).thenReturn(response);

        List<String> keys = s3Service.listFiles("uploads");

        assertEquals(1, keys.size());
        assertEquals("uploads/file1.txt", keys.get(0));
    }
}

