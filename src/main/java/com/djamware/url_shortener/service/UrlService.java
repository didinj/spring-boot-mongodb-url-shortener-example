package com.djamware.url_shortener.service;

import java.util.Random;

import org.springframework.stereotype.Service;

import com.djamware.url_shortener.model.UrlMapping;
import com.djamware.url_shortener.repository.UrlRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository repository;

    private static final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public String generateShortCode(int length) {
        Random random = new Random();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < length; i++) {
            code.append(BASE62.charAt(random.nextInt(BASE62.length())));
        }

        return code.toString();
    }

    public String shortenUrl(String originalUrl) {
        return repository.findByOriginalUrl(originalUrl)
                .map(UrlMapping::getShortCode)
                .orElseGet(() -> {
                    String code = generateShortCode(6);

                    UrlMapping mapping = UrlMapping.builder()
                            .originalUrl(originalUrl)
                            .shortCode(code)
                            .clicks(0L)
                            .createdAt(java.time.LocalDateTime.now())
                            .build();

                    repository.save(mapping);
                    return code;
                });
    }

    public String getOriginalUrl(String shortCode) {
        UrlMapping mapping = repository.findByShortCode(shortCode)
                .orElseThrow(() -> new RuntimeException("Short URL not found"));

        mapping.setClicks(mapping.getClicks() + 1);
        repository.save(mapping);

        return mapping.getOriginalUrl();
    }
}
