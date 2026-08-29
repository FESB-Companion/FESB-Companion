package com.tstudioz.fax.fme.feature.timetable.repository

import com.tstudioz.fax.fme.feature.timetable.dao.TimeTableDao
import com.tstudioz.fax.fme.feature.timetable.models.Event
import com.tstudioz.fax.fme.feature.timetable.models.EventRoom
import com.tstudioz.fax.fme.feature.timetable.models.TimeTableInfo
import com.tstudioz.fax.fme.testStuff.newTestEvents
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class MockTimeTableRepository(
    private val timeTableDao: TimeTableDao
) : TimeTableRepositoryInterface {

    private val _events: MutableSharedFlow<List<Event>> = MutableSharedFlow(1)
    override val events: SharedFlow<List<Event>> = _events.asSharedFlow()

    init {
        observeEventsFromCache()
    }

    override suspend fun fetchTimetable(
        user: String,
        startDate: String,
        endDate: String,
        shouldCache: Boolean
    ): List<Event> {
        val events = newTestEvents
        if (shouldCache) {
            insert(events)
        }
        return events
    }

    override suspend fun fetchTimeTableCalendar(
        startDate: String,
        endDate: String
    ): Map<LocalDate, TimeTableInfo> {
        return emptyMap()
    }

    private fun observeEventsFromCache() {
        CoroutineScope(Dispatchers.IO).launch {
            timeTableDao.getEventsAsync().collect { events ->
                _events.emit(events.map { Event(it) })
            }
        }
    }

    private fun insert(classes: List<Event>) {
        timeTableDao.deleteAll()
        timeTableDao.insert(classes.map { EventRoom(it) })
    }

}
