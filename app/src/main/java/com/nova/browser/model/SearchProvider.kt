package com.nova.browser.model

enum class SearchCategory {
    GENERAL,
    SPECIALIZED
}

data class SearchProvider(
    val id: String,
    val name: String,
    val searchUrlTemplate: String,
    val homeUrl: String,
    val category: SearchCategory,
    val iconName: String
) {
    fun buildSearchUrl(query: String): String {
        val encodedQuery = java.net.URLEncoder.encode(query, "UTF-8")
        return searchUrlTemplate.replace("{query}", encodedQuery)
    }
}

object SearchEngineRegistry {
    val providers = listOf(
        // General Search Engines
        SearchProvider(
            id = "google",
            name = "Google",
            searchUrlTemplate = "https://www.google.com/search?q={query}",
            homeUrl = "https://www.google.com",
            category = SearchCategory.GENERAL,
            iconName = "google"
        ),
        SearchProvider(
            id = "bing",
            name = "Bing",
            searchUrlTemplate = "https://www.bing.com/search?q={query}",
            homeUrl = "https://www.bing.com",
            category = SearchCategory.GENERAL,
            iconName = "bing"
        ),
        SearchProvider(
            id = "duckduckgo",
            name = "DuckDuckGo",
            searchUrlTemplate = "https://duckduckgo.com/?q={query}",
            homeUrl = "https://duckduckgo.com",
            category = SearchCategory.GENERAL,
            iconName = "duckduckgo"
        ),
        SearchProvider(
            id = "brave",
            name = "Brave Search",
            searchUrlTemplate = "https://search.brave.com/search?q={query}",
            homeUrl = "https://search.brave.com",
            category = SearchCategory.GENERAL,
            iconName = "brave"
        ),
        SearchProvider(
            id = "yahoo",
            name = "Yahoo",
            searchUrlTemplate = "https://search.yahoo.com/search?p={query}",
            homeUrl = "https://search.yahoo.com",
            category = SearchCategory.GENERAL,
            iconName = "yahoo"
        ),

        // Specialized Search Destinations
        SearchProvider(
            id = "wikipedia",
            name = "Wikipedia",
            searchUrlTemplate = "https://en.wikipedia.org/w/index.php?search={query}",
            homeUrl = "https://en.wikipedia.org",
            category = SearchCategory.SPECIALIZED,
            iconName = "wikipedia"
        ),
        SearchProvider(
            id = "youtube",
            name = "YouTube",
            searchUrlTemplate = "https://www.youtube.com/results?search_query={query}",
            homeUrl = "https://www.youtube.com",
            category = SearchCategory.SPECIALIZED,
            iconName = "youtube"
        ),
        SearchProvider(
            id = "github",
            name = "GitHub",
            searchUrlTemplate = "https://github.com/search?q={query}",
            homeUrl = "https://github.com",
            category = SearchCategory.SPECIALIZED,
            iconName = "github"
        ),
        SearchProvider(
            id = "google_scholar",
            name = "Google Scholar",
            searchUrlTemplate = "https://scholar.google.com/scholar?q={query}",
            homeUrl = "https://scholar.google.com",
            category = SearchCategory.SPECIALIZED,
            iconName = "scholar"
        )
    )

    val defaultProvider: SearchProvider = providers.first { it.id == "google" }

    fun getProviderById(id: String): SearchProvider {
        return providers.find { it.id == id } ?: defaultProvider
    }
}
