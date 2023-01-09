val scala3Version = "3.2.1"

lazy val root = project
  .in(file("."))
  .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
  .settings(
    name := "tab-mirror",
    version := "0.1.0",
    scalaVersion := scala3Version,
    scalaJSUseMainModuleInitializer := true,
    webpack / version := "5.75.0",
    webpackCliVersion := "5.0.1",
    startWebpackDevServer / version := "4.11.0",
    resolvers += "jitpack" at "https://jitpack.io",
    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % "0.7.29" % Test,
      "com.raquo" %%% "laminar" % "0.14.5",
      "com.github.japgolly.scalacss" %%% "core" % "1.0.0",
      "com.github.fdietze.scala-js-fontawesome" %%% "scala-js-fontawesome" % "a412650e7f"
    ),
    Compile / npmDependencies ++= Seq(
      "uuid" -> "^9.0.0"
    ),
    Compile / npmDevDependencies ++= Seq(
      "copy-webpack-plugin" -> "^11.0.0",
      "html-webpack-plugin" -> "^5.5.0"
    ),
    Compile / webpackConfigFile := Some(file("custom.webpack.config.js"))
  )
