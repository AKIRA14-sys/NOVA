package com.nova.browser.search

enum class SearchCategory {
    GENERAL,
    SPECIALIZED
}

data class SearchEngine(
    val id: String,
    val name: String,
    val searchUrlTemplate: String,
    val category: SearchCategory
) {
    fun buildSearchUrl(query: String): String {
        val encodedQuery = java.net.URLEncoder.encode(query, "UTF-8")
        return searchUrlTemplate.replace("{query}", encodedQuery)
    }
}

object SearchEngineRegistry {
    val GOOGLE = SearchEngine(
        id = "google",
        name = "Google",
        searchUrlTemplate = "https://www.google.com/search?q={query}",
        category = SearchCategory.GENERAL
    )

    val BING = SearchEngine(
        id = "bing",
        name = "Bing",
        searchUrlTemplate = "https://www.bing.com/search?q={query}",
        category = SearchCategory.GENERAL
    )

    val YAHOO = SearchEngine(
        id = "yahoo",
        name = "Yahoo",
        searchUrlTemplate = "https://search.yahoo.com/search?p={query}",
        category = SearchCategory.GENERAL
    )

    val DUCKDUCKGO = SearchEngine(
        id = "duckduckgo",
        name = "DuckDuckGo",
        searchUrlTemplate = "https://duckduckgo.com/?q={query}",
        category = SearchCategory.GENERAL
    )

    val BRAVE = SearchEngine(
        id = "brave",
        name = "Brave Search",
        searchUrlTemplate = "https://search.brave.com/search?q={query}",
        category = SearchCategory.GENERAL
    )

    val WIKIPEDIA = SearchEngine(
        id = "wikipedia",
        name = "Wikipedia",
        searchUrlTemplate = "https://en.wikipedia.org/w/index.php?search={query}",
        category = SearchCategory.SPECIALIZED
    )

    val YOUTUBE = SearchEngine(
        id = "youtube",
        name = "YouTube",
        searchUrlTemplate = "https://www.youtube.com/results?search_query={query}",
        category = SearchCategory.SPECIALIZED
    )

    val GITHUB = SearchEngine(
        id = "github",
        name = "GitHub",
        searchUrlTemplate = "https://github.com/search?q={query}",
        category = SearchCategory.SPECIALIZED
    )

    val GOOGLE_SCHOLAR = SearchEngine(
        id = "google_scholar",
        name = "Google Scholar",
        searchUrlTemplate = "https://scholar.google.com/scholar?q={query}",
        category = SearchCategory.SPECIALIZED
    )

    val DEFAULT = GOOGLE

    val ALL_ENGINES = listOf(
        GOOGLE,
        BING,
        YAHOO,
        DUCKDUCKGO,
        BRAVE,
        WIKIPEDIA,
        YOUTUBE,
        GITHUB,
        GOOGLE_SCHOLAR
    )

    fun getGeneralEngines(): List<SearchEngine> = ALL_ENGINES.filter { it.category == SearchCategory.GENERAL }
    fun getSpecializedEngines(): List<SearchEngine> = ALL_ENGINES.filter { it.category == SearchCategory.SPECIALIZED }
    fun getById(id: String): SearchEngine = ALL_ENGINES.find { it.id == id } ?: DEFAULT
}
