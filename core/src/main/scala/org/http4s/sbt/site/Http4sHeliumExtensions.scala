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

import cats.effect.Resource
import cats.effect.kernel.Sync
import laika.ast.Path
import laika.io.model.InputTree
import laika.rewrite.DefaultTemplatePath
import laika.theme.Theme
import laika.theme.ThemeBuilder
import laika.theme.ThemeProvider

object Http4sHeliumExtensions extends ThemeProvider {

  override def build[F[_]](implicit F: Sync[F]): Resource[F, Theme[F]] =
    ThemeBuilder[F]("Typelevel Helium Extensions")
      .addInputs(
        InputTree[F]
          .addStream(
            F.blocking(getClass.getResourceAsStream("default.template.html")),
            DefaultTemplatePath.forHTML
          )
          .addStream(
            F.blocking(getClass.getResourceAsStream("site/styles.css")),
            Path.Root / "site" / "styles.css"
          )
          .addStream(
            F.blocking(getClass.getResourceAsStream("images/http4s-favicon.png")),
            Path.Root / "images" / "http4s-favicon.png"
          )
          .addStream(
            F.blocking(getClass.getResourceAsStream("images/http4s-favicon.svg")),
            Path.Root / "images" / "http4s-favicon.svg"
          )
          .addStream(
            F.blocking(getClass.getResourceAsStream("images/http4s-logo.svg")),
            Path.Root / "images" / "http4s-logo.svg"
          )
      )
      .build

}
