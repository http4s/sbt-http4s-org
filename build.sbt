// Projects
lazy val `sbt-http4s-org` = project
  .in(file("."))
  .disablePlugins(MimaPlugin)
  .settings(noPublishSettings)
  .aggregate(core)

lazy val core = project
  .in(file("core"))
  .enablePlugins(SbtPlugin)
  .settings(commonSettings)
  .settings(
    name := "sbt-http4s-org",
    addSbtPlugin("com.codecommit" % "sbt-spiewak" % "0.17.0"),
    addSbtPlugin("com.github.cb372" % "sbt-explicit-dependencies" % "0.2.15"),
    addSbtPlugin("com.lightbend.sbt" % "sbt-java-formatter" % "0.6.0"),
    addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.2"),
  )

// General Settings
lazy val commonSettings = Seq(
  crossScalaVersions := Seq("2.12.12"),
  scalaVersion := crossScalaVersions.value.head,
)

// General Settings
inThisBuild(
  List(
    organization := "org.http4s",
    organizationName := "http4s.org",
    baseVersion := "0.5",
    publishGithubUser in ThisBuild := "rossabaker",
    publishFullName in ThisBuild := "Ross A. Baker",
    homepage := Some(url("https://github.com/http4s/sbt-http4s-org")),
    licenses := Seq("Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0.html")),
    pomIncludeRepository := { _ =>
      false
    },
    scalacOptions in (Compile, doc) ++= Seq(
      "-groups",
      "-sourcepath",
      (baseDirectory in LocalRootProject).value.getAbsolutePath,
      "-doc-source-url",
      "https://github.com/http4s/sbt-http4s-org/blob/v" + version.value + "€{FILE_PATH}.scala"
    ),
    githubWorkflowTargetTags ++= Seq("v*"),
    githubWorkflowBuild := Seq(
      WorkflowStep
        .Sbt(List("scalafmtCheckAll", "scalafmtSbtCheck"), name = Some("Check formatting")),
      WorkflowStep.Sbt(List("test:compile"), name = Some("Compile")),
      WorkflowStep.Sbt(List("test"), name = Some("Run tests")),
      WorkflowStep.Sbt(List("doc"), name = Some("Build docs"))
    ),
    githubWorkflowPublish := Seq(WorkflowStep.Sbt(List("ci-release"), name = Some("Release"))),
    githubWorkflowPublishTargetBranches :=
      Seq(RefPredicate.StartsWith(Ref.Tag("v"))),
  )
)
