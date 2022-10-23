package com.marmotta.anyx.graphql

import com.expediagroup.graphql.generator.SchemaGeneratorConfig
import com.expediagroup.graphql.generator.TopLevelObject
import com.expediagroup.graphql.generator.hooks.SchemaGeneratorHooks
import com.expediagroup.graphql.generator.scalars.IDValueUnboxer
import com.expediagroup.graphql.generator.toSchema
import com.expediagroup.graphql.server.operations.Query
import com.marmotta.anyx.graphql.repository.persistence.NewPaymentPersistence
import com.marmotta.anyx.graphql.schema.*
import graphql.GraphQL
import graphql.scalars.ExtendedScalars.*
import graphql.schema.GraphQLType
import org.koin.java.KoinJavaComponent
import org.koin.java.KoinJavaComponent.inject
import java.math.BigDecimal
import java.time.OffsetDateTime
import kotlin.reflect.KClass
import kotlin.reflect.KType


/**
 * Custom logic for how this Ktor server loads all the queries and configuration to create the [GraphQL] object
 * needed to handle incoming requests. In a more enterprise solution you may want to load more things from
 * configuration files instead of hardcoding them.
 */
private val config = SchemaGeneratorConfig(
    supportedPackages = listOf(
        "com.marmotta.anyx.graphql"
    ),
    hooks = CustomSchemaGeneratorHooks()
)


class CustomSchemaGeneratorHooks : SchemaGeneratorHooks {
    override fun willGenerateGraphQLType(type: KType): GraphQLType? = when (type.classifier as? KClass<*>) {
        BigDecimal::class -> GraphQLBigDecimal
        OffsetDateTime::class -> DateTime
        Long::class -> GraphQLLong
        else -> null
    }
}



private val queries = listOf(
    TopLevelObject(HelloQueryService()),
)

private val persistence: NewPaymentPersistence by inject(NewPaymentPersistence::class.java)

private val mutations = listOf(
    TopLevelObject(PaymentReceiveController(persistence))
)


val graphQLSchema = toSchema(config, queries, mutations)

fun getGraphQLObject(): GraphQL = GraphQL.newGraphQL(graphQLSchema)
    .valueUnboxer(IDValueUnboxer())
    .build()


class HelloQueryService : Query {
    fun hello() = "World!"
}
