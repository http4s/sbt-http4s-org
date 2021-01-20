enablePlugins(SonatypeCiReleasePlugin)

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
    addSbtPlugin("com.codecommit" % "sbt-spiewak-sonatype" % "0.20.1"),
    addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.2")
  )

// General Settings
inThisBuild(
  List(
    organization := "org.http4s",
    organizationName := "http4s.org",
    publishGithubUser := "rossabaker",
    publishFullName := "Ross A. Baker",
    baseVersion := "0.7",
    crossScalaVersions := Seq("2.12.13"),
    developers := List(
      Developer(
        "rossabaker",
        "Ross A. Baker",
        "ross@rossabaker.com",
        url("https://github.com/rossabaker"))
    ),
    homepage := Some(url("https://github.com/http4s/sbt-http4s-org")),
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/http4s/sbt-http4s-org.git"),
        "git@github.com:http4s/sbt-http4s-org.git")),
    licenses := Seq("Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0.html")),
    githubWorkflowTargetTags ++= Seq("v*"),
    githubWorkflowBuild := Seq(
      WorkflowStep
        .Sbt(List("scalafmtCheckAll", "scalafmtSbtCheck"), name = Some("Check formatting")),
      WorkflowStep.Sbt(List("test:compile"), name = Some("Compile")),
      WorkflowStep.Sbt(List("test"), name = Some("Run tests")),
      WorkflowStep.Sbt(List("doc"), name = Some("Build docs"))
    ),
    spiewakMainBranches := Seq("main")
  ))
