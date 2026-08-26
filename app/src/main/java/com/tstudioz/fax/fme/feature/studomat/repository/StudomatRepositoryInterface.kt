package com.tstudioz.fax.fme.feature.studomat.repository

import com.tstudioz.fax.fme.feature.studomat.models.StudomatYear
import com.tstudioz.fax.fme.feature.studomat.models.StudomatYearInfo
import com.tstudioz.fax.fme.feature.studomat.repository.models.StudomatRepositoryResult

interface StudomatRepositoryInterface {

    fun getStudomatDataAndYears(): StudomatRepositoryResult.StudentAndYearsResult

    fun getYear(year: StudomatYearInfo): StudomatRepositoryResult.ChosenYearResult

    fun insert(year: StudomatYear)

    fun readData(): List<StudomatYear>
}