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

import com.typesafe.sbt.SbtGit.git
import sbtghactions._
import sbtghactions.GenerativeKeys._
import sbtspiewak._, SonatypeCiReleasePlugin.autoImport._

object Http4sOrgPlugin extends AutoPlugin {
  object autoImport

  override def trigger = allRequirements

  override def requires = SpiewakPlugin && SonatypeCiReleasePlugin

  override def buildSettings = organizationSettings ++ githubActionsSettings ++ http4sStyleSnapshots

  val organizationSettings: Seq[Setting[_]] =
    Seq(
      organization := "org.http4s",
      organizationName := "http4s.org"
    )

  val githubActionsSettings: Seq[Setting[_]] =
    Seq(
      spiewakMainBranches := Seq("main"),
      githubWorkflowJavaVersions := List("adoptium@8", "adoptium@11", "adoptium@17"),
      githubWorkflowEnv += ("JABBA_INDEX" -> "https://github.com/typelevel/jdk-index/raw/main/index.json"),
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
      ),
      githubWorkflowBuildMatrixFailFast := Some(false),
      githubWorkflowArtifactUpload := false,
      githubWorkflowTargetBranches := Seq("**")
    )

  val http4sStyleSnapshots: Seq[Setting[_]] =
    Seq(
      isSnapshot :=
        git.gitCurrentTags.value.isEmpty || git.gitUncommittedChanges.value,
      version := {
        val v = version.value
        val suffix = "-SNAPSHOT"
        if (isSnapshot.value && !v.endsWith(suffix)) v + suffix else v
      },
      githubWorkflowPublish := Seq(
        WorkflowStep.Sbt(List("+publish")),
        WorkflowStep
          .Sbt(List("sonatypeBundleRelease"), cond = Some("startsWith(github.ref, 'refs/tags/v')"))
      )
    )
}
