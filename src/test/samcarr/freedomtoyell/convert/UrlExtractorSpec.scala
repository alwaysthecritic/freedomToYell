package samcarr.freedomtoyell.convert

import org.scalatest._
import org.scalatest.matchers.ShouldMatchers
import samcarr.freedomtoyell._

class UrlExtractorSpec extends UnitSpec {
    implicit val config = Config("", "www.old.com", "new.com", "")
    val extractor = new UrlExtractor()
    
    "Migrator" should "find img URL in trivial img tag" in {
        val input = """<img src="http://www.old.com" />"""
        check(input, List("http://www.old.com"))
    }
    
    it should "find img URL in non-trivial img tag" in {
        // Notable features of the input:
        // - attr between img and src
        // - attr after src
        // - space around =
        // - non-lowercase tag and attribute names
        // - self-closing img tag with space before /
        val input = """<Img class = "image" SRC = "http://www.old.com" alt="foo" />"""
        check(input, List("http://www.old.com"))
    }
    
    it should "find img URL where the tag is not the whole input" in {
        // Notable features of the input:
        // - attr between img and src
        // - attr after src
        // - space around =
        // - non-lowercase tag and attribute names
        // - self-closing img tag with space before /
        val input = """<a href="foo"><img src="http://www.old.com" /></a>"""
        check(input, List("http://www.old.com"))
    }
    
    private def check(input: String, expected: List[String]) = {
        extractor.extract(input).toList should be (expected)
    }
}