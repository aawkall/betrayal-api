package com.github.aawkall.betrayalapi.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import com.github.aawkall.betrayalapi.service.BetrayalServiceImpl;

@Configuration
@ComponentScan(basePackages = {"com.github.aawkall.betrayalapi.service"},
		excludeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = BetrayalServiceImpl.class))
public class TestContextNoDb {

}