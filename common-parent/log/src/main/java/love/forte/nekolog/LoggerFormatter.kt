/*
 * Copyright (c) 2020. ForteScarlet All rights reserved.
 * Project  parent
 * File     LoggerFormater.kt
 *
 * You can contact the author through the following channels:
 * github https://github.com/ForteScarlet
 * gitee  https://gitee.com/ForteScarlet
 * email  ForteScarlet@163.com
 * QQ     1149159218
 */

package love.forte.nekolog


/**
 *
 * @author ForteScarlet -> https://github.com/ForteScarlet
 */
public fun interface LoggerFormatter {
    /**
     * 对输出的日志进行格式化。
     */
    fun format()
}