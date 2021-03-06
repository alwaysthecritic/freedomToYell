package samcarr.freedomtoyell.convert

import org.scalatest._
import org.scalatest.matchers.ShouldMatchers
import samcarr.freedomtoyell._
import java.net.URI

class ImageUriMapperSpec extends UnitSpec {
    implicit val config = Config("", "old.com", "new.net", "", "")
    val nr = ImageUriMapper.NewRoot
    
    "ImageUriMapper" should "remove -pi suffix for import URI" in {
        checkMappedUri("http://old.com/123-pi",
                expectedImportUri = "http://old.com/123",
                expectedFinalUri = s"http://new.net/$nr/123")
    }
    
    it should "remove -popup suffix for import URI" in {
        checkMappedUri("http://old.com/123-popup",
                expectedImportUri = "http://old.com/123",
                expectedFinalUri = s"http://new.net/$nr/123")
    }
    
    it should "use new host, root and hyphens" in {
        checkMappedUri("http://old.com/12/34/foo-350wi",
                expectedImportUri = "http://old.com/12/34/foo-350wi",
                expectedFinalUri = s"http://new.net/$nr/12-34-foo-350wi")
    }
    
    it should "remove .a/ at start of path" in {
        checkMappedUri("http://old.com/.a/123",
                expectedImportUri = "http://old.com/.a/123",
                expectedFinalUri = s"http://new.net/$nr/123")
    }
    
    it should "remove .shared-image.html?/" in {
        checkMappedUri("http://old.com/.shared/image.html?/photos/misc/foo.jpg",
                expectedImportUri = "http://old.com/photos/misc/foo.jpg",
                expectedFinalUri = s"http://new.net/$nr/photos-misc-foo.jpg")
    }
    
    private def checkMappedUri(input: String, expectedImportUri: String, expectedFinalUri: String) = {
        val (importUri, finalUri) = ImageUriMapper.mapUri(new URI(input))
        importUri.toString should be (expectedImportUri)
        finalUri.toString should be (expectedFinalUri)
    }
}