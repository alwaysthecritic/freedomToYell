package samcarr.freedomtoyell.convert

import samcarr.freedomtoyell.Config
import java.net.URI

class UriExtractor(config: Config) {
    
    // All article links - capturing the href value.
    val ARegex = s"""(?i)<a[^>]*?href\\s*=\\s*"([^"]*?${config.oldHost}[^"]*?${config.articleDir}[^"]*?)"""".r
    
    // All images - capturing the src value.
    val ImgRegex = s"""(?i)<img[^>]*?src\\s*=\\s*"([^"]*?${config.oldHost}[^"]*?)"""".r
    
    // Finding image URLs is a little involved as many don't have a handy .jpg extension.
    // We find all links where href ends with:
    // a standard image extension like .jpg, .gif etc.
    // -XXXwi: an image shrunk to a specific size - e.g. "foo-800wi"
    // -pi: an image for use in a popup (perhaps pi = popup image?) but seems same as raw image
    // -popup: actually serves HTML for image popup.
    // In all cases if the suffix is removed, the URL serves the original full size image.
    val AImgRegex = s"""(?i)<a[^>]*?href\\s*=\\s*"([^"]*?${config.oldHost}[^"]*?(?:-\\d+wi|-pi|-popup|\\.jpg|\\.jpeg|\\.gif|\\.png))"""".r
    
    /** May contain duplicates */
    def extract(content: String)(implicit config: Config): Iterator[UriForMigration] = {
        // qq This surely runs the regex twice for each match - must be a way to get the actual
        //    matches directly and extract the groups.
        val imgSrcs = for (ImgRegex(src) <- ImgRegex findAllIn content) yield src
        val imgHrefs = for (AImgRegex(href) <- AImgRegex findAllIn content) yield href
        val imgs = (imgSrcs ++ imgHrefs) map (uri => new ImageUri(new URI(uri)))
        
        val aHrefs = for (ARegex(href) <- ARegex findAllIn content) yield href
        val articles = (aHrefs) map (uri => new ArticleUri(new URI(uri)))
        
        imgs ++ articles
    }
}