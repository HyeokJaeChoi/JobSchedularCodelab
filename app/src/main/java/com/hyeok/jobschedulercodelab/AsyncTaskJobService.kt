package com.hyeok.jobschedulercodelab

import android.app.job.JobParameters
import android.app.job.JobService
import android.os.AsyncTask
import android.widget.Toast
import java.lang.ref.WeakReference

class AsyncTaskJobService: JobService() {

    private val jobStopCheckAsyncTask by lazy { WeakReference<JobStopCheckAsyncTask>(JobStopCheckAsyncTask()) }

    override fun onStartJob(p0: JobParameters?): Boolean {
        p0?.let {
            jobStopCheckAsyncTask.get()?.execute(it)
        }
        return true
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        Toast.makeText(this, "The job is stopped!", Toast.LENGTH_SHORT).show()
        return true
    }

    inner class JobStopCheckAsyncTask: AsyncTask<JobParameters, Void, Void?>() {
        override fun doInBackground(vararg p0: JobParameters?): Void? {
            p0[0]?.let {
                Thread.sleep(5000L)
                jobFinished(it, false)
            }
            return null
        }
    }
}