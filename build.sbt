import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

val catsV = "2.0.0"
val catsEffectV = "2.0.0"
val shapelessV = "2.3.3"
val fs2V = "2.0.0"
val http4sV = "0.21.0-M6"
val circeV = "0.12.3"
val doobieV = "0.8.8"
val log4catsV = "1.0.1"
val specs2V = "4.8.1"

val kindProjectorV = "0.11.0"
val betterMonadicForV = "0.3.1"

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
  crossScalaVersions := Seq(scalaVersion.value, "2.12.10"),

  addCompilerPlugin("org.typelevel" %% "kind-projector" % kindProjectorV cross CrossVersion.full),
  addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % betterMonadicForV),

  libraryDependencies ++= Seq(
    "org.typelevel"               %% "cats-core"                  % catsV,
    "org.typelevel"               %% "alleycats-core"             % catsV,

    "org.typelevel"               %% "cats-effect"                % catsEffectV,

    "com.chuusai"                 %% "shapeless"                  % shapelessV,

    "co.fs2"                      %% "fs2-core"                   % fs2V,
    "co.fs2"                      %% "fs2-io"                     % fs2V,

    "org.http4s"                  %% "http4s-dsl"                 % http4sV,
    "org.http4s"                  %% "http4s-blaze-server"        % http4sV,
    "org.http4s"                  %% "http4s-blaze-client"        % http4sV,
    "org.http4s"                  %% "http4s-circe"               % http4sV,

    "io.circe"                    %% "circe-core"                 % circeV,
    "io.circe"                    %% "circe-generic"              % circeV,
    "io.circe"                    %% "circe-parser"               % circeV,

    "org.tpolecat"                %% "doobie-core"                % doobieV,
    "org.tpolecat"                %% "doobie-h2"                  % doobieV,
    "org.tpolecat"                %% "doobie-hikari"              % doobieV,
    "org.tpolecat"                %% "doobie-postgres"            % doobieV,
    "org.tpolecat"                %% "doobie-specs2"              % doobieV       % Test,

    "io.chrisdavenport"           %% "log4cats-core"              % log4catsV,
    "io.chrisdavenport"           %% "log4cats-slf4j"             % log4catsV,
    "io.chrisdavenport"           %% "log4cats-testing"           % log4catsV     % Test,

    "org.specs2"                  %% "specs2-core"                % specs2V       % Test,
    "org.specs2"                  %% "specs2-scalacheck"          % specs2V       % Test
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
