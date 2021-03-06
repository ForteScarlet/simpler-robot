/*
 *
 *  * Copyright (c) 2020. ForteScarlet All rights reserved.
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

package love.forte.simbot.listener


/**
 * 一般由组件实现，代表监听函数注册结束后的代码。
 *
 *  @author ForteScarlet -> https://github.com/ForteScarlet
 */
public interface ListenerRegistered {

    /**
     * 监听函数注册结束。
     */
    fun onRegistered(manager: ListenerManager)

}