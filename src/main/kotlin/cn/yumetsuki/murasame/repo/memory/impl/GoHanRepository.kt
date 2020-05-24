package cn.yumetsuki.murasame.repo.memory.impl

import com.yumetsuki.repo.memory.Repository
import java.time.LocalDateTime

class GoHanRepository: Repository<GoHanRepository.Request, GoHanRepository.Response>() {

    //(userId, groupId) -> recordTime
    private val goHanRecord: HashMap<Pair<Long, Long>, LocalDateTime> = hashMapOf()

    override suspend fun handleRequest(request: Request): Response {
        return when(request) {
            is Request.GoHanRecordRequest -> {
                recordGoHan(request)
            }
        }
    }

    private fun recordGoHan(goHanRecordRequest: Request.GoHanRecordRequest): Response.GoHanRecordResponse {
        return goHanRecord[goHanRecordRequest.run { userId to groupId }]?.takeIf {
            it.toLocalDate().isEqual(goHanRecordRequest.goHanTime.toLocalDate())
        }?.let {
            Response.GoHanRecordResponse(false)
        }?:run {
            goHanRecord[goHanRecordRequest.run { userId to groupId }] = goHanRecordRequest.goHanTime
            Response.GoHanRecordResponse(true)
        }
    }

    sealed class Request {
        class GoHanRecordRequest(
            val userId: Long,
            val groupId: Long,
            val goHanTime: LocalDateTime
        ): Request()
    }

    sealed class Response {
        class GoHanRecordResponse(
            val success: Boolean
        ): Response()
    }

}