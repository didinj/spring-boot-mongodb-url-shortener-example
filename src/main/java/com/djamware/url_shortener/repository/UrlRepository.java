package com.djamware.url_shortener.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.djamware.url_shortener.model.UrlMapping;

public interface UrlRepository extends MongoRepository<UrlMapping, String> {

    Optional<UrlMapping> findByShortCode(String shortCode);

    Optional<UrlMapping> findByOriginalUrl(String originalUrl);
}
