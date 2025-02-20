package com.jules.urlshortener.url;

import com.jules.urlshortener.util.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ShortenedUrlService {

    private final UrlValidator urlValidator;
    private final ShortCodeGenerator shortCodeGenerator;
    private final ShortenedUrlRepository repo;

    @Transactional
    public Result<String, Throwable> getOriginalUrl(String shortUrl) {
        try {
            return this
                    .getUrl(shortUrl)
                    .map(ShortenedUrl::getOriginalUrl)
                    .map(Result::ok)
                    .orElseGet(() -> Result.err(new UrlNotFoundException(shortUrl)));
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return Result.err(e);
        }
    }

    @Transactional
    public Result<String, Throwable> shortenUrl(String originalUrl) {
        try {
            if (!this.urlValidator.isValidUrl(originalUrl)) {
                return Result.err(new InvalidUrlException(originalUrl));
            }

            var shortCode = shortCodeGenerator.generateShortCode(originalUrl);

            var shortenedUrl = new ShortenedUrl();
            shortenedUrl.setOriginalUrl(originalUrl);
            shortenedUrl.setAccessCount(0);
            shortenedUrl.setShortCode(shortCode);

            repo.save(shortenedUrl);
            return Result.ok(shortCode);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return Result.err(e);
        }
    }

    public Optional<ShortenedUrl> getUrl(String shortUrl) {
        var maybeUrl = this
                .repo
                .findByShortCode(shortUrl);

        maybeUrl.ifPresent(url -> url.setAccessCount(url.getAccessCount() + 1));

        return maybeUrl;
    }
}
