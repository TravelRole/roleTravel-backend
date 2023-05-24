package com.travel.role.global.config.aws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;

@Configuration
public class AWSSESConfig {

	@Value("${cloud.aws.ses.access-key}")
	private String accessKey;

	@Value("${cloud.aws.ses.secret-key")
	private String secretKey;

	@Bean
	public AmazonSimpleEmailService amazonSimpleEmailService() {
		final BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey);
		final AWSStaticCredentialsProvider awsStaticCredentialsProvider = new AWSStaticCredentialsProvider(basicAWSCredentials);

		return AmazonSimpleEmailServiceClientBuilder.standard()
			.withCredentials(awsStaticCredentialsProvider)
			.withRegion(Regions.AP_NORTHEAST_2)
			.build();
	}
}
