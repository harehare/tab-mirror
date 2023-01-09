package com.multitabviewer.facade

import scala.scalajs.js
import org.scalajs.dom.Blob
import scala.scalajs.js.Promise
import scala.scalajs.js.annotation.JSImport
import org.scalajs.dom.experimental.mediastream.{
  MediaStream,
  MediaStreamConstraints
}
import scala.scalajs.js.annotation.JSGlobal

@js.native
trait MediaDevices extends js.Object {
  def getDisplayMedia(
      constraints: MediaStreamConstraints
  ): Promise[MediaStream]
  def getUserMedia(
      constraints: MediaStreamConstraints
  ): Promise[MediaStream]
}

@js.native
trait Navigator extends js.Object {
  val mediaDevices: MediaDevices = js.native
}

@js.native
@JSGlobal
object URL extends js.Object {
  def createObjectURL(stream: MediaStream): String = js.native
}

object Dom {
  lazy val navigator: Navigator =
    scala.scalajs.js.Dynamic.global.navigator.asInstanceOf[Navigator]
}
