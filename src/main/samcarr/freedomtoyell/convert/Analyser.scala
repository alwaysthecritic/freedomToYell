package samcarr.freedomtoyell.convert

import samcarr.freedomtoyell.Config
import java.net.URI

case class ImageUris(importUri: URI, finalUri: URI)

object Analyser {
    def analyse(content: String)(implicit config: Config): Map[URI, ImageUris] = {
        val uris = UriExtractor.extract(content)(config)
        // Duplicate URI keys will be coalesced (last one wins) due to the nature of a Map.
        uris map (uri => uri -> ImageUriMapper.mapUri(uri)) toMap
    }
}