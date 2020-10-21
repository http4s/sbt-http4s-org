package org.http4s.sbt

import sbt._
import sbt.Keys._

import explicitdeps.ExplicitDepsPlugin.autoImport._

import CompileTimePlugin.CompileTime

object SilencerPlugin extends AutoPlugin {
  object autoImport {
    val silencerVersion = settingKey[String]("Version of the silencer compiler plugin")
  }

  import autoImport._

  override def trigger = allRequirements
  override def requires = CompileTimePlugin

  override lazy val projectSettings: Seq[Setting[_]] =
    if (isDotty) {
      Seq(
        silencerVersion := "1.7.0",
        libraryDependencies ++=
          Seq(
            compilerPlugin(
              ("com.github.ghik" % "silencer-plugin" % silencerVersion.value).cross(
                CrossVersion.full)),
            ("com.github.ghik" % "silencer-lib" % silencerVersion.value % CompileTime)
              .cross(CrossVersion.full),
            ("com.github.ghik" % "silencer-lib" % silencerVersion.value % Test)
              .cross(CrossVersion.full)
          ),
        unusedCompileDependenciesFilter -= moduleFilter("com.github.ghik", name = "silencer-lib")
      )
    } else {
      Seq.empty
    }
}
