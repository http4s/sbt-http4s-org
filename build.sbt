// Projects
lazy val `sbt-http4s-org` = project.in(file("."))
  .disablePlugins(MimaPlugin)
  .enablePlugins(NoPublishPlugin)
  .aggregate(core)

lazy val core = project.in(file("core"))
  .settings(commonSettings)
  .settings(
    name := "sbt-http4s-org"
  )

// General Settings
lazy val commonSettings = Seq(
  scalaVersion := "2.12.11",
  crossScalaVersions := Seq(scalaVersion.value),

  libraryDependencies ++= Seq(
  )
)

// General Settings
inThisBuild(List(
  organization := "org.http4s",
  developers := List(
    Developer("rossabaker", "Ross A. Baker", "ross@rossabaker.com", url("https://github.com/rossabaker"))
  ),

  homepage := Some(url("https://github.com/rossabaker/sbt-http4s-org")),

  licenses := Seq("Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0.html")),

  pomIncludeRepository := { _ => false},
  scalacOptions in (Compile, doc) ++= Seq(
      "-groups",
      "-sourcepath", (baseDirectory in LocalRootProject).value.getAbsolutePath,
      "-doc-source-url", "https://github.com/rossabaker/sbt-http4s-org/blob/v" + version.value + "€{FILE_PATH}.scala"
  )
))
