package com.multitabviewer

import com.raquo.laminar.api.L.*
import org.scalajs.dom
import scala.scalajs.js
import org.scalajs.dom.experimental.mediastream.{
  MediaStream,
  MediaStreamConstraints
}
import scala.concurrent.ExecutionContext.Implicits.global

import com.multitabviewer.facade.Dom
import com.multitabviewer.facade.URL
import com.multitabviewer.Styles
import com.multitabviewer.facade.UUID
import org.scalajs.dom.MediaStream
import com.raquo.domtypes.generic.codecs.AsIsCodec
import com.raquo.domtypes.generic.codecs.BooleanAsIsCodec
import scalacss.Defaults._
import fontAwesome.fontawesome
import fontAwesome.freeSolid

object UnsafeInnerHtmlReceiver {

  def :=[El <: Element](innerHtml: String): Modifier[El] = {
    new Modifier[El] {
      override def apply(element: El): Unit = element.ref.innerHTML = innerHtml
    }
  }
}

val unsafeInnerHtml: UnsafeInnerHtmlReceiver.type = UnsafeInnerHtmlReceiver

object MultiTabViewerApp {
  // models
  case class Media(
      id: String,
      stream: MediaStream,
      muted: Boolean,
      fullScreen: Boolean
  )

  // update
  sealed trait Command

  case object Add extends Command
  case class Delete(media: Media) extends Command
  case class Muted(media: Media) extends Command
  case class ToggleFullScreen(media: Media) extends Command

  // state
  private val mediaListVar = Var(List[Media]())

  private val commandObserver = Observer[Command] {
    case Add =>
      Dom.navigator.mediaDevices
        .getDisplayMedia(MediaStreamConstraints(true, true, ""))
        .toFuture
        .map(stream =>
          mediaListVar.update(
            new Media(UUID.v4(), stream, true, false) :: _
          )
        )

    case Delete(m) =>
      mediaListVar.update(_.filterNot(_.id.toString() == m.id.toString()))

    case Muted(m) =>
      mediaListVar.update(
        _.map(_m => if (_m.id == m.id) m.copy(muted = !m.muted) else _m)
      )

    case ToggleFullScreen(m) =>
      mediaListVar.update(
        _.map(_m =>
          if (_m.id == m.id) m.copy(fullScreen = !m.fullScreen) else _m
        )
      )
  }

  // views
  lazy val node: Signal[HtmlElement] =
    mediaListVar.signal.map(mediaList =>
      if (mediaList.isEmpty) {
        div(
          cls(Styles.container.htmlClass),
          cls(Styles.flexCenter.htmlClass),
          cls(Styles.flexColumn.htmlClass),
          height := "100svh",
          div(
            cls(Styles.flexCenter.htmlClass),
            img(cls(Styles.logo.htmlClass), src("assets/tab-mirror.svg")),
            div(
              cls(Styles.logoText.htmlClass),
              "Tab Mirror"
            )
          ),
          renderAddMediaButton
        )
      } else {
        div(
          cls(Styles.container.htmlClass),
          div(
            cls(Styles.buttonPosition.htmlClass),
            renderAddMediaButton
          ),
          mediaList.find(_.fullScreen) match {
            case Some(m) =>
              div(
                cls(Styles.mediaList.htmlClass),
                height := "100svh",
                renderMedia(m)
              )
            case None =>
              val columnCount = Math.min(
                Math.max(mediaList.length, 1),
                4
              )

              val rowCount =
                Math.max(mediaList.length / 4, 1)

              div(
                cls(Styles.mediaList.htmlClass),
                height := "100svh",
                children <-- mediaListVar.signal.map(
                  _.map(
                    renderMedia(
                      _: Media,
                      (
                        (dom.window.innerWidth / columnCount).toInt,
                        (dom.window.innerHeight / rowCount).toInt
                      )
                    )
                  )
                )
              )
          }
        )
      }
    )

  private val renderAddMediaButton: HtmlElement =
    div(
      cls(Styles.button.htmlClass),
      onClick.mapTo(Add) --> commandObserver,
      div(
        cls(Styles.buttonText.htmlClass),
        div(unsafeInnerHtml := fontawesome.icon(freeSolid.faPlus).html.join(""))
      )
    )

  private def renderMedia(
      media: Media,
      size: (Int, Int) = (0, 0)
  ): HtmlElement =
    val srcObject: Prop[MediaStream] =
      customProp("srcObject", AsIsCodec[MediaStream])
    val autoPlay: Prop[Boolean] =
      customProp("autoplay", BooleanAsIsCodec)
    val muted: Prop[Boolean] =
      customProp("muted", BooleanAsIsCodec)

    div(
      cls(Styles.container.htmlClass),
      width := (if (media.fullScreen) "100vw" else s"${size._1}px"),
      height := (if (media.fullScreen) "100svh" else s"${size._2}px"),
      video(
        cls(Styles.video.htmlClass),
        width := (if (media.fullScreen) "100vw" else s"${size._1}px"),
        height := (if (media.fullScreen) "100svh" else s"${size._2}px"),
        srcObject := media.stream,
        autoPlay := true,
        muted := media.muted,
        onSuspend.map(_ => Delete(media)) --> commandObserver
      ),
      renderDeleteButton(media),
      renderFullScreenButton(media),
      renderSoundButton(media)
    )

  private def renderDeleteButton(m: Media): HtmlElement =
    div(
      cls(Styles.closeButton.htmlClass),
      onClick.mapTo(Delete(m)) --> commandObserver,
      div(
        unsafeInnerHtml := fontawesome
          .icon(freeSolid.faWindowClose)
          .html
          .join("")
      )
    )

  private def renderFullScreenButton(m: Media): HtmlElement =
    div(
      cls(Styles.fullScreenButton.htmlClass),
      onClick.mapTo(ToggleFullScreen(m)) --> commandObserver,
      div(
        unsafeInnerHtml := (if (m.fullScreen)
                              fontawesome
                                .icon(freeSolid.faCompress)
                                .html
                                .join("")
                            else
                              fontawesome
                                .icon(freeSolid.faExpand)
                                .html
                                .join(""))
      )
    )

  private def renderSoundButton(m: Media): HtmlElement =
    div(
      cls(Styles.soundButton.htmlClass),
      onClick.mapTo(Muted(m)) --> commandObserver,
      div(
        unsafeInnerHtml := (if (m.muted)
                              fontawesome
                                .icon(freeSolid.faVolumeMute)
                                .html
                                .join("")
                            else
                              fontawesome
                                .icon(freeSolid.faVolumeUp)
                                .html
                                .join(""))
      )
    )
}

@main
def main() =
  dom.document
    .querySelector("head")
    .appendChild(styleTag(Styles.render[String]).ref)

  renderOnDomContentLoaded(
    dom.document getElementById "app",
    div(child <-- MultiTabViewerApp.node)
  )
