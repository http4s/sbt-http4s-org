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

import explicitdeps.ExplicitDepsPlugin
import org.typelevel.sbt._
import org.typelevel.sbt.gha._
import sbt.Keys._
import sbt._
import scalafix.sbt.ScalafixPlugin.autoImport._

import ExplicitDepsPlugin.autoImport._
import GenerativeKeys._
import TypelevelCiPlugin.autoImport._
import TypelevelSonatypePlugin.autoImport._

object Http4sOrgPlugin extends AutoPlugin {
  override def trigger = allRequirements

  override def requires = TypelevelPlugin && TypelevelScalafixPlugin && ExplicitDepsPlugin

  override def buildSettings =
    publishSettings ++ organizationSettings ++ githubActionsSettings ++ scalafixSettings

  override def projectSettings = explicitDepsSettings

  override def extraProjects = Seq(
    Project(internalScalafixProject, file(s".$internalScalafixProject"))
      .enablePlugins(NoPublishPlugin)
  )

  lazy val publishSettings: Seq[Setting[_]] =
    Seq(
      tlSonatypeUseLegacyHost := false
    )

  lazy val organizationSettings: Seq[Setting[_]] =
    Seq(
      organization := "org.http4s",
      organizationName := "http4s.org"
    )

  lazy val githubActionsSettings: Seq[Setting[_]] =
    Seq(
      githubWorkflowJavaVersions := List("8", "11", "17").map(JavaSpec.temurin(_)),
      tlCiScalafixCheck := false, // we add our own, that skips Scala 3
      githubWorkflowBuildPostamble ++= Seq(
        WorkflowStep.Sbt(
          List("scalafixAll --check"),
          name = Some("Check scalafix lints"),
          cond = Some(s"${primaryJavaCond.value} && !startsWith(matrix.scala, '3')")
        ),
        WorkflowStep.Sbt(
          List("unusedCompileDependenciesTest"),
          name = Some("Check unused compile dependencies"),
          cond = Some(primaryJavaCond.value)
        )
      ),
      githubWorkflowBuildMatrixFailFast := Some(false)
    )

  lazy val explicitDepsSettings: Seq[Setting[_]] =
    Seq(
      unusedCompileDependenciesFilter -= moduleFilter("org.typelevel", "scalac-compat-annotation")
    )

  lazy val scalafixSettings: Seq[Setting[_]] =
    Seq(
      scalafixScalaBinaryVersion := (LocalProject(
        internalScalafixProject) / scalaBinaryVersion).value,
      scalafixDependencies ++= Seq(
        "org.http4s" %% "http4s-scalafix-internal" % "0.23.27"
      )
    )

  private val primaryJavaCond = Def.setting {
    val java = githubWorkflowJavaVersions.value.head
    s"matrix.java == '${java.render}'"
  }

  private final val internalScalafixProject = "sbt-http4s-org-scalafix-internal"

}
