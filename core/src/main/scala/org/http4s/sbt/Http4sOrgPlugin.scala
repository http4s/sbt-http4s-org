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

import org.scalafmt.sbt.ScalafmtPlugin
import sbtghactions._
import sbtghactions.GenerativeKeys._
import sbtspiewak.SpiewakPlugin

object Http4sOrgPlugin extends AutoPlugin {
  object autoImport

  override def trigger = allRequirements

  override def requires =
    ScalafmtPlugin &&
      SpiewakPlugin

  override lazy val projectSettings: Seq[Setting[_]] =
    organizationSettings

  override val buildSettings: Seq[Setting[_]] =
    addCommandAlias(
      "ci",
      List(
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
        "doc"
      ).mkString("; ", "; ", "")
    )

  val organizationSettings: Seq[Setting[_]] =
    Seq(
      organization := "org.http4s",
      organizationName := "http4s.org"
    )

  val githubActionsSettings: Seq[Setting[_]] =
    Seq(
      githubWorkflowJavaVersions := List("adopt@1.8", "adopt@1.11", "adopt@1.15"),
      githubWorkflowTargetTags ++= Seq("v*"),
      githubWorkflowPublish := Seq(WorkflowStep.Sbt(List("ci-release"), name = Some("Release"))),
      githubWorkflowPublishTargetBranches :=
        Seq(RefPredicate.StartsWith(Ref.Tag("v"))),
      githubWorkflowBuild := Seq(WorkflowStep.Sbt(List("ci")))
    )
}
