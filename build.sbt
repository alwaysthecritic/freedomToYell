name := "freedomToYell"

version := "1.0"

scalaVersion := "2.10.3"

// set the main source directory
unmanagedSourceDirectories in Compile <<= baseDirectory(base => List("src/main") map (base / _ ))

// set the test source directory
unmanagedSourceDirectories in Test <<= baseDirectory(base => List("src/test") map (base / _ ))

// set the main class for the main 'run' task
// change Compile to Test to set it for 'test:run'
mainClass in (Compile, run) := Some("samcarr.freedomtoyell.Main")

addCommandAlias("runSample", "run sample/input.txt alwaysthecritic.typepad.com justthesam.com sample/output")

// add a test dependency on ScalaTest
libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.1.0" % "test"
