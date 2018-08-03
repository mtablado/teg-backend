package com.eg.tracker.conf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import com.eg.tracker.security.oauth2.converter.PreAuthenticatedAuthenticationTokenReadConverter;
import com.eg.tracker.security.oauth2.converter.UsernamePasswordAuthenticationTokenReadConverter;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

@Configuration
public class MongoConfig extends AbstractMongoConfiguration {
    @Value("${spring.data.mongodb.uri}")
    private String uri;

    @Override
    public String getMappingBasePackage() {
        return "com.eg.tracker.security.oauth2.converter";
    }

    @Override
	protected Collection<String> getMappingBasePackages() {
		return Collections.singleton(this.getMappingBasePackage());
	}

    @Override
    public CustomConversions customConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new UsernamePasswordAuthenticationTokenReadConverter());
        converters.add(new PreAuthenticatedAuthenticationTokenReadConverter());
        return new MongoCustomConversions(converters);
    }

	@Override
	public MongoClient mongoClient() {
		MongoClientURI _uri = new MongoClientURI(this.uri);
		return new MongoClient(_uri);

	}

	@Override
	protected String getDatabaseName() {
		return "transport-tracker";
	}
}
