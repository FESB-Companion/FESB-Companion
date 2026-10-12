package com.tstudioz.fax.fme.testStuff

import com.tstudioz.fax.fme.feature.timetable.models.Event
import com.tstudioz.fax.fme.feature.timetable.models.Recurring
import com.tstudioz.fax.fme.feature.timetable.models.TimetableType
import com.tstudioz.fax.fme.theme.accentBlue
import com.tstudioz.fax.fme.theme.accentGreen
import com.tstudioz.fax.fme.theme.accentRed
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime

// Monday of this week
private val thisWeekMonday: LocalDate = LocalDate.now()
    .with(DayOfWeek.MONDAY)

private fun thisWeekDate(dayOfWeek: DayOfWeek, hour: Int, minute: Int): LocalDateTime {
    return thisWeekMonday.with(dayOfWeek).atTime(hour, minute)
}

val testEvents = listOf(
    Event(
        id = "532059",
        name = "Kriptografija i mrežna sigurnost",
        shortName = "KIMS",
        color = accentBlue,
        colorId = 2131099687,
        professor = "Čagalj Mario",
        eventType = TimetableType.PREDAVANJE,
        groups = "",
        classroom = "C501",
        start = thisWeekDate(DayOfWeek.MONDAY, 10, 15),
        end = thisWeekDate(DayOfWeek.MONDAY, 12, 0),
        description = "C501"
    ),
    Event(
        id = "534198",
        name = "Metode optimizacije",
        shortName = "MO",
        color = accentGreen,
        colorId = 2131100480,
        professor = "Bašić Martina",
        eventType = TimetableType.LABORATORIJSKA_VJEZBA,
        groups = "Grupa 1,",
        classroom = "B420",
        start = thisWeekDate(DayOfWeek.MONDAY, 18, 30),
        end = thisWeekDate(DayOfWeek.MONDAY, 20, 0),
        description = "B420"
    ),
    Event(
        id = "532144",
        name = "Podržano strojno učenje",
        shortName = "PSU",
        color = accentBlue,
        colorId = 2131099687,
        professor = "Vasilj Josip",
        eventType = TimetableType.PREDAVANJE,
        groups = "",
        classroom = "A243",
        start = thisWeekDate(DayOfWeek.TUESDAY, 8, 15),
        end = thisWeekDate(DayOfWeek.TUESDAY, 10, 0),
        description = "A243"
    ),
    Event(
        id = "532084",
        name = "Metode optimizacije",
        shortName = "MO",
        color = accentBlue,
        colorId = 2131099687,
        professor = "Marasović Jadranka",
        eventType = TimetableType.PREDAVANJE,
        groups = "",
        classroom = "C502",
        start = thisWeekDate(DayOfWeek.TUESDAY, 10, 15),
        end = thisWeekDate(DayOfWeek.TUESDAY, 12, 0),
        description = "C502"
    ),
    Event(
        id = "532120",
        name = "IP komunikacije",
        shortName = "IK",
        color = accentBlue,
        colorId = 2131099687,
        professor = "Russo Mladen",
        eventType = TimetableType.PREDAVANJE,
        groups = "",
        classroom = "A105",
        start = thisWeekDate(DayOfWeek.TUESDAY, 12, 15),
        end = thisWeekDate(DayOfWeek.TUESDAY, 14, 0),
        description = "A105"
    ),
    Event(
        id = "538989",
        name = "Podržano strojno učenje",
        shortName = "PSU",
        color = accentGreen,
        colorId = 2131100480,
        professor = "Vasilj Josip",
        eventType = TimetableType.LABORATORIJSKA_VJEZBA,
        groups = "Grupa 1,",
        classroom = "A507",
        start = thisWeekDate(DayOfWeek.THURSDAY, 10, 0),
        end = thisWeekDate(DayOfWeek.THURSDAY, 12, 15),
        description = "A507"
    ),
    Event(
        id = "535595",
        name = "Jezici i prevoditelji",
        shortName = "JIP",
        color = accentGreen,
        colorId = 2131100480,
        professor = "Sikora Marjan",
        eventType = TimetableType.LABORATORIJSKA_VJEZBA,
        groups = "Grupa 1,",
        classroom = "B526",
        start = thisWeekDate(DayOfWeek.THURSDAY, 8, 30),
        end = thisWeekDate(DayOfWeek.THURSDAY, 10, 0),
        description = "B526"
    ),
    Event(
        id = "535336",
        name = "IP komunikacije",
        shortName = "IK",
        color = accentGreen,
        colorId = 2131100480,
        professor = "Meter Davor",
        eventType = TimetableType.LABORATORIJSKA_VJEZBA,
        groups = "Grupa 1,",
        classroom = "B526",
        start = thisWeekDate(DayOfWeek.FRIDAY, 8, 0),
        end = thisWeekDate(DayOfWeek.FRIDAY, 9, 30),
        description = "B526"
    ),
)

