package com.tstudioz.fax.fme.feature.attendance.repository

import com.tstudioz.fax.fme.feature.attendance.dao.AttendanceDao
import com.tstudioz.fax.fme.feature.attendance.models.AttendanceEntry
import com.tstudioz.fax.fme.feature.attendance.utils.sortedByClassAndSemester
import com.tstudioz.fax.fme.networking.NetworkServiceResult
import com.tstudioz.fax.fme.util.attendanceTestData

class MockAttendanceRepository(
    private val attendanceDao: AttendanceDao,
) : AttendanceRepositoryInterface {

    override suspend fun fetchAttendance(): NetworkServiceResult.AttendanceParseResult {
        val attendanceList: List<List<AttendanceEntry>> = attendanceTestData.groupBy { it.subject }.values.toList()
        insertAttendance(attendanceList.flatten())

        return NetworkServiceResult.AttendanceParseResult.Success(
            attendanceList.sortedByClassAndSemester()
        )
    }

    override suspend fun insertAttendance(attendance: List<AttendanceEntry>) {
        attendanceDao.deleteAll()
        attendanceDao.insert(attendance)
    }

    override suspend fun readAttendance(): List<List<AttendanceEntry>> {
        val test = attendanceDao.read()
            .groupBy { it.subject }.values
            .toList()
            .sortedByClassAndSemester()
        return test
    }

}
