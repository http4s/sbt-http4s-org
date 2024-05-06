enablePlugins(TypelevelCiReleasePlugin)

// Projects
lazy val `sbt-http4s-org` = project
  .in(file("."))
  .enablePlugins(NoPublishPlugin)
  .aggregate(core)

lazy val core = project
  .in(file("core"))
  .enablePlugins(SbtPlugin)
  .settings(
    name := "sbt-http4s-org",
    unusedCompileDependenciesFilter -= moduleFilter("org.typelevel", "sbt-typelevel"),
    unusedCompileDependenciesFilter -= moduleFilter("org.typelevel", "sbt-typelevel-scalafix"),
    unusedCompileDependenciesFilter -= moduleFilter("org.typelevel", "sbt-typelevel-site")
  )

lazy val docs = project
  .in(file("site"))
  .enablePlugins(Http4sOrgSitePlugin)

ThisBuild / tlBaseVersion := "0.17"
ThisBuild / crossScalaVersions := Seq("2.12.19")
ThisBuild / developers := List(
  Developer(
    "rossabaker",
    "Ross A. Baker",
    "ross@rossabaker.com",
    url("https://github.com/rossabaker"))
)
ThisBuild / startYear := Some(2020)
