package com.ramazanm.devpomodoro

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform