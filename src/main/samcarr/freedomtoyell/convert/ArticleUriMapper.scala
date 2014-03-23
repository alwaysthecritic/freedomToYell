package samcarr.freedomtoyell.convert

import samcarr.freedomtoyell.Config
import java.net.URI

object ArticleUriMapper {
    def mapUri(uri: URI)(implicit config: Config): URI = {
        val convertedPath = uri.getPath().replace(config.articleDir, "").replace(".html", "")
        new URI(uri.getScheme(), uri.getUserInfo(), config.newHost,
                uri.getPort(), convertedPath, uri.getQuery(), uri.getFragment())
    }
}