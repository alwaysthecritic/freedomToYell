package samcarr.tp2wp;

import IoUtils._
import java.io.File

object Main {
    
    implicit val FileEncoding = Utf8
    
    def main(args: Array[String]) {
        parseArgs(args) { inputFilePath =>
             
        }
    }
    
    private def parseArgs(args: Array[String])(act:String => Unit) {
        if (args.length < 1) {
            println("Usage: tp2wp <typepad_export_file>")
        } else {
            act(args(0))
        }
    }
    
    private def readInput(filePath: String): Option[String] = {
        withSource(new File(filePath)) { source =>
            Some("")
        }
    }
    
}
