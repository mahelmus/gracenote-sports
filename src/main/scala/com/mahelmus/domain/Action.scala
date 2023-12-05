package com.mahelmus.domain

import java.time.LocalDate

/**
 * Action that takes place in a soccer match
 * @param id A unique identifier for each action
 * @param competition A unique identifier for the competition the match is part of
 * @param matchId A unique identifier for the match
 * @param kickoffDate Date and Time in CET that the match the action is part of kicked off
 * @param actionType [[ActionType]] the row represents
 * @param period The [[Period]] the [[Action]] occurred in
 * @param startTime milliseconds since d_date that the specific actions started, when available
 * @param endTime milliseconds since d_date that the specific actions ended, when available
 * @param homeOrAway [[Home]] or [[Away]] team
 * @param team [[Team]] in the competition
 * @param person Primary [[Person]] involved in the [[Action]]
 * @param shirtNum Squad / Shirt number of the primary person involved in the action, when available
 * @param personFunction Position of the primary person involved in the action
 * @param reason Qualifier for the reason leading to the [[Action]], when available
 * @param extraInfo Further data on the [[Action]]
 * @param subPerson Second [[Person]] involved in the [[Action]], when available
 */
final case class Action(id: Long,
                        competition: String,
                        matchId: Long,
                        kickoffDate: LocalDate,
                        actionType: ActionType,
                        period: Period,
                        startTime: Option[Long],
                        endTime: Option[Long],
                        homeOrAway: HomeOrAway,
                        team: Team,
                        person: Person,
                        shirtNum: Option[Byte],
                        personFunction: String,
                        reason: Option[String],
                        extraInfo: Option[String],
                        subPerson: Option[Person]
                       )

/**
 * Team that performs an action
 * @param id Unique identifier for the team in the competition
 * @param name name of the team
 */
case class Team(id: Int, name: String)

/**
 * Person that performs an action
 * @param id unique identifier of the Person. 0 for no identifier
 * @param name full name of the Person. NULL for no name
 */
case class Person(id: Long, name: String)

