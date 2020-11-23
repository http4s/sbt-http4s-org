package org.http4s.sbt

import sbt._
import sbt.Keys._

import com.typesafe.sbt.SbtGit.git
import dotty.tools.sbtplugin.DottyPlugin
import dotty.tools.sbtplugin.DottyPlugin.autoImport._
import de.heikoseeberger.sbtheader.{AutomateHeaderPlugin, LicenseDetection, LicenseStyle}
import de.heikoseeberger.sbtheader.HeaderPlugin.autoImport._
import java.lang.{Runtime => JRuntime}
import _root_.io.chrisdavenport.sbtmimaversioncheck.MimaVersionCheck
import org.scalafmt.sbt.ScalafmtPlugin
import sbtghactions._
import sbtghactions.GenerativeKeys._

object Http4sOrgPlugin extends AutoPlugin {
  object autoImport

  override def trigger = allRequirements

  override def requires =
    AutomateHeaderPlugin &&
      DottyPlugin &&
      MimaVersionCheck &&
      ScalafmtPlugin

  override lazy val projectSettings: Seq[Setting[_]] =
    organizationSettings ++
      scalaSettings ++
      docSettings ++
      headerSettings

  val organizationSettings: Seq[Setting[_]] =
    Seq(
      organization := "org.http4s",
      organizationName := "http4s.org"
    )

  val scalaSettings: Seq[Setting[_]] =
    Seq(
      scalacOptions ++= {
        if (isDotty.value) Seq.empty
        else
          Seq(
            "-Ybackend-parallelism",
            math.min(JRuntime.getRuntime.availableProcessors, 16).toString
          )
      }
    )

  val docSettings: Seq[Setting[_]] =
    Seq(
      Compile / doc / scalacOptions ++= {
        (for {
          headCommit <- git.gitHeadCommit.value
          isSnapshot = git.gitCurrentTags.value.map(git.gitTagToVersionNumber.value).flatten.isEmpty
          ref = if (isSnapshot) headCommit else s"v${version.value}"
          scm <- scmInfo.value
          browseUrl = scm.browseUrl
          path = s"${browseUrl}/blob/${ref}€{FILE_PATH}.scala"
        } yield Seq(
          "-doc-source-url",
          path,
          "-sourcepath",
          baseDirectory.in(LocalRootProject).value.getAbsolutePath
        )).getOrElse(Seq.empty[String])
      }
    )

  val headerSettings: Seq[Setting[_]] =
    Seq(
      headerLicenseStyle := LicenseStyle.SpdxSyntax,
      headerLicense := {
        val current = java.time.Year.now().getValue
        val copyrightYear = startYear.value.fold(current.toString)(start => s"$start-$current")
        LicenseDetection(
          licenses.value.toList,
          organizationName.value,
          Some(copyrightYear),
          headerLicenseStyle.value
        )
      }
    )

  val githubActionsSettings: Seq[Setting[_]] =
    Seq(
      githubWorkflowJavaVersions := List("adopt@1.8", "adopt@1.11", "adopt@1.15"),
      githubWorkflowTargetTags ++= Seq("v*"),
      githubWorkflowPublish := Seq(WorkflowStep.Sbt(List("ci-release"), name = Some("Release"))),
      githubWorkflowPublishTargetBranches :=
        Seq(RefPredicate.StartsWith(Ref.Tag("v"))),
      githubWorkflowBuild := Seq(
        WorkflowStep
          .Sbt(List("scalafmtCheckAll", "scalafmtSbtCheck"), name = Some("Check formatting")),
        WorkflowStep.Sbt(List("headerCheckAll"), name = Some("Check headers")),
        WorkflowStep.Sbt(List("test:compile"), name = Some("Compile")),
        WorkflowStep.Sbt(List("mimaReportBinaryIssues"), name = Some("Check binary compatibility")),
        WorkflowStep.Sbt(
          List("unusedCompileDependenciesTest"),
          name = Some("Check unused compile dependencies")),
        WorkflowStep.Sbt(List("test"), name = Some("Run tests")),
        WorkflowStep.Sbt(List("doc"), name = Some("Build docs"))
      )
    )
}
