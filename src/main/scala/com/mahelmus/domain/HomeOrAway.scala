package com.mahelmus.domain

/**
 * Whether the action is performed by the home or away team
 */
sealed trait HomeOrAway extends Product with Serializable {
  def value: String
}

object HomeOrAway {
  def apply(str: String): HomeOrAway =
    str.trim.toLowerCase() match {
      case Home.value => Home
      case Away.value => Away
      case _ => Away
    }
}
case object Home extends HomeOrAway {
  final val value: String = "home"
}
case object Away extends HomeOrAway {
  final val value: String = "away"
}