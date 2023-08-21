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

package org.http4s.sbt.site

import cats.effect.Async
import cats.effect.Resource
import laika.ast.Path
import laika.io.model.InputTree
import laika.theme.Theme
import laika.theme.ThemeBuilder
import laika.theme.ThemeProvider

object Http4sHeliumExtensions extends ThemeProvider {

  private val imageFileNames = Seq(
    "http4s-favicon.png",
    "http4s-favicon.svg",
    "http4s-logo.svg",
    "http4s-logo-text-light.svg",
    "http4s-logo-text-dark.svg"
  )

  override def build[F[_]](implicit F: Async[F]): Resource[F, Theme[F]] = {

    val inputs = imageFileNames.foldLeft(InputTree[F]) { case (inputTree, fileName) =>
      inputTree.addClassResource[this.type](s"images/$fileName", Path.Root / "images" / fileName)
    }

    ThemeBuilder[F]("http4s Images")
      .addInputs(inputs)
      .build
  }

}
