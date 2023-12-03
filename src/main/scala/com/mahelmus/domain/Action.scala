package com.mahelmus.domain

import java.time.{Instant, LocalDate}

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
 * @param id unique identifier of the Team
 * @param name name of the team
 */
case class Team(id: Int, name: String)

/**
 * Person that performs an action
 * @param id unique identifier of the Person. 0 for no identifier
 * @param name full name of the Person. NULL for no name
 */
case class Person(id: Long, name: String)

