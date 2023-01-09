package com.multitabviewer

import scalacss.DevDefaults._
import scalacss.internal.Attrs.border
import scalacss.internal.Attrs.height
import scalacss.internal.Attrs.overflowY
import scalacss.internal.Attrs
import scalacss.internal.Attrs.objectFit

object Styles extends StyleSheet.Inline {
  import dsl._

  val mainColor = mixin(backgroundColor(c"#302075"))
  val videoColor = mixin(backgroundColor.rgb(49, 30, 114))
  val primaryColor = mixin(backgroundColor.rgb(209, 36, 94))
  val secondaryColor = mixin(backgroundColor.rgb(165, 37, 119))
  val textColor = mixin(color.rgb(4, 2, 30))
  val lightTextColor = mixin(color(c"#FEFEFE"))

  val common = mixin(
    // TODO:
    mainColor
  )

  val container = style(
    common,
    position.relative
  )

  val flexCenter = style(
    display.flex,
    alignItems.center,
    justifyContent.center
  )

  val flexColumn = style(
    flexDirection.column
  )

  val logoText = style(
    fontSize(2.rem),
    fontWeight.bold,
    lightTextColor
  )

  val logo = style(
    width(48.px),
    height(48.px),
    objectFit.contain,
    paddingRight(8.px)
  )

  val description = style(
    fontSize(0.9.rem)
  )

  val mediaList = style(
    // TODO:
    width(100.vw),
    display.flex,
    alignItems.flexStart,
    justifyContent.flexStart,
    flexWrap.wrap,
    overflowY.scroll
  )

  val video = style(
    border(2.px, solid),
    borderColor(c"#361D66"),
    cursor.pointer,
    objectFit.contain
  )

  val button = style(
    textColor,
    primaryColor,
    display.flex,
    alignItems.center,
    justifyContent.center,
    margin(16.px),
    width(24.px),
    height(24.px),
    padding(8.px),
    cursor.pointer,
    userSelect.none,
    boxShadow := "0px 8px 15px rgba(0, 0, 0, 0.4)",
    &.hover(
      secondaryColor,
      transform := "scale(1.1, 1.1)"
    )
  )

  val buttonPosition = style(
    position.absolute,
    zIndex(100),
    right(16.px),
    bottom(16.px)
  )

  val buttonText = style(
    fontWeight.bold
  )

  val buttonBase = mixin(
    padding(8.px),
    fontSize(1.5.rem),
    position.absolute,
    cursor.pointer,
    lightTextColor,
    display.flex,
    alignItems.center,
    justifyContent.center,
    opacity(0.7),
    &.hover(
      transform := "scale(1.1, 1.1)",
      opacity(1.0)
    )
  )

  val closeButton = style(
    buttonBase,
    top(4.px),
    right(4.px)
  )

  val fullScreenButton = style(
    buttonBase,
    top(4.px),
    left(4.px)
  )

  val soundButton = style(
    buttonBase,
    top(4.px),
    left(48.px)
  )
}
