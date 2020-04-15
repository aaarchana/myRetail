package com.myRetail.productservices.spring

import org.slf4j.MDC

import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class ProductServicesThreadExecutor extends ThreadPoolExecutor {

    ProductServicesThreadExecutor(int poolSize) {
        super(poolSize, poolSize, 0l, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>())
    }

    @Override
    void execute(Runnable task) {
        PerformanceLoggingServletFilter.PerfTimer timer = PerformanceLoggingServletFilter.timer.get()
        // get timer
        Runnable wrapped = {
            PerformanceLoggingServletFilter.timer.set(timer)
            task.run()
            PerformanceLoggingServletFilter.timer.set(null)
        }

        super.execute(wrapped)
    }

}
