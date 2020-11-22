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

object CompileTimePlugin extends AutoPlugin {
  object autoImport

  override lazy val projectSettings: Seq[Setting[_]] =
    Seq(
      ivyConfigurations += CompileTime,
      unmanagedClasspath in Compile ++= update.value.select(configurationFilter("CompileTime"))
    )

  val CompileTime = config("CompileTime").hide
}
