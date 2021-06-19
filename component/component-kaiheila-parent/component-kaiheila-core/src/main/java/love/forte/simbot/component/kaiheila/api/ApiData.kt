/*
 *
 *  * Copyright (c) 2021. ForteScarlet All rights reserved.
 *  * Project  simple-robot
 *  * File     MiraiAvatar.kt
 *  *
 *  * You can contact the author through the following channels:
 *  * github https://github.com/ForteScarlet
 *  * gitee  https://gitee.com/ForteScarlet
 *  * email  ForteScarlet@163.com
 *  * QQ     1149159218
 *
 */

@file:JvmName("ApiDataUtil")

package love.forte.simbot.component.kaiheila.api

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.component.kaiheila.api.ApiData.Req
import love.forte.simbot.component.kaiheila.api.ApiData.Resp


/**
 * 此接口定义一个与Api相关的数据类。
 *
 * 定义两个类型一个来回，分别为 [Request][Req] 和 [Response][Resp].
 *
 * @author ForteScarlet
 */
public sealed interface ApiData {

    /**
     * [Request][Req]. 请求相关的数据类。
     * 一次请求，都会有一个对应的 [响应][RESP].
     */
    public interface Req<HTTP_RESP : KhlHttpResp<*>> : ApiData {
        // /** 获取响应的数据类型。 */
        // val respType: KClass<out RESP>

        suspend fun request(client: HttpClient, block: HttpRequestBuilder.() -> Unit): HTTP_RESP // = client.request(block)

        /**
         * 此请求对应的api路由路径。
         * 例如：`/guild/list`
         * */
        val route: String

        /**
         * 此次请求所发送的数据。为null则代表没有参数。
         */
        val body: Any?

        /**
         * 获取请求的鉴权token。
         *
         * - 机器人。TOKEN_TYPE = Bot。 `Authorization: Bot BHsTZ4232tLatgV5AFyjoqZGAHHmpl9mTxYQ/u4/80=`
         * - Oauth2。TOKEN_TYPE = Bearer。 `Authorization: Bearer BHsTZ4232tLatgV5AFyjoqZGAHHmpl9mTxYQ/u4/80=`
         */
        val authorization: String?
    }

    /**
     * [Response][Resp]. 请求响应相关的数据类。
     *
     * @see KhlHttpResp
     *
     */
    public interface Resp : ApiData

}


public suspend inline fun <reified HTTP_RESP : KhlHttpResp<*>> Req<HTTP_RESP>.doRequest(
    apiVersion: ApiVersion,
    client: HttpClient,
): HTTP_RESP {
    return request(client) {
        // body
        this@doRequest.body?.let { b ->
            this.body = b
        }
        contentType(ContentType.Application.Json)
        this@doRequest.authorization?.let { authorization ->
            header("Authorization", "Bot $authorization")
        }

        // path
        url {
            this.toKhlBuild(apiVersion, this@doRequest.route)
        }
    }
}


/**
 * 此接口定义一个 开黑啦 http请求的响应值标准。 参考 [常规 http 接口规范](https://developer.kaiheila.cn/doc/reference#%E5%B8%B8%E8%A7%84%20http%20%E6%8E%A5%E5%8F%A3%E8%A7%84%E8%8C%83)
 *
 *
 * @see ObjectResp
 * @see ListResp
 */
public interface KhlHttpResp<D> {
    /**
     * integer, 错误码，0代表成功，非0代表失败，具体的错误码参见错误码一览
     */
    val code: Int

    /**
     * string, 错误消息，具体的返回消息会根据Accept-Language来返回。
     */
    val message: String

    /**
     * 响应值。
     */
    val data: D

}


/**
 * 返回值 [data] 为一个json实例对象的结果。
 */
@Serializable
public data class ObjectResp<RESP : Resp>(
    /**
     * integer, 错误码，0代表成功，非0代表失败，具体的错误码参见错误码一览
     */
    override val code: Int,
    /**
     * string, 错误消息，具体的返回消息会根据Accept-Language来返回。
     */
    override val message: String,
    /**
     * mixed, 具体的数据。
     */
    override val data: RESP?,
) : KhlHttpResp<RESP?>

