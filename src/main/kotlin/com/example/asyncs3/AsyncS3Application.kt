package com.example.asyncs3

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AsyncS3Application

fun main(args: Array<String>) {
    runApplication<AsyncS3Application>(*args)
}
