package com.nova.browser.utils

import com.nova.browser.search.SearchEngine
import com.nova.browser.search.SearchEngineRegistry
import java.util.regex.Pattern

object UrlUtils {
    private val WEB_URL_PATTERN = Pattern.compile(
        "^https?://.*",
        Pattern.CASE_INSENSITIVE
    )

    private val DOMAIN_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)+.*"
    )

    fun parseInput(input: String, searchEngine: SearchEngine = SearchEngineRegistry.DEFAULT): String {
        val trimmed = input.trim()
        if (trimmed.isEmpty()) {
            return ""
        }

        // Check if it already has scheme
        if (WEB_URL_PATTERN.matcher(trimmed).matches()) {
            return trimmed
        }

        // Check if it's internal page
        if (trimmed.startsWith("about:") || trimmed.startsWith("chrome://")) {
            return trimmed
        }

        // Check if it looks like a domain name e.g., "example.com", "sub.domain.org/path"
        if (!trimmed.contains(" ") && DOMAIN_PATTERN.matcher(trimmed).matches()) {
            return "https://$trimmed"
        }

        // Localhost special case
        if (!trimmed.contains(" ") && (trimmed.startsWith("localhost") || trimmed.startsWith("127.0.0.1"))) {
            return "http://$trimmed"
        }

        // Fallback to search query using current search engine
        return searchEngine.buildSearchUrl(trimmed)
    }

    fun extractDisplayDomain(url: String): String {
        return try {
            val uri = java.net.URI(url)
            val host = uri.host
            if (host.isNullOrEmpty()) url else host.removePrefix("www.")
        } catch (e: Exception) {
            url
        }
    }
}
