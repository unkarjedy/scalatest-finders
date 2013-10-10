name := "scalatest-finders"
 
organization := "org.scalatest"

version := "0.9.6"

scalaVersion := "2.10.0"

// disable using the Scala version in output paths and artifacts
crossPaths := false

// removes Scala dependency
autoScalaLibrary := false

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.0.RC1" % "test"

resolvers ++= Seq("releases" at "http://oss.sonatype.org/content/repositories/releases",
                  "snapshots" at "http://oss.sonatype.org/content/repositories/snapshots", 
                  "localmaven" at "file://"+Path.userHome+"/.m2/repository")

publishTo <<= version { v: String =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT")) Some("publish-snapshots" at nexus + "content/repositories/snapshots")
  else                             Some("publish-releases" at nexus + "service/local/staging/deploy/maven2")
}

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := (
  <url>http://www.scalatest.org</url>
  <licenses>
    <license>
      <name>the Apache License, ASL Version 2.0</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>https://github.com/scalatest/scalatest-finders</url>
    <connection>scm:git:git@github.com:scalatest/scalatest-finders.git</connection>
    <developerConnection>
      scm:git:git@github.com:scalatest/scalatest-finders.git
    </developerConnection>
  </scm>
  <developers>
    <developer>
      <id>bill</id>
      <name>Bill Venners</name>
      <email>bill@artima.com</email>
    </developer>
  </developers>
)

