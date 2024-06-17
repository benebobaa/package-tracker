package com.beneboba.package_tracking.helper;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;

public class JsonReader {

    public static String read(String filePath) throws Exception {
        ClassPathResource resource = new ClassPathResource(filePath);
        return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
    }
}
