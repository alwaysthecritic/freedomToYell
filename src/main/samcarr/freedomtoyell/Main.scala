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
import java.net.URI
import samcarr.freedomtoyell.image.ImageImporter

object Main {
    case object BadArgumentsException extends Exception
    
    val OutputFileName = "Migrated.txt"
    val Utf8 = "UTF8"
    
    def main(args: Array[String]) {
        load(args) flatMap { case (config, content) =>
            Try {
                val uriMap = Analyser.analyse(content)(config)
                val dir = createOutputDir(config)
                val migratedContent = ContentMigrator.migrate(content, uriMap)
                writeMigratedFile(migratedContent, dir)
                importImages(uriMap, dir)
            }
        } recover {
            case e: URISyntaxException => println(s"Malformed URI found: fix and re-run - ${e.getMessage()}")
            case BadArgumentsException => println(Usage.Message)
            case e: Exception => println(s"Failed: $e")
        }
    }
    
    private def load(args: Array[String]) = {
        for {
            config <- parseArgs(args)
            content <- readInput(config.inputFilename)
        } yield (config, content)
    }
    
    private def parseArgs(args: Array[String]): Try[Config] = {
        if (args.length < 5) {
            Failure(BadArgumentsException)
        } else {
            Success(Config(args(0), args(1), args(2), args(3), args(4)))
        }
    }
    
    private def readInput(filename: String): Try[String] = {
        Try {
            // Reading the whole file and only closing when there are no errors is not
            // great but it's quite sufficient for this one-shot command-line app.
            val source = Source.fromFile(new File(filename), Utf8)
            val contents = source.getLines().mkString("\n")
            source.close()
            contents
        }
    }
    
    private def createOutputDir(config: Config): File = {
        val dir = new File(config.outputDirName)
        dir.mkdirs()
        dir
    }
    
    private def writeMigratedFile(converted: String, dir: File): Try[Unit] = {
        Try {
            val outputFile = new File(dir, OutputFileName)
            val writer = new PrintWriter(outputFile, Utf8)
            writer.write(converted)
            writer.close()
        }
    }
    
    private def importImages(migratedUris: Set[MigratedUri], baseDir: File) = {
        // Poor man's parallelism by using .par, which will probably only match
        // number of cores, but is a quick win and perfectly safe here.
        migratedUris.par foreach {
            case MigratedUri(_, Some(importUri), finalUri) => {
                println(s"Importing image from $importUri...")
                ImageImporter.importImage(importUri, finalUri, baseDir)
            }
            case MigratedUri(_, None, _) => ()
        }
    }
}
