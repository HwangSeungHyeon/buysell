package com.teamsparta.buysell.domain.exception

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "데이터베이스에 id 값에 해당하는 레코드가 없을 때 에러 문구를 전달하는 객체.")
data class ModelNotFoundException(val modelName: String, val id: Int?) :
    RuntimeException("Model $modelName not found with given id: $id")