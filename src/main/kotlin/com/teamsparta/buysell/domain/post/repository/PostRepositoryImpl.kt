package com.teamsparta.buysell.domain.post.repository

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Expression
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.EntityPathBase
import com.querydsl.core.types.dsl.PathBuilder
import com.teamsparta.buysell.domain.member.model.QMember
import com.teamsparta.buysell.domain.post.dto.response.PostListResponse
import com.teamsparta.buysell.domain.post.model.Category
import com.teamsparta.buysell.domain.post.model.QPost
import com.teamsparta.buysell.infra.querydsl.QueryDslSupport
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class PostRepositoryImpl : CustomPostRepository, QueryDslSupport(){
    private val post = QPost.post
    override fun getPostsWithPagination(
        category: Category?,
        pageable: Pageable
    ) : Page<PostListResponse> {

        // 삭제된 게시글은 조회가 되면 안됨
        // 카테고리가 선택되었다면 그 카테고리를 가진 게시글만 조회가 되어야 됨

        val booleanBuilder = BooleanBuilder()
        booleanBuilder
//            .and(post.isDeleted.isFalse)
            .andAnyOf(
                category?.let { post.category.eq(it) }
            )

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
}