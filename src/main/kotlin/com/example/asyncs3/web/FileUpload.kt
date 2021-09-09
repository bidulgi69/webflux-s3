package com.example.asyncs3.web

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.nio.ByteBuffer

@RestController
@RequestMapping(value = [ "/file" ])
class FileUpload(
    private val uploader: Uploader,
) {
    val log: Logger = LoggerFactory.getLogger(FileUpload::class.java)

    @PostMapping(
        value = [ "/upload" ],
        consumes = [ MediaType.ALL_VALUE ])
    fun upload(@RequestHeader headers: HttpHeaders, @RequestBody file: Flux<ByteBuffer>): Mono<ResponseEntity<UploadResult>> {
        log.info("Upload request handler activated. {}", headers.contentLength)
        return uploader.upload(headers.contentLength, headers["file-extension"]?. first(), file)
    }
}