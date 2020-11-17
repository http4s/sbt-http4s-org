/*
 * Copyright 2020-2020 http4s.org
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.http4s.sbt

import sbt._
import sbt.Keys._

import de.heikoseeberger.sbtheader.{LicenseDetection, LicenseStyle}
import de.heikoseeberger.sbtheader.HeaderPlugin.autoImport._
import _root_.io.chrisdavenport.sbtmimaversioncheck.MimaVersionCheck
import org.scalafmt.sbt.ScalafmtPlugin
import sbtghactions._
import sbtghactions.GenerativeKeys._
import sbtspiewak.SpiewakPlugin

object Http4sOrgPlugin extends AutoPlugin {
  object autoImport

  override def trigger = allRequirements

  override def requires =
    ScalafmtPlugin &&
    SpiewakPlugin &&
    MimaVersionCheck

  override lazy val projectSettings: Seq[Setting[_]] =
    organizationSettings ++
      headerSettings

  override val buildSettings: Seq[Setting[_]] =
    addCommandAlias("ci", List(
      "project /",
      "headerCheck",
      "test:headerCheck",
      "scalafmtCheckAll",
      "scalafmtSbtCheck",
      "clean",
      "mimaReportBinaryIssuesIfRelevant",
      "undeclaredCompileDependencies",
      "unusedCompileDependenices",
      "testIfRelevant",
      "doc",
    ).mkString("; ", "; ", ""))

  val organizationSettings: Seq[Setting[_]] =
    Seq(
      organization := "org.http4s",
      organizationName := "http4s.org"
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
        WorkflowStep.Sbt(List("ci")))
    )
}
