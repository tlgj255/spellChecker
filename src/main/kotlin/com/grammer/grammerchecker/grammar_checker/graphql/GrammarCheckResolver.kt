package com.grammer.grammerchecker.grammar_checker.graphql

import com.expediagroup.graphql.spring.operations.Query
import com.grammer.grammerchecker.grammar_checker.GrammarDto
import com.grammer.grammerchecker.grammar_checker.LogDto
import com.grammer.grammerchecker.grammar_checker.WordLogRepository
import com.grammer.grammerchecker.utils.GrammarChecker
import graphql.GraphQLException
import kotlinx.coroutines.reactive.awaitFirst
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class GrammarCheckResolver(
    private val repository: WordLogRepository,
    private val checker: GrammarChecker
) : Query {

    suspend fun check(text: String): GrammarDto =
        checker.checkGrammar(text)
            .collectList()
            .flatMap {
            if(it.isEmpty()){
                Mono.empty<GrammarDto>()
            }
            Mono.just(it[0])
        }.awaitFirst()

    suspend fun log(): List<LogDto> =
        repository.findAll(
            Sort.by(Sort.Direction.DESC, "fixedTime")
        ).flatMap {
            Flux.just(LogDto(it))
        }.collectList()
            .awaitFirst()
}