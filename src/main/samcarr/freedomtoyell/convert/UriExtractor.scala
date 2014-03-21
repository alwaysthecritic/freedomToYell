package samcarr.freedomtoyell.convert

import samcarr.freedomtoyell.Config
import java.net.URI

object UriExtractor {
    // All images - capturing the src value.
    val ImgRegex = """(?i)<img[^>]*?src\s*=\s*"([^>]*?)"""".r
    
    // Finding image URLs is a little involved as many don't have a handy .jpg extension.
    // We find all links where href ends with:
    // a standard image extension like .jpg, .gif etc.
    // -XXXwi: an image shrunk to a specific size - e.g. "foo-800wi"
    // -pi: an image for use in a popup (perhaps pi = popup image?) but seems same as raw image
    // -popup: actually serves HTML for image popup.
    // In all cases if the suffix is removed, the URL serves the original full size image.
    val ARegex = """(?i)<a[^>]*?href\s*=\s*"([^>]*?(?:-\d+wi|-pi|-popup|\.jpg|\.jpeg|\.gif|\.png))"""".r
    
    /** May contain duplicates */
    def extract(content: String)(implicit config: Config): Iterator[URI] = {
        // qq This surely runs the regex twice for each match - must be a way to get the actual
        //    matches directly and extract the groups.
        val srcs = for (ImgRegex(src) <- ImgRegex findAllIn content) yield src
        val hrefs = for (ARegex(href) <- ARegex findAllIn content) yield href
        onlyOriginalHost(srcs ++ hrefs) map (new URI(_))
    }
    
    private def onlyOriginalHost(urls: Iterator[String])(implicit config: Config) = {
        urls filter (_.toLowerCase().contains(config.oldHost))
    }
}