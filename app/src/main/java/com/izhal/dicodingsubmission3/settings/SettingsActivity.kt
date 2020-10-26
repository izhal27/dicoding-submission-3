package com.izhal.dicodingsubmission3.settings

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.izhal.dicodingsubmission3.R
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_settings)

    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.title = "Settings"

    val sharedPref = applicationContext.getSharedPreferences(
      getString(R.string.preference_file_key),
      Context.MODE_PRIVATE
    )
    val editor = sharedPref.edit()
    val alarmReceiver = AlarmReceiver()

    val statusReminder = sharedPref.getBoolean(getString(R.string.status_reminder), false)
    switchReminder.isChecked = statusReminder

    switchReminder.setOnCheckedChangeListener { _, isChecked ->
      editor.putBoolean(getString(R.string.status_reminder), isChecked)
      editor.apply()

      if (!isChecked) {
        if (alarmReceiver.isAlarmSet(this)) {
          alarmReceiver.cancelAlarm(this)
        }
      } else {
        alarmReceiver.setRepeatingAlarm(this, AlarmReceiver.TYPE_REPEATING)
      }
    }
  }

  override fun onSupportNavigateUp(): Boolean {
    onBackPressed()
    return super.onSupportNavigateUp()
  }
}