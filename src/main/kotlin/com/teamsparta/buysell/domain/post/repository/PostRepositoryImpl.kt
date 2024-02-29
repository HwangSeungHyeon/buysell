package com.teamsparta.buysell.domain.post.repository

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Expression
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.EntityPathBase
import com.querydsl.core.types.dsl.PathBuilder
import com.teamsparta.buysell.domain.post.dto.response.PostListResponse
import com.teamsparta.buysell.domain.post.model.QPost
import com.teamsparta.buysell.infra.querydsl.QueryDslSupport
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class PostRepositoryImpl : CustomPostRepository, QueryDslSupport(){
    private val post = QPost.post
    override fun searchByKeyword(
        keyword: String,
        pageable: Pageable
    ) : Page<PostListResponse> {
        val booleanBuilder = BooleanBuilder()
        booleanBuilder.and(post.title.contains(keyword))
        booleanBuilder.and(post.isDeleted.isFalse)

        val totalCount = queryFactory
            .select(post.count())
            .from(post)
            .where(booleanBuilder)
            .fetchOne() ?: 0L

        val contents = queryFactory
            .select(
                Projections.constructor(
                    PostListResponse::class.java,
                    post.id,
                    post.title,
                    post.createdName,
                    post.price
                )
            )
            .from(post)
            .where(booleanBuilder)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(*getOrderSpecifier(pageable, post))
            .fetch()

        return PageImpl(contents, pageable, totalCount)
    }
}

    private fun getOrderSpecifier(
        pageable: Pageable,
        path: EntityPathBase<*>
    ): Array<OrderSpecifier<*>>{
        val pathBuilder = PathBuilder(path.type, path.metadata)

        return pageable.sort.toList().map {
                order -> OrderSpecifier(
            if(order.isAscending) Order.ASC else Order.DESC,
            pathBuilder.get(order.property) as Expression<Comparable<*>>
        )
        }.toTypedArray()
    }