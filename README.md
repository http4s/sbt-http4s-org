# sbt-http4s-org

This project is intended to set up sensible defaults and standards for projects under http4s.org.
If it's useful outside http4s.org, it probably should be pushed further upstream into [sbt-typelevel](https://github.com/typelevel/sbt-typelevel).

## Installing it

The plugin is deployed to Sonatype.

```scala
addSbtPlugin("org.http4s" % "sbt-http4s-org" % "@VERSION@")
```

## What does it do?

### `Http4sOrgPlugin`

Triggers automatically.  Use on all http4s modules.

* Activates the sensible defaults of [sbt-typelevel](https://github.com/typelevel/sbt-typelevel/)
* Sets the organization info
* Enables automated license headers with appropriate years
* Enables [sbt-github-actions](https://github.com/djspiewak/sbt-github-actions) with settings appropriate to our workflow

### `Http4sOrgSitePlugin`

Must be manually triggered.  Extends the `TypelevelSitePlugin` with the http4s.org theme.  See the [sbt-typelevel-site docs](https://typelevel.org/sbt-typelevel/#how-do-i-publish-a-site-like-this-one) for usage.
