package com.compilit.sanitization;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan("com.compilit.sanitization")
@EnableAspectJAutoProxy
class SanitizationConfiguration {
}
