/*
 * Copyright 2020 http4s.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.http4s.sbt

import sbt._
import sbt.Keys._

import de.heikoseeberger.sbtheader.{LicenseDetection, LicenseStyle}
import de.heikoseeberger.sbtheader.HeaderPlugin.autoImport._
import sbtghactions._
import sbtghactions.GenerativeKeys._
import sbtspiewak._, SonatypeCiRelease.autoImport._

object Http4sOrgPlugin extends AutoPlugin {
  object autoImport

  override def trigger = allRequirements

  override def requires = SpiewakPlugin && SonatypeCiRelease

  override def buildSettings = organizationSettings

  override def projectSettings = headerSettings ++ githubActionsSettings

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
      spiewakMainBranches := Seq("main"),
      githubWorkflowJavaVersions := List("adopt@1.8", "adopt@1.11", "adopt@1.15"),
      githubWorkflowBuild := Seq(
        WorkflowStep
          .Sbt(List("scalafmtCheckAll", "scalafmtSbtCheck"), name = Some("Check formatting")),
        WorkflowStep.Sbt(List("headerCheck", "test:headerCheck"), name = Some("Check headers")),
        WorkflowStep.Sbt(List("test:compile"), name = Some("Compile")),
        WorkflowStep.Sbt(List("mimaReportBinaryIssues"), name = Some("Check binary compatibility")),
        WorkflowStep.Sbt(
          List("undeclaredCompileDependencies", "unusedCompileDependenciesTest"),
          name = Some("Check explicit dependencies")),
        WorkflowStep.Sbt(List("test"), name = Some("Run tests")),
        WorkflowStep.Sbt(List("doc"), name = Some("Build docs"))
      )
    )
}
