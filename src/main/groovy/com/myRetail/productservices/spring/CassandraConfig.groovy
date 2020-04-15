package com.myRetail.productservices.spring

import com.datastax.driver.core.*
import com.datastax.driver.core.policies.*
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@Slf4j

class CassandraConfig {

    @Value('${cassandra.keyspace}')
    String keyspace

    @Value('#{${cassandra.contactpoint}}')
    List<String> contactPoints

    @Value('${cassandra.port}')
    int port

    @Value('${cassandra.readTimeoutMilliseconds}')
    int readTimeout

    @Value('${cassandra.connectionRetryInMilliseconds}')
    long reconnect



    @Bean
    Cluster cluster() {

        Cluster.Builder builder = Cluster.builder()

        builder.addContactPointsWithPorts(contactPoints.collect { new InetSocketAddress(it, port) })
        builder.withLoadBalancingPolicy(new TokenAwarePolicy(DCAwareRoundRobinPolicy.builder().build(), true))
        builder.withReconnectionPolicy(new ConstantReconnectionPolicy(reconnect))
        builder.withRetryPolicy(new LoggingRetryPolicy(DefaultRetryPolicy.INSTANCE))
        builder.withQueryOptions(new QueryOptions().setConsistencyLevel(ConsistencyLevel.LOCAL_ONE))
        builder.withSocketOptions(new SocketOptions(readTimeoutMillis: readTimeout))      //default:  12 seconds
        //for pricingServices?:  .withRetryPolicy(new LoggingRetryPolicy(DowngradingConsistencyRetryPolicy.INSTANCE))
        //for pricingServices?: .withQueryOptions(new QueryOptions().setConsistencyLevel(ConsistencyLevel.LOCAL_ONE))

        return builder.build()
    }


    @Bean
    Session session(Cluster cluster) throws Exception {

        cluster.connect(keyspace)

    }

}