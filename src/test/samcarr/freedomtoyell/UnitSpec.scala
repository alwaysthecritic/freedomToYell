package samcarr.freedomtoyell

import org.scalatest.FlatSpec
import org.scalatest.ShouldMatchers
import java.net.URI

abstract class UnitSpec extends FlatSpec with ShouldMatchers

object UnitSpec {
    // Allow using strings instead of URIs.
    implicit def string2Uri(uri: String) = new URI(uri)
}