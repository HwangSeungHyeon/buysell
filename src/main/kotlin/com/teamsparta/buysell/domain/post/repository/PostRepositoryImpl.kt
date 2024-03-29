package com.teamsparta.buysell.domain.post.repository

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Expression
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.EntityPathBase
import com.querydsl.core.types.dsl.PathBuilder
import com.teamsparta.buysell.domain.post.dto.response.PostListResponse
import com.teamsparta.buysell.domain.post.model.Category
import com.teamsparta.buysell.domain.post.model.QPost
import com.teamsparta.buysell.infra.querydsl.QueryDslSupport
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class PostRepositoryImpl : CustomPostRepository, QueryDslSupport() {
    private val post = QPost.post

    //게시글 목록을 가져올 때 Pagination 을 적용
    //카테고리를 지정하지 않으면 기존 Pagination 과 동일
    //카테고리를 지정하면, 그 카테고리에 해당하는 게시글만 조회
    override fun getPostsWithPagination(
        category: Category?,
        pageable: Pageable
    ): Page<PostListResponse> {

        // 카테고리가 선택되었다면 그 카테고리를 가진 게시글만 조회가 되어야 됨
        val booleanBuilder = initBooleanBuilder()
        booleanBuilder.andAnyOf(eqCategory(category))

        val totalCount = getTotalPageCount(booleanBuilder)
        val contents = getPostsContents(booleanBuilder, pageable)

        return PageImpl(contents, pageable, totalCount)
    }

    //키워드를 입력했을 때, 제목에 해당 키워드가 포함되어 있다면
    //그 게시글만 조회하는 메서드
    override fun searchByKeyword(
        keyword: String,
        pageable: Pageable
    ): Page<PostListResponse> {

        // 사용자가 입력한 검색어를 제목에 포함한 게시글만 조회되어야 함
        val booleanBuilder = initBooleanBuilder()
        booleanBuilder.andAnyOf(eqTitle(keyword))

        val totalCount = getTotalPageCount(booleanBuilder)
        val contents = getPostsContents(booleanBuilder, pageable)

        return PageImpl(contents, pageable, totalCount)
    }

    private fun getOrderSpecifier(
        pageable: Pageable,
        path: EntityPathBase<*>
    ): Array<OrderSpecifier<*>> {
        val pathBuilder = PathBuilder(path.type, path.metadata)

        return pageable.sort.toList().map { order ->
            OrderSpecifier(
                if (order.isAscending) Order.ASC else Order.DESC,
                pathBuilder.get(order.property) as Expression<Comparable<*>>
            )
        }.toTypedArray()
    }

    //해당하는 게시글 개수를 알아오는 메서드
    //시간이 오래 걸릴 것으로 예상됨
    private fun getTotalPageCount(
        booleanBuilder: BooleanBuilder
    ): Long {
        return queryFactory
            .select(post.count())
            .from(post)
            .where(booleanBuilder)
            .fetchOne() ?: 0L
    }

    //진짜로 게시글을 Select 할 때 사용하는 메서드
    //Projection 을 적용하여 DTO 로 전달
    private fun getPostsContents(
        booleanBuilder: BooleanBuilder,
        pageable: Pageable
    ): MutableList<PostListResponse> {
        return queryFactory
            .select(
                Projections.constructor(
                    PostListResponse::class.java,
                    post.id,
                    post.title,
                    post.member.nickname,
                    post.price,
                    post.createdAt,
                    post.updatedAt,
                    post.view,
                    post.imageUrl,
                    post.isSoldOut
                )
            )
            .from(post)
            .where(booleanBuilder)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(*getOrderSpecifier(pageable, post))
            .fetch()
    }

    //BooleanBuilder 를 만들고, 초기 공통값을 세팅하는 메서드
    private fun initBooleanBuilder(): BooleanBuilder {
        return BooleanBuilder()
    }

    //카테고리를 선택하였을 때, 그 카테고리 관련 게시글만 조회하도록 하는 BooleanExpression
    private fun eqCategory(category: Category?) = category?.let { post.category.eq(it) }

    //키워드를 입력했을 때, 키워드를 포함한 게시글 제목만 조회하도록 하는 BooleanExpression
    private fun eqTitle(keyword: String) = keyword.let { post.title.contains(it) }

}