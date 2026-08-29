package com.tstudioz.fax.fme.feature.studomat.repository

import com.tstudioz.fax.fme.feature.studomat.dao.StudomatDao
import com.tstudioz.fax.fme.feature.studomat.data.sortedByNameAndSemester
import com.tstudioz.fax.fme.feature.studomat.models.StudomatYear
import com.tstudioz.fax.fme.feature.studomat.models.StudomatYearInfo
import com.tstudioz.fax.fme.feature.studomat.repository.models.StudomatRepositoryResult
import com.tstudioz.fax.fme.testStuff.studomatStudentData
import com.tstudioz.fax.fme.testStuff.studomatSubjectTestData
import com.tstudioz.fax.fme.testStuff.studomatYearInfoTestData

class MockStudomatRepository(
    private val studomatDao: StudomatDao,
) : StudomatRepositoryInterface {

    override fun getStudomatDataAndYears(): StudomatRepositoryResult.StudentAndYearsResult {
        return StudomatRepositoryResult.StudentAndYearsResult.Success(studomatYearInfoTestData, studomatStudentData)
    }

    override fun getYear(year: StudomatYearInfo): StudomatRepositoryResult.ChosenYearResult {
        return StudomatRepositoryResult.ChosenYearResult.Success(
            Pair(
                year,
                studomatSubjectTestData.filter { it.course == year.courseName })
        )

    }

    override fun insert(year: StudomatYear) {
        year.subjects.firstOrNull()?.year?.let { studomatDao.deleteAll(it) }
        studomatDao.insert(year.subjects)
        studomatDao.insertYears(listOf(year.yearInfo))
    }

    override fun readData(): List<StudomatYear> {
        val years = studomatDao.readYears().sortedBy { it.academicYear }
        val subjects = studomatDao.read().sortedByNameAndSemester().groupBy { it.year }
        return years.mapNotNull { yearInfo ->
            subjects[yearInfo.academicYear]?.let { subjectsForYearAndCourse ->
                StudomatYear(yearInfo, subjectsForYearAndCourse)
            }
        }
    }
}