package samcarr.freedomtoyell.convert

import org.scalatest._
import org.scalatest.matchers.ShouldMatchers
import samcarr.freedomtoyell._
import java.net.URI

class ImageUriMapperSpec extends UnitSpec {
    implicit val config = Config("", "old.com", "new.net", "")
    
    "ImageUriMapper.fixedImportUri" should "remove -pi suffix" in {
        checkFixedUri("http://old.com/123-pi", s"http://old.com/123")
    }
    
    it should "map -popup URL to same but with host changed and without -popup suffix" in {
        checkFixedUri("http://old.com/123-popup", s"http://old.com/123")
    }
    
    "ImageUriMapper.migratedUri" should "use new host, root and hyphens" in {
        checkMigratedUri("http://old.com/12/34/foo-350wi", s"http://new.net/${ImageUriMapper.NewRoot}/12-34-foo-350wi")
    }
    
    it should "remove .a/ at start of path" in {
        checkMigratedUri("http://old.com/.a/123", s"http://new.net/${ImageUriMapper.NewRoot}/123")
    }

    private def checkFixedUri(input: String, expected: String) = {
        ImageUriMapper.fixedImportUri(new URI(input)).toString() should be (expected)
    }
    
    private def checkMigratedUri(input: String, expected: String) = {
        ImageUriMapper.migratedUri(new URI(input)).toString() should be (expected)
    }
}