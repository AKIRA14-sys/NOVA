package com.nova.browser.util

import com.nova.browser.model.SearchProvider

object UrlParser {
    private val URL_PATTERN = Regex(
        "^(https?://)?" +
                "([a-zA-Z0-9\\-]+\\.)+[a-zA-Z]{2,}" +
                "(:\\d+)?" +
                "(/[^\\s]*)?$"
    )

    fun parseInput(input: String, searchProvider: SearchProvider): String {
        val trimmed = input.trim()
        if (trimmed.isEmpty()) return ""

        if (trimmed.startsWith("http://") || trimmed.startsWith("https://") || trimmed.startsWith("file://") || trimmed.startsWith("about:")) {
            return trimmed
        }

        if (URL_PATTERN.matches(trimmed) && !trimmed.contains(" ")) {
            return "https://$trimmed"
        }

        return searchProvider.buildSearchUrl(trimmed)
    }

    fun extractDomain(url: String): String {
        val cleanUrl = url.removePrefix("https://").removePrefix("http://")
        val domainWithPort = cleanUrl.split("/")[0]
        val domain = domainWithPort.split(":")[0]
        return if (domain.startsWith("www.")) domain.substring(4) else domain
    }
}
