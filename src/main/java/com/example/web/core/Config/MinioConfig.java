package com.example.web.core.Config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program:
 * @description: minioConfig
 * @author: Xu·yan
 * @create: 2020-12-24 15:17
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {

    private String endpoint;
    private String accesskey;
    private String secrekey;

    /**
    * @description 获取minioClint
    * @author Xu·yan
    * @date 2020/12/24 3:23 下午
    */
    @Bean
    public MinioClient minioClient() {
        return new MinioClient(endpoint, accesskey, secrekey);
    }
}
