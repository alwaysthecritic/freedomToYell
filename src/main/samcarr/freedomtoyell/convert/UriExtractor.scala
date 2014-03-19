package samcarr.freedomtoyell.convert

import samcarr.freedomtoyell.Config

object UriExtractor {
    // All images - capturing the src value.
    val ImgRegex = """(?i)<img[^>]*?src\s*=\s*"([^>]*?)"""".r
    
    // All links where href ends with:
    // -XXXwi: an image shrunk to a specific size - e.g. "foo-800wi"
    // -pi: an image for use in a popup (perhaps pi = popup image?) but seems same as raw image
    // -popup: actually serves HTML for image popup.
    // In all cases if the suffix is removed, the URL serves the original full size image.
    val ARegex = """(?i)<a[^>]*?href\s*=\s*"([^>]*?(?:-\d+wi|-pi|-popup))"""".r
    
    def extract(content: String)(implicit config: Config): Iterator[String] = {
        // qq This surely runs the regex twice for each match - must be a way to get the actual
        //    matches directly and extract the groups.
        val srcs = for (ImgRegex(src) <- ImgRegex findAllIn content) yield src
        val hrefs = for (ARegex(href) <- ARegex findAllIn content) yield href
        onlyOriginalHost(srcs ++ hrefs)
    }
    
    private def onlyOriginalHost(urls: Iterator[String])(implicit config: Config) = {
        urls filter (_.toLowerCase().contains(config.oldHost))
    }
}