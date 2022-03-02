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
