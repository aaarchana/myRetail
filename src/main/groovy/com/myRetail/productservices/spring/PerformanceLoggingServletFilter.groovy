package com.myRetail.productservices.spring

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.servlet.*

@Slf4j
@CompileStatic
class PerformanceLoggingServletFilter implements Filter {

    static final ThreadLocal<PerfTimer> timer = new ThreadLocal<PerfTimer>(){
        @Override
        PerfTimer initialValue(){
            return new PerfTimer()
        }
    }

    @Override
    void init(FilterConfig filterConfig) throws ServletException {

    }

    static PerfTimer fetchTimer(){
        timer.get()
    }

    @Override
    void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        PerfTimer t = timer.get()
        t.startCall()
        chain.doFilter(request, response)
        t.stopCall()

        log.info("performanceStats=${t}")
        timer.remove()
    }

    @Override
    void destroy() {

    }



    static class PerfTimer {
        protected Date beginCall
        protected Date endCall

        protected Date beginProductApiCall
        protected Date endProductApiCall


        protected Date beginCassandraCall
        protected Date endCassandraCall

        void startCall() {
            beginCall = new Date()
        }

        void stopCall() {
            endCall = new Date()
        }

        void startProductApiCall() {
            beginProductApiCall = new Date()
        }

        void stopProuctApiCall() {
            endProductApiCall = new Date()
        }

        void startCassandraCall() {
            beginCassandraCall = new Date()
        }

        void stopCassandraCall() {
            endCassandraCall = new Date()
        }


        @Override
        String toString() {
            StringBuilder sb =new StringBuilder()
            sb.append("[totalTimeMilliSeconds=").append(endCall.time - beginCall.time)
            endProductApiCall ? sb.append(",productApiCall=").append(endProductApiCall.time - beginProductApiCall.time) : null
            endCassandraCall ? sb.append(",cassandraCall=").append(endCassandraCall.time - beginCassandraCall.time) : null
            sb.append("]")
            sb.toString()
        }

    }
}
