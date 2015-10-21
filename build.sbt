name := "scalatest-finders-patched"
 
organization := "org.scalatest"

version := "0.9.8"

scalaVersion := "2.10.0"

scalacOptions in Global += "-target:jvm-1.6"

javacOptions in Global ++= Seq("-source", "1.6", "-target", "1.6")

publishArtifact in (Compile, packageDoc) := false

publishArtifact in packageDoc := false

sources in (Compile,doc) := Seq.empty

// disable using the Scala version in output paths and artifacts
crossPaths := false

// removes Scala dependency
autoScalaLibrary := false

licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.0.RC1" % "test"

resolvers ++= Seq("releases" at "http://oss.sonatype.org/content/repositories/releases",
                  "snapshots" at "http://oss.sonatype.org/content/repositories/snapshots", 
                  "localmaven" at "file://"+Path.userHome+"/.m2/repository")

publishArtifact in Test := false

publishMavenStyle := false

//resolvers += Resolver.bintrayRepo("jetbrains", "scalatest")

bintrayOrganization := Some("jetbrains")

bintrayRepository := "scalatest"
