package com.nova.browser

import com.nova.browser.model.SearchCategory
import com.nova.browser.model.SearchEngineRegistry
import com.nova.browser.util.UrlParser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchAndUrlTest {

    @Test
    fun testDefaultSearchEngineIsGoogle() {
        val defaultEngine = SearchEngineRegistry.defaultProvider
        assertEquals("google", defaultEngine.id)
        assertEquals("Google", defaultEngine.name)
        assertEquals(SearchCategory.GENERAL, defaultEngine.category)
    }

    @Test
    fun testAllRequiredSearchProvidersExist() {
        val ids = SearchEngineRegistry.providers.map { it.id }
        val expected = listOf("google", "bing", "duckduckgo", "brave", "yahoo", "wikipedia", "youtube", "github", "google_scholar")
        for (id in expected) {
            assertTrue("Provider $id missing", ids.contains(id))
        }
    }

    @Test
    fun testUrlParsingDirectUrls() {
        val google = SearchEngineRegistry.defaultProvider
        assertEquals("https://example.com", UrlParser.parseInput("example.com", google))
        assertEquals("https://www.google.com", UrlParser.parseInput("https://www.google.com", google))
        assertEquals("http://localhost:8080/test", UrlParser.parseInput("http://localhost:8080/test", google))
    }

    @Test
    fun testUrlParsingSearchQueries() {
        val google = SearchEngineRegistry.defaultProvider
        val duckduckgo = SearchEngineRegistry.getProviderById("duckduckgo")

        val googleSearch = UrlParser.parseInput("anime wallpaper 4k", google)
        assertEquals("https://www.google.com/search?q=anime+wallpaper+4k", googleSearch)

        val ddgSearch = UrlParser.parseInput("nova browser", duckduckgo)
        assertEquals("https://duckduckgo.com/?q=nova+browser", ddgSearch)
    }

    @Test
    fun testDomainExtraction() {
        assertEquals("github.com", UrlParser.extractDomain("https://www.github.com/mozilla/geckoview"))
        assertEquals("en.wikipedia.org", UrlParser.extractDomain("https://en.wikipedia.org/wiki/Main_Page"))
    }
}
