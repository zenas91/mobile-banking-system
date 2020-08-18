package com.proemion.machine.mobilebanking.model

data class AccountItem(
    val account: List<AccountX>,
    val dateCreated: Long,
    val dateModified: Long,
    val dob: Long,
    val firstName: String,
    val id: String,
    val lastName: String,
    val password: String,
    val username: String
)