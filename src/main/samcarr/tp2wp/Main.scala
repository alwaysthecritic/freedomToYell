package samcarr.tp2wp;

import java.io.File

import scala.io.Source
import scala.util.Failure
import scala.util.Success
import scala.util.Try

import IoUtils.Utf8

object Main {
    
    implicit val FileEncoding = Utf8
    
    def main(args: Array[String]) {
        migrate(args) match {
            case Success(_) => println("Completed")
            case Failure(e) => println(e.getMessage())
        }
    }
    
    private def migrate(args: Array[String]) = {
        for {
            config <- parseArgs(args)
            contents <- readInput(config)
            converted <- convertImageUrls(contents)
            result <- writeOutput(converted, config)
        } yield result
    }
    
    private def parseArgs(args: Array[String]): Try[Config] = {
        if (args.length < 2) {
            Failure(new IllegalArgumentException("Usage: tp2wp <typepad_export_file> <output_dir>"))
        } else {
            Success(Config(args(0), args(1)))
        }
    }
    
    private def readInput(config: Config): Try[String] = {
        Try {
            // Reading the whole file and only closing when there are no errors is not great
            // but it's quite sufficient for this one-shot command-line app.
            val source = Source.fromFile(new File(config.inputFileName), FileEncoding.name)
            val contents = source.getLines().mkString("\n")
            source.close()
            contents
        }
    }
    
    private def convertImageUrls(contents: String): Try[String] = {
        Success("")
    }
    
    private def writeOutput(converted: String, config: Config): Try[Unit] = {
        Try {
            val dir = new File(config.outputDirName)
            dir.mkdirs()
        }
    }
}
