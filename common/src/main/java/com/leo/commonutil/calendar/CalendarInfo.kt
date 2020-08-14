package com.leo.commonutil.calendar

import com.leo.commonutil.callback.IProguard
import com.leo.commonutil.enume.UnitWeek

data class CalendarInfo(var year: Int, var month: Int, var day: Int, @param:UnitWeek
var week: Int) : IProguard
