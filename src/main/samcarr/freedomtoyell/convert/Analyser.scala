package samcarr.freedomtoyell.convert

import samcarr.freedomtoyell.Config
import java.net.URI
import scala.language.postfixOps

sealed abstract class UriForMigration(val uri: URI)
case class ArticleUri(_uri: URI) extends UriForMigration(_uri)
case class ImageUri(_uri: URI) extends UriForMigration(_uri)

case class MigratedUri(originalUri: URI, importUri: Option[URI], finalUri: URI)

object Analyser {
    def analyse(content: String)(implicit config: Config): Set[MigratedUri] = {
        val uriExtractor = new UriExtractor(config)
        val urisForMigration = uriExtractor.extract(content)
        
        // Duplicates will be coalesced since we're using a Set.
        urisForMigration map {
            case ArticleUri(uri) => MigratedUri(uri, None, ArticleUriMapper.mapUri(uri))
            case ImageUri(uri) => {
                val (importUri, finalUri) = ImageUriMapper.mapUri(uri)
                MigratedUri(uri, Some(importUri), finalUri)
            }
        } toSet
    }
}