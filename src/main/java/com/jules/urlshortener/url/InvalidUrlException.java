package com.jules.urlshortener.url;

public class InvalidUrlException extends RuntimeException {
    public InvalidUrlException(String url) {
        super("Invalid url: " + url);
    }
}
