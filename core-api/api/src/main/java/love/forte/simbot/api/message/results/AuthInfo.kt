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
@file:JvmMultifileClass
@file:JvmName("Results")
package love.forte.simbot.api.message.results

/**
 * bot的权限信息
 */
public interface AuthInfo : Result {

    @Deprecated("Use property 'auths'.")
    val cookies: Cookies

    /**
     * 得到权限信息。不保证其内部存在信息。
     */
    val auths: Auths

    /**
     * 如果存在，则得到token信息。在qq中，token一般指代bkn值。
     * 其他应用中参考其文档说明。
     */
    val token: String?

    /**
     * Cookies信息封装接口
     * @see CookieValuesImpl [Cookies]的默认实现。
     */
    public interface Cookies {
        operator fun get(key: String): String?
        fun toMap(): MutableMap<String, String>
        override fun toString(): String
    }


    /**
     *
     * 记录着各项 **权限** 信息的类，用于代替原本的 [Cookies].
     *
     * 首先，在很多情况下，[Auths] 中记录的可能不仅仅是cookies的信息，而是一些具有特殊规则的信息。
     * 以QQ为例，QQ可能在各个不同的域名下存在不同的权限信息，那么如果仅用 [Cookies] 则会造成含义混淆。
     *
     * 在[Auths]中，其本质依旧是 `key-value pairs`, 但是它们并不一定是标准意义上的键值对，
     * 比如可能存在这样两个键值对：
     * ```properties
     *   psKey:vip.qq.com=xxxxx
     *   psKey:qzone.qq.com=xxxxx
     *  ```
     *  那么这就可能代表在不同的两个域下的 psKey 所对应的值。
     *
     * 至于Key的规则，以实现的组件说明为准。
     *
     */
    public interface Auths {
        /**
         * 根据[key]得到一个对应的value。
         */
        operator fun get(key: String): String?

        /**
         * 转化为一个Map。
         */
        fun toMap(): Map<String, String>
        override fun toString(): String
    }

}


public fun AuthInfo.Cookies.asAuths(): AuthInfo.Auths = CookiesAsAuths(this)
private class CookiesAsAuths(private val cookies: AuthInfo.Cookies): AuthInfo.Auths {
    override fun get(key: String): String? = cookies[key]
    override fun toMap(): Map<String, String> = cookies.toMap()
    override fun toString(): String = cookies.toString()
}

public fun AuthInfo.Auths.asCookies(): AuthInfo.Cookies = AuthsAsCookies(this)
private class AuthsAsCookies(private val auths: AuthInfo.Auths): AuthInfo.Cookies {
    override fun get(key: String): String? = auths[key]
    override fun toMap(): MutableMap<String, String> = auths.toMap().toMutableMap()
    override fun toString(): String = auths.toString()
}


private val COOKIE_SPLIT_REGEX = Regex("; *")

/**
 * 根据 cookies 字符串的实例。
 */
public data class CookieValuesImpl(private val cookieValue: String) : AuthInfo.Cookies {

    private val cookieMap: MutableMap<String, String> by lazy(LazyThreadSafetyMode.PUBLICATION) {
        cookieValue.split(COOKIE_SPLIT_REGEX).asSequence().filter {
            it.isNotBlank()
        }.map {
            val sp: List<String> = it.split("=", limit = 2)
            if(sp.size == 1){
                sp[0] to ""
            }else{
                sp[0] to sp[1]
            }
        }.toMap().toMutableMap()
    }

    override fun get(key: String): String? = cookieMap[key]
    override fun toMap(): MutableMap<String, String> = cookieMap

    override fun toString(): String = cookieMap.toString()
}

/**
 * 获取一个 [AuthInfo] 的空值实现。
 */
public fun emptyAuthInfo(): AuthInfo = EmptyAuthInfo

/**
 * 空值实现。
 */
private object EmptyAuthInfo : AuthInfo {
    override val cookies: AuthInfo.Cookies
        get() = EmptyCookie
    override val auths: AuthInfo.Auths
        get() = EmptyAuth
    override val token: String?
        get() = null
    override val originalData: String
        get() = "{}"

    override fun toString(): String {
        return "EmptyAuthInfo()"
    }

    private object EmptyCookie : AuthInfo.Cookies {
        override fun get(key: String): String? = null
        override fun toMap(): MutableMap<String, String> = mutableMapOf()
        override fun toString(): String = "{}"
    }
    private object EmptyAuth : AuthInfo.Auths {
        override fun get(key: String): String? = null
        override fun toMap(): Map<String, String> = emptyMap()
        override fun toString(): String = "{}"
    }

}











