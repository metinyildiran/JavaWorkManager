package com.example.javaworkmanager;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Data data = new Data.Builder().putInt("intKey", 2).build();

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .build();

//        WorkRequest workRequest = new OneTimeWorkRequest.Builder(RefreshDatabase.class)
//                .setConstraints(constraints)
//                .setInputData(data)
//                .setInitialDelay(100, TimeUnit.MILLISECONDS)
//                .addTag("myTag")
//                .build();

        WorkRequest workRequest = new PeriodicWorkRequest.Builder(RefreshDatabase.class, 15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .setInputData(data)
                .build();

        WorkManager.getInstance(this).enqueue(workRequest);

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(workRequest.getId()).observe(this, workInfo -> {
            if (workInfo.getState() == WorkInfo.State.ENQUEUED) {
                System.out.println("Enqueued");
            } else if (workInfo.getState() == WorkInfo.State.RUNNING) {
                System.out.println("Running");
            } else if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                System.out.println("Succeeded");
            } else if (workInfo.getState() == WorkInfo.State.FAILED) {
                System.out.println("Failed");
            }
        });

//        WorkManager.getInstance(this).cancelAllWork();

//        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(RefreshDatabase.class)
//                .setInputData(data)
//                .setConstraints(constraints)
//                .build();
//
//        WorkManager.getInstance(this).beginWith(oneTimeWorkRequest).then(oneTimeWorkRequest).then(oneTimeWorkRequest).enqueue();
    }
}