/*
 * Copyright 2020-2020 http4s.org
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.http4s.sbt

import sbt._
import sbt.Keys._

object AlpnBootPlugin extends AutoPlugin {
  object autoImport {
    val alpnBootModule = settingKey[ModuleID]("JAR to use for ALPN boot")
  }

  import autoImport._

  override lazy val projectSettings: Seq[Setting[_]] =
    Seq(
      alpnBootModule := "org.mortbay.jetty.alpn" % "alpn-boot" % "8.1.13.v20181017",
      libraryDependencies += alpnBootModule.value % Runtime,
      run / javaOptions ++= addAlpnPath((Runtime / managedClasspath).value, alpnBootModule.value)
    )

  def addAlpnPath(classpath: Classpath, alpnBoot: ModuleID): Seq[String] = {
    def isAlpnBoot(m: ModuleID) =
      (m.organization == alpnBoot.organization) &&
        (m.name == alpnBoot.name) &&
        (m.revision == alpnBoot.revision)
    val args = classpath.collect {
      case entry if entry.get(moduleID.key).fold(false)(isAlpnBoot) =>
        s"-Xbootclasspath/p:${entry}"
    }
    args
  }
}
