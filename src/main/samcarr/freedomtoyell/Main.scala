package samcarr.freedomtoyell;

import java.io.File
import java.io.PrintWriter
import scala.util.Failure
import scala.util.Success
import scala.util.Try
import scala.io.Source
import samcarr.freedomtoyell.convert._
import java.net.MalformedURLException
import java.net.URISyntaxException

object Main {
    val OutputFileName = "Migrated.txt"
    val Usage = "Usage: tp2wp <typepad_export_file> <old_host> <new_host> <output_dir>"
    val Utf8 = "UTF8"
    
    def main(args: Array[String]) {
        load(args) map { case (config, content) =>
            Try {
                val uriMap = Analyser.analyse(content)(config)
                val dir = createOutputDir(config)
                val migratedContent = ContentMigrator.migrate(content, uriMap)
                writeMigratedFile(migratedContent, dir)
                
                // Fetch all images from source URI and save based on path of target URI
            }
        } recover {
            case e: URISyntaxException => println(s"Malformed URI found: fix and re-run - ${e.getMessage()}")
            case e: Exception => println(e.getMessage())
        }
    }
    
    private def load(args: Array[String]) = {
        for {
            config <- parseArgs(args)
            content <- readInput(config.inputFilename)
        } yield (config, content)
    }
    
    private def parseArgs(args: Array[String]): Try[Config] = {
        if (args.length < 4) {
            Failure(new IllegalArgumentException(Usage))
        } else {
            Success(Config(args(0), args(1), args(2), args(3)))
        }
    }
    
    private def readInput(filename: String): Try[String] = {
        Try {
            // Reading the whole file and only closing when there are no errors is not great
            // but it's quite sufficient for this one-shot command-line app.
            val source = Source.fromFile(new File(filename), Utf8)
            val contents = source.getLines().mkString("\n")
            source.close()
            contents
        }
    }
    
    private def writeMigratedFile(converted: String, dir: File): Try[Unit] = {
        Try {
            val outputFile = new File(dir, OutputFileName)
            val writer = new PrintWriter(outputFile, Utf8)
            writer.write(converted)
            writer.close()
        }
    }
  
    private def createOutputDir(config: Config): File = {
        val dir = new File(config.outputDirName)
        dir.mkdirs()
        dir
    }
}
