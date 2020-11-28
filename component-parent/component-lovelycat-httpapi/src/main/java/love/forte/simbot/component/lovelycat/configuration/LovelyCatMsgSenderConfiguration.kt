/*
 *
 *  * Copyright (c) 2020. ForteScarlet All rights reserved.
 *  * Project  simple-robot-S
 *  * File     LovelyCatMsgSenderConfiguration.kt
 *  *
 *  * You can contact the author through the following channels:
 *  * github https://github.com/ForteScarlet
 *  * gitee  https://gitee.com/ForteScarlet
 *  * email  ForteScarlet@163.com
 *  * QQ     1149159218
 *  *
 *  *
 *
 */

package love.forte.simbot.component.lovelycat.configuration

import love.forte.common.ioc.annotation.Beans
import love.forte.common.ioc.annotation.ConfigBeans
import love.forte.simbot.component.lovelycat.LovelyCatApiManager
import love.forte.simbot.component.lovelycat.sender.LovelyCatSenderFactory


@ConfigBeans("lovelyCatMsgSenderConfiguration")
public class LovelyCatMsgSenderConfiguration {

    @Beans("lovelyCatSenderFactory")
    public fun lovelyCatSenderFactory(apiManager: LovelyCatApiManager): LovelyCatSenderFactory {
        return LovelyCatSenderFactory(apiManager)
    }


}