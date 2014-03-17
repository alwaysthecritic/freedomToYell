package samcarr.freedomtoyell.convert

import org.scalatest._
import org.scalatest.matchers.ShouldMatchers
import samcarr.freedomtoyell._

class UrlMapperSpec extends UnitSpec {
    implicit val config = Config("", "www.old.com", "new.com", "")
    val mapper = new UrlMapper()

    "UrlMapper" should "map -XXXwi URL to same but with host changed" in {
        check("http://www.old.com/123-350wi", "http://new.com/123-350wi")
    }
    
    it should "map -pi URL to same but with host changed and without -pi suffix" in {
        check("http://www.old.com/123-pi", "http://new.com/123")
    }
    
    it should "map -popup URL to same but with host changed and without -popup suffix" in {
        check("http://www.old.com/123-popup", "http://new.com/123")
    }

    private def check(input: String, expected: String) = {
        mapper.mapUrl(input) should be (expected)
    }
}