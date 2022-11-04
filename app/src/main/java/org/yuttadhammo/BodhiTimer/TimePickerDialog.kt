/*
    This file is part of Bodhi Timer.

    Bodhi Timer is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Bodhi Timer is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Bodhi Timer.  If not, see <http://www.gnu.org/licenses/>.
*/
package org.yuttadhammo.BodhiTimer

import android.content.Context
import android.view.View
import android.widget.TimePicker

/**
 * Dialog box with an arbitrary number of number pickers
 *
 * THIS IS AN UNUSED EXPERIMENT!
 */
class TimePickerDialog(context: Context) : SlidingPickerDialog(context) {
    override val layout: Int = R.layout.timepicker_dialog

    private var timePicker: TimePicker? = null

    override fun setupTimePicker() {
        // Time Picker
        timePicker = findViewById(R.id.timePicker)
        timePicker!!.setIs24HourView(true)
        setHour(mTimes)
        setMinute(mTimes)
    }

    /**
     * {@inheritDoc}
     */
    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnOk -> {
                val hsel = hour
                val msel = minute
                val ssel = 0
                val values = intArrayOf(hsel, msel, ssel)
                returnResults(values)
            }
            R.id.btnCancel -> dismiss()
            R.id.btn1 -> setFromPreset(i1)
            R.id.btn2 -> setFromPreset(i2)
            R.id.btn3 -> setFromPreset(i3)
            R.id.btn4 -> setFromPreset(i4)
            R.id.btnadv -> setFromAdv()
        }
    }


    override fun getStringFromUI(): String {
        var h = hour.toString() + ""
        var m = minute.toString() + ""
        if (h.length == 1) h = "0$h"
        if (m.length == 1) m = "0$m"

        return "$h:$m"
    }


    // Helper functions
    private fun setMinute(times: IntArray?) {
        timePicker!!.minute = times!![1]
    }

    private fun setHour(times: IntArray?) {
        timePicker!!.hour = times!![0]
    }

    private val minute: Int
        get() = timePicker!!.minute

    private val hour: Int
        get() = timePicker!!.hour
}