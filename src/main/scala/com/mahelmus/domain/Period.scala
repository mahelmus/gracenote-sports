package com.mahelmus.domain

/**
 * Moment in a match when an [[Action]] can take place
 */
sealed trait Period extends Product with Serializable {
  def value: String
}

object Period {
  def apply(str: String): Period =
    str.trim.toLowerCase() match {
      case Start.value => Start
      case FirstHalf.value => FirstHalf
      case SecondHalf.value => SecondHalf
      case _ => BeforeMatch
    }
}

case object BeforeMatch extends Period {
  final val value: String = "before"
}

case object Start extends Period {
  final val value: String = "start"
}

case object FirstHalf extends Period {
  final val value: String = "first half"
}

case object SecondHalf extends Period {
  final val value: String = "second half"
}