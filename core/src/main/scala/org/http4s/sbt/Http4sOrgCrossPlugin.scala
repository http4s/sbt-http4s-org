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

import explicitdeps.ExplicitDepsPlugin
import org.typelevel.sbt.gha._, GenerativeKeys._
import sbtcrossproject.CrossPlugin
import sbtcrossproject.CrossPlugin.autoImport._

object Http4sOrgCrossPlugin extends AutoPlugin {
  object autoImport

  override def trigger = allRequirements

  override def requires = Http4sOrgPlugin && ExplicitDepsPlugin && CrossPlugin

  override def buildSettings: Seq[Setting[_]] = Seq(
    githubWorkflowBuildPostamble ++= {
      // unusedCompileDependenciesTest only works on the JVM
      crossProjectPlatform.value match {
        case JVMPlatform =>
          Seq(
            WorkflowStep.Sbt(
              List("unusedCompileDependenciesTest"),
              name = Some("Check unused compile dependencies")))
        case _ =>
          Seq.empty
      }
    }
  )
}
