package samcarr.freedomtoyell.convert

import java.net.URI

object ContentMigrator {
    def migrate(content: String, uriMap: Map[URI, ImageUris]): String = {
        var migratedContent = content
        uriMap foreach { case (sourceUri, ImageUris(_, finalUri)) =>
            migratedContent = migratedContent.replaceAllLiterally(sourceUri.toString, finalUri.toString)
        }
        migratedContent
    }
}