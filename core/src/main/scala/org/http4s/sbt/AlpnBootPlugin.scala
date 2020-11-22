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
