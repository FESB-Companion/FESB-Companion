package com.tstudioz.fax.fme.feature.timetable.repository

import com.tstudioz.fax.fme.feature.timetable.models.Event
import com.tstudioz.fax.fme.feature.timetable.models.TimeTableInfo
import kotlinx.coroutines.flow.SharedFlow
import java.time.LocalDate

interface TimeTableRepositoryInterface {

    val events: SharedFlow<List<Event>>

    suspend fun fetchTimetable(user: String, startDate: String, endDate: String, shouldCache: Boolean): List<Event>

    suspend fun fetchTimeTableCalendar(startDate: String, endDate: String): Map<LocalDate, TimeTableInfo>

}