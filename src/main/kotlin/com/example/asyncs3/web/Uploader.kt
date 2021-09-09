package com.example.asyncs3.web

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import software.amazon.awssdk.core.async.AsyncRequestBody
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectResponse
import java.nio.ByteBuffer
import java.util.*
import java.util.concurrent.CompletableFuture

@Service
class Uploader(
    @Value("\${aws.s3.bucket}") private var bucket: String,
    @Value("\${aws.s3.region}") private var region: String,
    private val s3client: S3AsyncClient,
) {
    val log: Logger = LoggerFactory.getLogger(Uploader::class.java)

    fun upload(length: Long, extension: String?, file: Flux<ByteBuffer>): Mono<ResponseEntity<UploadResult>> {
        val key: UUID = UUID.randomUUID()
        log.info("Extension: {}", extension)
        val future: CompletableFuture<PutObjectResponse> = s3client
            .putObject(PutObjectRequest.builder()
                .bucket(bucket)
                .key(key.toString())
                .contentType(extension ?: MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .contentLength(length)
                .build(),
                AsyncRequestBody.fromPublisher(file)
            )
        return Mono.fromFuture(future)
            .flatMap { response ->
                log.info("response from s3 bucket {}", response.toString())
                Mono.just(ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(UploadResult(key.toString(), key, "https://$bucket.s3.$region.amazonaws.com/${key}")))
            }
    }
}