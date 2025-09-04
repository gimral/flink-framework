package com.gimral.streaming.core.configuration;

public class FlinkOptions {
    private static String jobName;

    public static String getJobName() {
        return jobName;
    }

    public static void setJobName(String jobName) {
        FlinkOptions.jobName = jobName;
    }
}
