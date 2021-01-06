package com.vnpost.elearning.config;

import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Configuration;

import static java.util.Arrays.asList;

/**
 * @author:Nguyen Anh Tuan
 * <p>
 * June 07,2020
 */
@Configuration
@EnableCaching
public class CachingConfig implements CacheManagerCustomizer<ConcurrentMapCacheManager> {
    @Override
    public void customize(ConcurrentMapCacheManager cacheManager) {
        cacheManager.setCacheNames(asList("poscodeVnpost","chapterIds","courseCategoryAndChild"));
    }
}