val newTestEvents = listOf(
    Event(
        id = "545864",
        name = "Grid računalni sustavi",
        shortName = "GRS",
        color = accentBlue,
        colorId = -14043402,
        professor = "Pleština Vladimir",
        eventType = TimetableType.PREDAVANJE,
        groups = "",
        classroom = "B401",
        start = thisWeekDate(DayOfWeek.MONDAY, 14, 15),
        end = thisWeekDate(DayOfWeek.MONDAY, 16, 0),
        description = "ponedjeljak, 20.1.2025. 14:15 - 16:00 (2 sata)",
        recurring = true,
        recurringType = Recurring.WEEKLY,
        recurringUntil = "do 26.1.2025.",
        studyCode = "(250)"
    ),

    Event(
        id = "545738",
        name = "Multimedijski sustavi",
        shortName = "MS",
        color = accentBlue,
        colorId = -14043402,
        professor = "Russo Mladen",
        eventType = TimetableType.PREDAVANJE,
        groups = "",
        classroom = "B401",
        start = thisWeekDate(DayOfWeek.MONDAY, 16, 15),
        end = thisWeekDate(DayOfWeek.MONDAY, 18, 0),
        description = "ponedjeljak, 20.1.2025. 16:15 - 18:00 (2 sata)",
        recurring = true,
        recurringType = Recurring.WEEKLY,
        recurringUntil = "do 26.1.2025.",
        studyCode = "(250)"
    ),

    Event(
        id = "554366",
        name = "Sigurnost bežičnih mreža",
        shortName = "SBM",
        color = accentRed,
        colorId = -44462,
        professor = "Šabić Josip",
        eventType = TimetableType.LABORATORIJSKA_VJEZBA,
        groups = "Grupa 1",
        classroom = "A507",
        start = thisWeekDate(DayOfWeek.TUESDAY, 10, 30),
        end = thisWeekDate(DayOfWeek.TUESDAY, 12, 0),
        description = "utorak, 21.1.2025. 10:30 - 12:00 (2 sata)",
        recurring = true,
        recurringType = Recurring.WEEKLY,
        recurringUntil = "do 28.1.2025.",
        studyCode = "(250)"
    ),

    Event(
        id = "557403",
        name = "Poslovni informacijski sustavi",
        shortName = "PIS",
        color = accentRed,
        colorId = -44462,
        professor = "Čelar Stipo",
        eventType = TimetableType.LABORATORIJSKA_VJEZBA,
        groups = "Grupa 3",
        classroom = "B523",
        start = thisWeekDate(DayOfWeek.TUESDAY, 18, 0),
        end = thisWeekDate(DayOfWeek.TUESDAY, 19, 30),
        description = "utorak, 21.1.2025. 18:00 - 19:30 (2 sata)",
        recurring = true,
        recurringType = Recurring.WEEKLY,
        recurringUntil = "do 24.1.2025.",
        studyCode = "(250)"
    ),

    Event(
        id = "558997",
        name = "Ugradbeni računalni sustavi",
        shortName = "URS",
        color = accentBlue,
        colorId = -14043402,
        professor = "Gotovac Sven",
        eventType = TimetableType.PREDAVANJE,
        groups = "",
        classroom = "B523",
        start = thisWeekDate(DayOfWeek.WEDNESDAY, 11, 15),
        end = thisWeekDate(DayOfWeek.WEDNESDAY, 14, 0),
        description = "srijeda, 22.1.2025. 11:15 - 14:00 (3 sata)",
        recurring = false,
        recurringType = Recurring.ONCE,
        recurringUntil = "",
        studyCode = "(250)"
    ),

    Event(
        id = "545953",
        name = "Paralelno programiranje",
        shortName = "PP",
        color = accentBlue,
        colorId = -14043402,
        professor = "Krstinić Damir",
        eventType = TimetableType.PREDAVANJE,
        groups = "",
        classroom = "A101",
        start = thisWeekDate(DayOfWeek.WEDNESDAY, 13, 15),
        end = thisWeekDate(DayOfWeek.WEDNESDAY, 15, 0),
        description = "srijeda, 22.1.2025. 13:15 - 15:00 (2 sata)",
        recurring = true,
        recurringType = Recurring.WEEKLY,
        recurringUntil = "do 26.1.2025.",
        studyCode = "(250)"
    ),

    Event(
        id = "545877",
        name = "Grid računalni sustavi",
        shortName = "GRS",
        color = accentGreen,
        colorId = -9240667,
        professor = "Pleština Vladimir",
        eventType = TimetableType.AUDITORNA_VJEZBA,
        groups = "",
        classroom = "C501",
        start = thisWeekDate(DayOfWeek.WEDNESDAY, 15, 15),
        end = thisWeekDate(DayOfWeek.WEDNESDAY, 17, 0),
        description = "srijeda, 22.1.2025. 15:15 - 17:00 (2 sata)",
        recurring = true,
        recurringType = Recurring.WEEKLY,
        recurringUntil = "do 26.1.2025.",
        studyCode = "(250)"
    ),

    Event(
        id = "551763",
        name = "Paralelno programiranje",
        shortName = "PP",
        color = accentRed,
        colorId = -44462,
        professor = "Ivanda Antonia",
        eventType = TimetableType.LABORATORIJSKA_VJEZBA,
        groups = "Grupa 2",
        classroom = "B526",
        start = thisWeekDate(DayOfWeek.THURSDAY, 9, 30),
        end = thisWeekDate(DayOfWeek.THURSDAY, 11, 0),
        description = "četvrtak, 23.1.2025. 9:30 - 11:00 (2 sata)",
        recurring = true,
        recurringType = Recurring.WEEKLY,
        recurringUntil = "do 24.1.2025.",
        studyCode = "(250)"
    ),

    Event(
        id = "558284",
        name = "Sigurnost bežičnih mreža",
        shortName = "SBM",
        color = accentBlue,
        colorId = -14043402,
        professor = "Perković Toni",
        eventType = TimetableType.PREDAVANJE,
        groups = "",
        classroom = "C401",
        start = thisWeekDate(DayOfWeek.THURSDAY, 11, 15),
        end = thisWeekDate(DayOfWeek.THURSDAY, 13, 0),
        description = "četvrtak, 23.1.2025. 11:15 - 13:00 (2 sata)",
        recurring = false,
        recurringType = Recurring.ONCE,
        recurringUntil = "",
        studyCode = "(250)"
    ),

    Event(
        id = "559058",
        name = "Sigurnost bežičnih mreža",
        shortName = "SBM",
        color = accentBlue,
        colorId = -14043402,
        professor = "Perković Toni",
        eventType = TimetableType.PREDAVANJE,
        groups = "",
        classroom = "A507",
        start = thisWeekDate(DayOfWeek.THURSDAY, 14, 15),
        end = thisWeekDate(DayOfWeek.THURSDAY, 15, 0),
        description = "četvrtak, 23.1.2025. 14:15 - 15:00 (1 sat)",
        recurring = false,
        recurringType = Recurring.ONCE,
        recurringUntil = "",
        studyCode = "(250)"
    ),

    Event(
        id = "545890",
        name = "Poslovni informacijski sustavi",
        shortName = "PIS",
        color = accentBlue,
        colorId = -14043402,
        professor = "Čelar Stipo",
        eventType = TimetableType.PREDAVANJE,
        groups = "",
        classroom = "B402",
        start = thisWeekDate(DayOfWeek.THURSDAY, 15, 15),
        end = thisWeekDate(DayOfWeek.THURSDAY, 17, 0),
        description = "četvrtak, 23.1.2025. 15:15 - 17:00 (2 sata)",
        recurring = true,
        recurringType = Recurring.WEEKLY,
        recurringUntil = "do 26.1.2025.",
        studyCode = "(250)"
    ),

    Event(
        id = "551619",
        name = "Multimedijski sustavi",
        shortName = "MS",
        color = accentRed,
        colorId = -44462,
        professor = "Bašić Martina",
        eventType = TimetableType.LABORATORIJSKA_VJEZBA,
        groups = "Grupa 2",
        classroom = "B526",
        start = thisWeekDate(DayOfWeek.THURSDAY, 18, 30),
        end = thisWeekDate(DayOfWeek.THURSDAY, 20, 0),
        description = "četvrtak, 23.1.2025. 18:30 - 20:00 (2 sata)",
        recurring = true,
        recurringType = Recurring.WEEKLY,
        recurringUntil = "do 24.1.2025.",
        studyCode = "(250)"
    ),
    Event(
        id = "551619",
        name = "Multimedijski sustavi",
        shortName = "MS",
        color = accentRed,
        colorId = -44462,
        professor = "Bašić Martina",
        eventType = TimetableType.LABORATORIJSKA_VJEZBA,
        groups = "Grupa 2",
        classroom = "B526",
        start = thisWeekDate(DayOfWeek.FRIDAY, 15, 30),
        end = thisWeekDate(DayOfWeek.FRIDAY, 17, 0),
        description = "četvrtak, 23.1.2025. 18:30 - 20:00 (2 sata)",
        recurring = true,
        recurringType = Recurring.WEEKLY,
        recurringUntil = "do 24.1.2025.",
        studyCode = "(250)"
    )
)
