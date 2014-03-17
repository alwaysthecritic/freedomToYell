package samcarr.freedomtoyell;

import java.io.File
import java.io.PrintWriter
import scala.util.Failure
import scala.util.Success
import scala.util.Try
import scala.io.Source

object Main {
    val OutputFileName = "Migrated.txt"
    val Usage = "Usage: tp2wp <typepad_export_file> <old_host> <new_host> <output_dir>"
    val Utf8 = "UTF8"
    
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
        if (args.length < 4) {
            Failure(new IllegalArgumentException(Usage))
        } else {
            Success(Config(args(0), args(1), args(2), args(3)))
        }
    }
    
    private def readInput(config: Config): Try[String] = {
        Try {
            // Reading the whole file and only closing when there are no errors is not great
            // but it's quite sufficient for this one-shot command-line app.
            val source = Source.fromFile(new File(config.inputFileName), Utf8)
            val contents = source.getLines().mkString("\n")
            source.close()
            contents
        }
    }
    
    private def convertImageUrls(contents: String): Try[String] = {
        Success(contents)
    }
    
    private def writeOutput(converted: String, config: Config): Try[Unit] = {
        Try {
            val dir = new File(config.outputDirName)
            dir.mkdirs()
            
            val outputFile = new File(dir, OutputFileName)
            val writer = new PrintWriter(outputFile, Utf8)
            writer.write(converted)
            writer.close()
        }
    }
}
