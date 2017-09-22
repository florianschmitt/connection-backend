package de.florianschmitt.service.util

import org.springframework.transaction.support.TransactionSynchronizationAdapter
import org.springframework.transaction.support.TransactionSynchronizationManager

object TransactionHook {
    fun afterCommitSuccess(work: () -> Unit) {
        TransactionSynchronizationManager.registerSynchronization(object : TransactionSynchronizationAdapter() {

            override fun afterCommit() {
                work.invoke()
            }
        })
    }
}
