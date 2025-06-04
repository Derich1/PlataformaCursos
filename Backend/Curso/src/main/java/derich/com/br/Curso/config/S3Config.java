package derich.com.br.Curso.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${aws.accessKeyId:#{null}}")
    private String accessKeyId;

    @Value("${aws.secretKey:#{null}}")
    private String secretKey;

    @Value("${aws.profileName:default}")
    private String profileName;

    @Bean
    public S3Client s3Client() {
        Region region = Region.of(awsRegion);

        S3Client client;

        // Se accessKeyId e secretKey estiverem preenchidos no properties, usa StaticCredentials
        if (accessKeyId != null && secretKey != null) {
            AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKeyId, secretKey);
            client = S3Client.builder()
                    .region(region)
                    .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                    .build();
        } else {
            // Senão, usa as credenciais do Perfil AWS CLI (~/.aws/credentials)
            client = S3Client.builder()
                    .region(region)
                    .credentialsProvider(ProfileCredentialsProvider.create(profileName))
                    .build();
        }

        return client;
    }
}
