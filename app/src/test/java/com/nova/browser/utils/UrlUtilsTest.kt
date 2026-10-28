package com.nova.browser.utils

import com.nova.browser.search.SearchCategory
import com.nova.browser.search.SearchEngineRegistry
import org.junit.Assert.assertEquals
import org.junit.Test

class UrlUtilsTest {

    @Test
    fun parseInput_httpUrl_returnsSameUrl() {
        val input = "http://example.com"
        val result = UrlUtils.parseInput(input)
        assertEquals("http://example.com", result)
    }

    @Test
    fun parseInput_httpsUrl_returnsSameUrl() {
        val input = "https://a.b.c/test?q=1"
        val result = UrlUtils.parseInput(input)
        assertEquals("https://a.b.c/test?q=1", result)
    }

    @Test
    fun parseInput_domainWithoutScheme_addsHttps() {
        val input = "example.com"
        val result = UrlUtils.parseInput(input)
        assertEquals("https://example.com", result)
    }

    @Test
    fun parseInput_searchQuery_usesDefaultGoogleSearch() {
        val input = "anime browser"
        val result = UrlUtils.parseInput(input)
        assertEquals("https://www.google.com/search?q=anime+browser", result)
    }

    @Test
    fun parseInput_customSearchEngine_usesCustomEngine() {
        val input = "kotlin"
        val result = UrlUtils.parseInput(input, SearchEngineRegistry.DUCKDUCKGO)
        assertEquals("https://duckduckgo.com/?q=kotlin", result)
    }

    @Test
    fun searchEngines_categoriesAndDefaults() {
        assertEquals(SearchEngineRegistry.GOOGLE, SearchEngineRegistry.DEFAULT)
        assertEquals(5, SearchEngineRegistry.getGeneralEngines().size)
        assertEquals(4, SearchEngineRegistry.getSpecializedEngines().size)
        assertEquals(SearchCategory.SPECIALIZED, SearchEngineRegistry.GITHUB.category)
    }
}
