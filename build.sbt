// The simplest possible sbt build file is just one line:

scalaVersion := "2.12.6"
// That is, to create a valid sbt build, all you've got to do is define the
// version of Scala you'd like your project to use.

// ============================================================================

// Lines like the above defining `scalaVersion` are called "settings" Settings
// are key/value pairs. In the case of `scalaVersion`, the key is "scalaVersion"
// and the value is "2.12.6"

// It's possible to define many kinds of settings, such as:

name := "cermine-service"
organization := "com.github.cermine"
version := "1.0"


// Note, it's not required for you to define these three settings. These are
// mostly only necessary if you intend to publish your library's binaries on a
// place like Sonatype or Bintray.


// Want to use a published library in your project?
// You can define other libraries as dependencies in your build like this:
val circeVersion = "0.9.3"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "1.0.1",
  "com.github.finagle" %% "finch-core" % "0.22.0",
  "com.github.finagle" %% "finch-circe" % "0.22.0",
  "pl.edu.icm.cermine" % "cermine-impl" % "1.13",
) ++ Seq(

  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
).map(_ % circeVersion)


// resolvers ++= Seq(
//   "maven.ceon.pl-releases" at "https://maven.ceon.pl/artifactory/kdd-releases",
//   "CEON local" at "https://maven.ceon.pl/artifactory/ext-releases-local"
// )

enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)
enablePlugins(AshScriptPlugin)

dockerBaseImage := "openjdk:jre-alpine"
dockerUpdateLatest := true
mainClass in Compile := Some("cermine.service.Main")
