package samcarr.freedomtoyell.convert

import samcarr.freedomtoyell.Config
import java.net.URI

/** Given a URI, ostensibly for a Typepad image, returns a new pair of URIs:
  * - the original stripped of any unnecessary bits, that we can use to import the image
  * - a new URI that we should use to access the imported image.
  *
  * We manipulate the original URI to canonicalise it somewhat:
  * - those that end -pi are images intended for use as a popup (perhaps pi = popup image)
  *   but that suffix can be removed without affecting the result.
  * - those that end -popup actually return HTML for a popup window, containing the image,
  *   but by stripping that suffix the URI actually just gives us the image directly.
  * 
  * The new URI we generate aims to be simpler, nicer and flatter:
  * - Typepad image paths often (but not always, depending on vintage perhaps) begin
  *   with /.a/ which we remove because it's just plain unpleasant.
  * - We convert / to -, hence all the images end up in a single flat directory
  * - We convert any path component beginning . to - and flatten query string too, to deal
  *   with URIs like .shared-image.html?/photos/misc/2008/09/23/foo.jpg.
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
        new URI(uri.toString().replaceFirst("-(pi|popup)$", ""))
    }
    
    def uriForMigratedContent(uri: URI)(implicit config: Config): URI = {
        val path = uri.normalize().getPath()
        val pathPrefixStripped = path.replaceFirst("^/.a/", "/")
        val pathInvisiDotsHyphenated = pathPrefixStripped.replaceAll("(?<=/)\\.", "-")
        // Replace all but the initial slash.
        val pathHyphenated = pathInvisiDotsHyphenated.replaceAll("(?<!^)/", "-")
        
        val convertedPath = "/" + NewRoot + pathHyphenated
        
        // If there is a query string, flatten it and add to path.
        val query = uri.getQuery()
        val finalPath = if (query != null) {
            convertedPath + query.replace("/", "-")
        } else {
            convertedPath
        }
                
        new URI(uri.getScheme(), uri.getUserInfo(), config.newHost,
                uri.getPort(), finalPath, null, null)
    }
}