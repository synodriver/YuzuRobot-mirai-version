package com.yumetsuki.repo.memory

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

abstract class Repository<RQ, RS> {

    private val mutex: Mutex = Mutex()

    protected abstract suspend fun handleRequest(request: RQ): RS

    suspend fun <T: RS> tell(request: RQ): T = mutex.withLock {
        @Suppress("UNCHECKED_CAST")
        handleRequest(request) as T
    }

}