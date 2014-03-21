package samcarr.freedomtoyell.image

import java.net.URI
import java.awt.Image
import javax.imageio.ImageIO
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import org.apache.commons.io.IOUtils

object ImageImporter {
    
    def importImage(importUri: URI, finalUri: URI, baseDir: File) = {
        val conn = importUri.toURL.openConnection()
        val in = conn.getInputStream();
        val out = new FileOutputStream(targetFile(finalUri, baseDir))
        IOUtils.copy(in, out)
        // qq Any exception causes whole app to exit anyway, but really we should do this better.
        in.close()
        out.close()
    }
    
    private def targetFile(uri: URI, targetDir: File) = {
        val file = new File(targetDir, uri.getPath())
        file.getParentFile().mkdirs()
        file
    }
}