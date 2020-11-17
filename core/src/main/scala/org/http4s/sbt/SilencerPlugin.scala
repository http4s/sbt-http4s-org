/*
 * Copyright 2020-2020 http4s.org
 *
 * SPDX-License-Identifier: Apache-2.0
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
