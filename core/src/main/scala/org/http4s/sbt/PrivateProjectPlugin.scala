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

import com.typesafe.tools.mima.plugin.MimaPlugin
import com.typesafe.tools.mima.plugin.MimaPlugin.autoImport.mimaPreviousArtifacts
import sbt._
import sbt.Keys._

object PrivateProjectPlugin extends AutoPlugin {
  override def trigger = noTrigger

  override def requires = Http4sOrgPlugin && MimaPlugin

  override lazy val projectSettings: Seq[Setting[_]] =
    Seq(
      publish / skip := true,
      mimaPreviousArtifacts := Set.empty
    )
}
