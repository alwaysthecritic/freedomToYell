package samcarr.freedomtoyell.convert

import samcarr.freedomtoyell._

class ExportConverter(implicit config: Config) {

    // qq Will want to break this down into smaller classes, but basic approach is:
    // - Find URLs for resources that we're going to pull locally.
    // - Build map of those original URLs to new local URLs
    // - Perform replacements in string
    // - Download those resources
    // - Report on what was downloaded and importantly, anything that couldn't be downloaded.
    def convert(content: String): ConversionResult = {
        val urls = UrlExtractor.extract(content)
        ConversionResult("")
    }
}