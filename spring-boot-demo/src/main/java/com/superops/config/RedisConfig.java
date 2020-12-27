package com.superops.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
@ComponentScan("com.superops")
public class RedisConfig {

//Registering the connection Factory
	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
		return new JedisConnectionFactory();
	}

//    Registering StringRedisTemplate Component
	@Bean
	public StringRedisTemplate stringRedisTemplate() {
		StringRedisTemplate stringRedisTemplate = new StringRedisTemplate(jedisConnectionFactory());
		stringRedisTemplate.setEnableTransactionSupport(true);
		return stringRedisTemplate;
	}
}
