package com.android.academy.fundamentals.app.workshop04

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import com.android.academy.fundamentals.app.workshop04.solution.MyWorkerSolution
import java.util.concurrent.TimeUnit

class WorkRepository {
    //Todo 4.3: Create simple OneTimeWorkRequest
    val simpleRequest = OneTimeWorkRequest.Builder(MyWorker::class.java).build()

    //Todo 4.5: Create delayedRequest
    val delayedRequest = OneTimeWorkRequest.Builder(MyWorker::class.java)
        .setInitialDelay(10L, TimeUnit.SECONDS)
        .build()

    //Todo 4.7: Create constrainedRequest
    private val constraints =
        Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
    val constrainedRequest = OneTimeWorkRequest.Builder(MyWorkerSolution::class.java)
        .setConstraints(constraints)
        .build()
}