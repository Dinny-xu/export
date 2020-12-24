package com.example.web.core.Config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @program: test_pdf
 * @description: minio 实体
 * @author: Xu·yan
 * @create: 2020-12-24 15:15
 **/
@Data
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioProp {

    private String endpoint;

    private String accesskey;

    private String secrekey;

}