/**
 * 返回值为一个列表（数组）实例对象的结果。
 *
 * 列表返回的时候会有三个属性
 * - `items`, 为列表结果
 * - `meta`, 为分页信息
 * - `sort`, 为排序信息
 *
 * 其中，如果无法确定sort类型（字段值），则直接使用 Map<String, Int>
 *
 */
@Serializable
public data class ListResp<RESP : Resp, SORT>(
    /**
     * integer, 错误码，0代表成功，非0代表失败，具体的错误码参见错误码一览
     */
    override val code: Int,
    /**
     * string, 错误消息，具体的返回消息会根据Accept-Language来返回。
     */
    override val message: String,
    /**
     * mixed, 具体的数据。
     */
    override val data: ListRespData<RESP, SORT>,
) : KhlHttpResp<ListRespData<RESP, SORT>>

/**
 * 返回值为一个列表（数组）实例对象的结果。
 *
 * 列表返回的时候会有三个属性
 * - `items`, 为列表结果
 * - `meta`, 为分页信息
 * - `sort`, 为排序信息
 *
 * 其中，sort 类型为 Map<String, Int>
 *
 */
@Serializable
public data class ListRespForMapSort<RESP : Resp>(
    /**
     * integer, 错误码，0代表成功，非0代表失败，具体的错误码参见错误码一览
     */
    val code: Int,
    /**
     * string, 错误消息，具体的返回消息会根据Accept-Language来返回。
     */
    val message: String,
    /**
     * mixed, 具体的数据。
     */
    val data: ListRespDataForMapSort<RESP>,
)


@Serializable
public data class ListRespData<RESP : Resp, SORT>(
    val items: List<RESP> = emptyList(),
    val meta: RespPageMeta,
    val sort: SORT? = null,
)

@Serializable
public data class ListRespDataForMapSort<RESP : Resp>(
    val items: List<RESP> = emptyList(),
    val meta: RespPageMeta,
    val sort: Map<String, Int> = emptyMap(),
)

@Serializable
public data class RespPageMeta(
    val page: Int,
    @SerialName("page_total")
    val pageTotal: Int,
    @SerialName("page_size")
    val pageSize: Int,
    val total: Int,
)


// public data class ReqData<HTTP_RESP : KhlHttpResp<*>>
// @JvmOverloads
// constructor(
//     override val route: String,
//     override val authorization: String? = null,
//     override val body: Any? = null,
//     private val doClient: suspend (client: HttpClient, block: HttpRequestBuilder.() -> Unit) -> HTTP_RESP,
// ) : Req<HTTP_RESP> {
//     override suspend fun request(client: HttpClient, block: HttpRequestBuilder.() -> Unit): HTTP_RESP =
//         doClient(client, block)
// }
//
//
// @Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
// @DslMarker
// public annotation class ReqBuilderDsl
//
//
// public class ReqBuilder<HTTP_RESP : KhlHttpResp<*>>
// @JvmOverloads
// constructor(
//     var route: String? = null,
//     var doClient: (suspend (client: HttpClient, block: HttpRequestBuilder.() -> Unit) -> HTTP_RESP)? = null,
// ) {
//     @ReqBuilderDsl
//     var authorization: String? = null
//
//     @ReqBuilderDsl
//     var parameters: Any? = null
//
//     /** Build instance. */
//     fun build(): Req<HTTP_RESP> = ReqData(
//         route = requireNotNull(route) { "Require route was null." },
//         authorization = authorization,
//         body = parameters,
//         doClient = requireNotNull(doClient) { "Require doClient function was null." },
//     )
//
// }
//
//
// public inline fun <reified HTTP_RESP : KhlHttpResp<*>> req(
//     route: String? = null,
//     noinline doClient: suspend (client: HttpClient, block: HttpRequestBuilder.() -> Unit) -> HTTP_RESP,
//     block: ReqBuilder<HTTP_RESP>.() -> Unit,
// ): Req<HTTP_RESP> {
//     return ReqBuilder(route, doClient).apply(block).build()
// }