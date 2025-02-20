package com.jules.urlshortener.url;

public class UrlNotFoundException extends RuntimeException {
    public UrlNotFoundException(String shortUrl) {
        super("Could not find url: " + shortUrl);
    }
}
