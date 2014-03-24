package samcarr.freedomtoyell.convert

import samcarr.freedomtoyell.Config
import java.net.URI

/** Given a URI, ostensibly for a Typepad image, returns a new pair of URIs:
  * - the original stripped of any unnecessary bits, that we can use to import the image
  * - a new URI that we should use to access the imported image.
  *
  * We manipulate the original URI to get a good URI from which to import the image:
  * - those that start .shared/image.html?/foo.jpg actually serve HTML that contains an
  *   img using the src in the query string, so we can just strip that prefix to get a
  *   URI for the image direct.
  * - similarly, those that end -popup return HTML for a popup window, containing the image,
  *   but by stripping that suffix the URI just gives us the image directly.
  * - those that end -pi are images intended for use as a popup (perhaps pi = popup image)
  *   but that suffix can be removed without affecting the result.
  * 
  * The new URI we generate from the import URI aims to be simpler, nicer and flatter:
  * - Typepad image paths often (but not always, depending on vintage perhaps) begin
  *   with /.a/ which we remove because it's just plain unpleasant.
  * - We convert / to -, hence all the images end up in a single flat directory
  * - We prepend a new root dir, in which all imported images will end up.
  */
object ImageUriMapper {
    val NewRoot = "wp-content/tp"
    
    /** Returns a pair of URIs:
      * - the URI from which to import the original image (which isn't nec. the same as input URI)
      * - the new URI to use for the image.
      */
    def mapUri(uri: URI)(implicit config: Config): (URI, URI) = {
        val importUri = uriForImport(uri)
        (importUri, uriForMigratedContent(importUri))
    }
    
    def uriForImport(uri: URI): URI = {
        new URI(uri.toString().replace("/.shared/image.html?", "").replaceFirst("-(pi|popup)$", ""))
    }
    
    def uriForMigratedContent(uri: URI)(implicit config: Config): URI = {
        val path = uri.normalize().getPath();
        val pathPrefixStripped = path.replaceFirst("^/.a/", "/")
        val pathInvisiDotsHyphenated = pathPrefixStripped.replaceAll("(?<=/)\\.", "-")
        // Replace all but the initial slash.
        val pathHyphenated = pathInvisiDotsHyphenated.replaceAll("(?<!^)/", "-")
        
        val convertedPath = "/" + NewRoot + pathHyphenated
                
        new URI(uri.getScheme(), uri.getUserInfo(), config.newHost,
                uri.getPort(), convertedPath, null, null)
    }
}