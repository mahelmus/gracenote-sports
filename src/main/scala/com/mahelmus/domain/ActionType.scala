package com.mahelmus.domain

/**
 * This trait groups all the action types available in a soccer match
 */
sealed abstract class ActionType(final val value: String) extends Product with Serializable

object ActionType {
  val allActionTypes: Set[ActionType] = Set(BlockedShot, Corner, DropBall, EndOfPeriod, FoulCommitted,
    FreeKick, Goal, GoalKick, HitBar, HitPost)

  private final val blockedShot = "blocked shot"
  private final val corner = "corner"
  private final val dropBall = "drop ball"
  private final val endOfPeriod = "end of period"
  private final val foulCommitted = "foul committed"
  private final val freeKick = "free kick"
  private final val goal = "goal"
  private final val goalKick = "goal kick"
  private final val hitBar = "hit bar"
  private final val hitPost = "hit post"
  private final val injuryTime = "injury time"
  private final val kickOff = "kick off"
  private final val lineUp = "line up"
  private final val offside = "off side"
  private final val ownGoal = "own goal"
  private final val penaltyMissed = "penalty missed"
  private final val red = "red"
  private final val red2yellow = "red (2 yellow)"
  private final val saveByGoalkeeper = "save by goalkeeper"
  private final val saveByPlayer = "save by player"
  private final val scoringOpportunity = "scoring opportunity"
  private final val shotBlockedBy = "shot blocked by"
  private final val shotOnGoal = "shot on goal"
  private final val shotWide = "shot wide"
  private final val substitution = "substitution"
  private final val yellow = "yellow"

  def apply(str: String): ActionType =
    str.trim.toLowerCase() match {
      case BlockedShot.value => BlockedShot
      case Corner.value => Corner
      case DropBall.value => DropBall
      case EndOfPeriod.value => EndOfPeriod
    }

  case object BlockedShot extends ActionType(blockedShot)
  case object Corner extends ActionType(corner)
  case object DropBall extends ActionType(dropBall)
  case object EndOfPeriod extends ActionType(endOfPeriod)
  case object FoulCommitted extends ActionType(foulCommitted)
  case object FreeKick extends ActionType(freeKick)
  case object Goal extends ActionType(goal)
  case object GoalKick extends ActionType(goalKick)
  case object HitBar extends ActionType(hitBar)
  case object HitPost extends ActionType(hitPost)
  case object InjuryTime extends ActionType(injuryTime)
  case object KickOff extends ActionType(kickOff)
  case object LineUp extends ActionType(lineUp)
  case object Offside extends ActionType(offside)
  case object OwnGoal extends ActionType(ownGoal)
  case object PenaltyMissed extends ActionType(penaltyMissed)
  case object Red extends ActionType(red)
  case object Red2Yellow extends ActionType(red2yellow)
  case object SaveByGoalkeeper extends ActionType(saveByGoalkeeper)
  case object SaveByPlayer extends ActionType(saveByPlayer)
  case object ScoringOpportunity extends ActionType(scoringOpportunity)
  case object ShotBlockedBy extends ActionType(shotBlockedBy)
  case object ShotOnGoal extends ActionType(shotOnGoal)
  case object ShotWide extends ActionType(shotWide)
  case object Substitution extends ActionType(substitution)
  case object Yellow extends ActionType(yellow)
}

