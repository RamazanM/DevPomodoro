package com.ramazanm.devpomodoro.data.dto

import kotlinx.serialization.Serializable

@Serializable
enum class TaskSourceType {
    LOCAL, JIRA, TRELLO, NOTION, FIRESTORE
}