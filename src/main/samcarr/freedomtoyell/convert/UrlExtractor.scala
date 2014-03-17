package samcarr.freedomtoyell.convert

import samcarr.freedomtoyell.Config

class UrlExtractor(implicit config: Config) {
    // All images - capturing the src value.
    val ImgRegex = """(?i)<img[^>]*?src\s*=\s*"([^>]*?)"""".r
    
    // All links where href ends with wi or pi.
    // - wi: an image shrunk to a specific size - e.g. "foo-800wi"
    // - pi: actually serves HTML for image popup, but can be truncated to get image itself.
    val ARegex = """(?i)<a[^>]*?href\s?=\s?"([^>]*?[wi|pi])"""".r
    
    def extract(content: String): Iterator[String] = {
        // qq This surely runs the regex twice for each match - must be a way to get the actual
        //    matches directly and extract the groups.
        val srcs = for (ImgRegex(src) <- ImgRegex findAllIn content) yield src
        val hrefs = for (ARegex(href) <- ARegex findAllIn content) yield href
        onlyOriginalHost(srcs ++ hrefs)
    }
    
    private def onlyOriginalHost(urls: Iterator[String]) = {
        urls filter (_.toLowerCase().contains(config.oldHost))
    }
}