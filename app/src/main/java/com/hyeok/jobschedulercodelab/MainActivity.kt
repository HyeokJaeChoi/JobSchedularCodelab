package com.hyeok.jobschedulercodelab

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.getSystemService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val jobScheduler by lazy { getSystemService<JobScheduler>() }
    private val JOB_ID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scheduleJobBtn.setOnClickListener(this)
        cancelJobBtn.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when(it.id) {
                R.id.scheduleJobBtn -> {
                    scheduleJob()
                }
                R.id.cancelJobBtn -> {
                    cancelJob()
                }
            }
        }
    }

    private fun scheduleJob() {
        val selectedNetworkId = networkOptions.checkedRadioButtonId
        val selectedNetworkOption = when(selectedNetworkId) {
            R.id.noNetwork -> {
                JobInfo.NETWORK_TYPE_NONE
            }
            R.id.anyNetwork -> {
                JobInfo.NETWORK_TYPE_ANY
            }
            R.id.wifiNetwork -> {
                JobInfo.NETWORK_TYPE_UNMETERED
            }
            else -> {
                JobInfo.NETWORK_TYPE_NONE
            }
        }
        val componentName = ComponentName(packageName, NotificationJobService::class.java.name)
        val jobInfoBuilder = JobInfo.Builder(JOB_ID, componentName)
            .setRequiredNetworkType(selectedNetworkOption)
        val constraintSet = selectedNetworkOption != JobInfo.NETWORK_TYPE_NONE

        if(constraintSet) {
            val jobInfo = jobInfoBuilder.build()
            jobScheduler?.schedule(jobInfo)
            Toast.makeText(this, "Job Scheduled, job will run when the constraints are met.", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(this, "Please set at least one constraint", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cancelJob() {
        jobScheduler?.cancelAll()
        Toast.makeText(this, "Jobs cancelled", Toast.LENGTH_SHORT).show()
    }
}
