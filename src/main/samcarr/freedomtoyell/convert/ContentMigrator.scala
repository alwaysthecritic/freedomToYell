package samcarr.freedomtoyell.convert

import java.net.URI

object ContentMigrator {
    def migrate(content: String, uriMap: Set[MigratedUri]): String = {
        var migratedContent = content
        uriMap foreach { case MigratedUri(originalUri, _, finalUri) =>
            migratedContent = migratedContent.replaceAllLiterally(originalUri.toString, finalUri.toString)
        }
        migratedContent
    }
}