package com.jules.urlshortener.url;

import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class UrlValidator {
    public boolean isValidUrl(String url) {
        try {
            URI uri = new URI(url.trim());

            if (uri.getScheme() == null || uri.getHost() == null) {
                return false;
            }

            uri.toURL();
            return true;
        } catch (URISyntaxException | IllegalArgumentException | MalformedURLException e) {
            return false;
        }
    }
}
