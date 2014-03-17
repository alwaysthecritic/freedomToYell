package samcarr.freedomtoyell

import org.scalatest._
import org.scalatest.matchers.ShouldMatchers

class ExportConverterSpec extends FlatSpec with ShouldMatchers {
    val BlankConfig = Config("", "", "", "")
    
    "Migrator" should "translate img URLs" in {
        val config = BlankConfig.copy(oldHost = "www.old.com", newHost = "new.com")
        val exportConverter = new ExportConverter(config)
        
        val input = "<img class=\"image\" src=\"http://www.old.com\" alt=\"foo\">Bar</img>"
        val output = exportConverter.convert(input)
        output.text should be ("<img class=\"image\" src=\"http://new.com\" alt=\"foo\">Bar</img>")
    }
}