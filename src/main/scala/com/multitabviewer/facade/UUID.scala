package com.multitabviewer.facade

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object UUID {
  @js.native
  @JSImport("uuid", "v4")
  def v4(): String = js.native
}
