package com.jules.urlshortener.app;

import com.jules.urlshortener.url.ShortenedUrlService;
import com.jules.urlshortener.util.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RestController
public class ShortenerController {

    private final ShortenedUrlService service;

    @GetMapping("{shortCode}")
    public ResponseEntity<Void> getOriginalLink(@PathVariable String shortCode) {
        return switch (service.getOriginalUrl(shortCode)) {
            case Result.Ok(String originalUrl) -> ResponseEntity
                    .status(HttpStatus.FOUND)
                    .location(URI.create(originalUrl))
                    .build();
            case Result.Err(Throwable e) -> ResponseEntity.notFound().build();
        };
    }

    @PostMapping("shorten")
    public ResponseEntity<String> shortenUrl(@RequestBody String url) {
        return switch (service.shortenUrl(url)) {
            case Result.Ok(String shortenedUrl) -> ResponseEntity.ok(shortenedUrl);
            case Result.Err(Throwable e) -> ResponseEntity.internalServerError().body(e.getMessage());
        };
    }
}
