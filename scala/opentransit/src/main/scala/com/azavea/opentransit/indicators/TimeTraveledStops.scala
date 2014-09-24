package com.azavea.opentransit.indicators

import com.azavea.gtfs._
import com.azavea.opentransit._

import com.github.nscala_time.time.Imports._
import org.joda.time._

object TimeTraveledStops extends Indicator
                            with AggregatesByAll {
  val name = "time_traveled_stops"

  val calculation =
    new PerTripIndicatorCalculation[Seq[Int]] {
      def map(trip: Trip) =
          trip.schedule
            .zip(trip.schedule.tail)
            .map { case (stop1, stop2) =>
              Minutes.minutesBetween(stop1.arrivalTime, stop2.arrivalTime).getMinutes
             }
   
      def reduce(durations: Seq[Seq[Int]]) = {
        val (sum, count) =
          durations
            .flatten
            .foldLeft((0,0)) { case ((sum, count), minutes) =>
              (sum + minutes, count + 1)
             }
        sum.toDouble / count
      }
    }
}
