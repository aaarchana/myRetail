package com.myRetail.productservices.spring

import net.sf.ehcache.Cache
import net.sf.ehcache.CacheManager
import net.sf.ehcache.config.CacheConfiguration
import net.sf.ehcache.config.Configuration
import net.sf.ehcache.config.PersistenceConfiguration
import net.sf.ehcache.management.ManagementService
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.ehcache.EhCacheCacheManager
import org.springframework.context.annotation.Bean

import javax.management.MBeanServer
import java.lang.management.ManagementFactory

@EnableCaching
class CacheConfig {

    static final String PRODUCT_CACHE = 'productData'


    @Bean(destroyMethod = "shutdown")
    public CacheManager cacheManager() {

        Cache productData = new Cache(
                new CacheConfiguration(PRODUCT_CACHE, 10000)
                        .eternal(false)
                        .memoryStoreEvictionPolicy("LFU")
                        .timeToLiveSeconds(60)
                        .persistence(new PersistenceConfiguration().strategy(PersistenceConfiguration.Strategy.NONE)))


        Configuration managerConf = new Configuration()
        CacheManager manager = CacheManager.create(managerConf)
        manager.addCache(productData)
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer()
        ManagementService.registerMBeans(manager, mBeanServer, false, true, true, true)
        return manager

    }

    @Bean
    EhCacheCacheManager getCacheManager(CacheManager cacheManager) {
        EhCacheCacheManager ehCacheCacheManager = new EhCacheCacheManager(cacheManager)
        ehCacheCacheManager.afterPropertiesSet()
        return ehCacheCacheManager
    }

}
