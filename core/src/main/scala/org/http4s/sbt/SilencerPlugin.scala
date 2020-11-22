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

import dotty.tools.sbtplugin.DottyPlugin
import dotty.tools.sbtplugin.DottyPlugin.autoImport._
import explicitdeps.ExplicitDepsPlugin.autoImport._
import CompileTimePlugin.CompileTime

object SilencerPlugin extends AutoPlugin {
  object autoImport {
    val silencerVersion = settingKey[String]("Version of the silencer compiler plugin")
  }

  import autoImport._

  override def trigger = allRequirements
  override def requires = DottyPlugin && CompileTimePlugin

  override lazy val projectSettings: Seq[Setting[_]] =
    Seq(
      silencerVersion := "1.7.0",
      libraryDependencies ++= {
        if (isDotty.value) Seq.empty
        else
          Seq(
            compilerPlugin(
              ("com.github.ghik" % "silencer-plugin" % silencerVersion.value).cross(
                CrossVersion.full)),
            ("com.github.ghik" % "silencer-lib" % silencerVersion.value % CompileTime)
              .cross(CrossVersion.full),
            ("com.github.ghik" % "silencer-lib" % silencerVersion.value % Test)
              .cross(CrossVersion.full)
          )
      },
      unusedCompileDependenciesFilter -= moduleFilter("com.github.ghik", name = "silencer-lib")
    )
}
