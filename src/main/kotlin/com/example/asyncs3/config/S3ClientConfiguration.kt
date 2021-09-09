package com.example.asyncs3.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.S3Configuration
import java.time.Duration


@Configuration
class S3ClientConfiguration(
    @Value("\${aws.s3.accessKeyId}") private var accessKeyId: String,
    @Value("\${aws.s3.secretAccessKey}") private var secretAccessKey: String,
    @Value("\${aws.s3.region}") private var region: String,
) {

    @Bean
    fun s3client(credentialsProvider: AwsCredentialsProvider): S3AsyncClient {
        val httpClient = NettyNioAsyncHttpClient.builder()
            .writeTimeout(Duration.ZERO)
            .maxConcurrency(64)
            .build()

        val serviceConfiguration = S3Configuration.builder()
            .checksumValidationEnabled(false)
            .chunkedEncodingEnabled(true)
            .build()

        return S3AsyncClient.builder().httpClient(httpClient)
            .region(Region.of(region))
            .credentialsProvider(credentialsProvider)
            .serviceConfiguration(serviceConfiguration)
            .build()
    }

    @Bean
    fun awsCredentialsProvider(): AwsCredentialsProvider {
        return AwsCredentialsProvider {
            AwsBasicCredentials.create(
                accessKeyId, secretAccessKey
            )
        }
    }
}