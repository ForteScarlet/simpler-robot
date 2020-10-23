/*
 * Copyright (c) 2020. ForteScarlet All rights reserved.
 * Project  parent
 * File     BaseMsgGets.kt
 *
 * You can contact the author through the following channels:
 * github https://github.com/ForteScarlet
 * gitee  https://gitee.com/ForteScarlet
 * email  ForteScarlet@163.com
 * QQ     1149159218
 */
@file:JvmName("MsgGets")
@file:JvmMultifileClass
package love.forte.simbot.api.message.events

import love.forte.simbot.annotation.ParentListenerType
import love.forte.simbot.api.message.assists.Flag
import love.forte.simbot.api.message.assists.FlagContent
import love.forte.simbot.api.message.containers.AccountContainer
import love.forte.simbot.api.message.containers.BotContainer
import love.forte.simbot.api.message.containers.FlagContainer
import love.forte.simbot.api.message.containers.OriginalDataContainer

/*
    什么? 你问为什么events包下的消息命名还是xxxMsgGet?
    是啊, 为什么呢
 */


/*
 * 此模块下定义基础事件父接口
 */

/**
 * 所有的主要监听事件类型。
 */
public val MsgGetMainListenerTypes: Set<Class<out MsgGet>> =
    setOf(
        PrivateMsg::class.java,
        GroupMsg::class.java,
        GroupMemberPermissionChanged::class.java,
        GroupNameChanged::class.java,
        GroupMemberRemarkChanged::class.java,
        GroupMemberSpecialChanged::class.java,
        FriendNicknameChanged::class.java,
        FriendAvatarChanged::class.java,
        PrivateMsgRecall::class.java,
        GroupMsgRecall::class.java,
        FriendIncrease::class.java,
        GroupMemberIncrease::class.java,
        FriendReduce::class.java,
        GroupMemberReduce::class.java,
        FriendAddRequest::class.java,
        GroupAddRequest::class.java,
    )


/**
 * 监听消息的父接口。
 *
 * 所有的监听消息都应当实现的容器：
 * - [原始信息容器][OriginalDataContainer],
 * - [bot基础信息容器][BotContainer],
 * - [用户容器][AccountContainer]。
 *
 * 作为一个[消息][MsgGet], 一般来讲，所需要实现或者监听的接口为：
 * - [私聊消息][PrivateMsg]
 * - [群聊消息][GroupMsg]
 * - [群成员权限变动事件][GroupMemberPermissionChanged]
 * - [群名称变动事件][GroupNameChanged]
 * - [群友群名片变动事件][GroupMemberRemarkChanged]
 * - [群友头衔变动事件][GroupMemberSpecialChanged]
 * - [好友昵称变动事件][FriendNicknameChanged]
 * - [好友头像变动事件][FriendAvatarChanged]
 * - [私聊消息撤回][PrivateMsgRecall]
 * - [群聊消息撤回][GroupMsgRecall]
 * - [好友增加事件][FriendIncrease]
 * - [群友增加事件][GroupMemberIncrease]
 * - [好友减少事件][FriendReduce]
 * - [群友减少事件][GroupMemberReduce]
 * - [好友添加请求事件][FriendAddRequest]
 * - [群添加请求][GroupAddRequest]
 *
 */
@ParentListenerType("所有监听类型的父接口")
public interface MsgGet : OriginalDataContainer, BotContainer, AccountContainer {
    /** 当前监听事件消息的ID。一般情况下应当是一个唯一ID。 */
    val id: String


    /**
     * 可以得到一个 **消息**。
     * 如果你想要对消息进行 **重新定义**，
     * 可以考虑注入一个 [love.forte.simbot.core.listener.ListenerContext] 而不是想办法重新设置 [msg]。
     */
    val msg: String?


    /** 消息接收到的时间。一般是一个毫秒时间戳。 */
    val time: Long


    /** 应当重写toString方法 */
    override fun toString(): String


    // /**
    //  * 当前类型所对应的真正要触发的监听类型，也是要触发的监听类型。
    //  */
    // val listenType: Class<out MsgGet>? get() = this.javaClass
}


/**
 * 判断一个MsgGet的实例是否存在于某个集合中。
 *
 * 即从此集合中寻找此是否存在此类型的父类。
 *
 * 如果存在，返回在这个集合中的结果集，否则返回一个空集合.
 */
public infix fun <T : Class<out MsgGet>> MsgGet.findIn(typeCollections: Collection<T>): List<T> {
    if (typeCollections.isEmpty()) return emptyList()

    val thisClass: Class<MsgGet> = javaClass
    return typeCollections.filter {
        it.isAssignableFrom(thisClass)
    }
}

/**
 * 判断一个MsgGet的实例是否存在于某个集合中。
 *
 * 即从此集合中寻找此是否存在此类型的父类。
 *
 * 如果存在，返回在这个集合中的结果集，否则返回一个空sequence.
 */
public infix fun <T : Class<out MsgGet>> MsgGet.findSequenceIn(typeCollections: Collection<T>): Sequence<T> {
    if (typeCollections.isEmpty()) return emptySequence()

    val thisClass: Class<MsgGet> = javaClass
    return typeCollections.asSequence().filter {
        it.isAssignableFrom(thisClass)
    }
}

/**
 * 判断一个MsgGet的实例是否存在于某个Map中。
 *
 * 即从此集合中寻找此是否存在此类型的父类。
 *
 * 如果存在，返回在这个集合中的结果集，否则返回一个空集合.
 */
public infix fun <T : Class<out MsgGet>> MsgGet.findIn(typeCollections: Map<T, *>): List<T> {
    if (typeCollections.isEmpty()) return emptyList()

    val thisClass: Class<MsgGet> = javaClass
    return typeCollections.map { it.key }.filter {
        it.isAssignableFrom(thisClass)
    }
}

/**
 * 判断一个MsgGet的实例是否存在于某个Map中。
 *
 * 即从此集合中寻找此是否存在此类型的父类。
 *
 * 如果存在，返回在这个集合中的结果集，否则返回一个空sequence.
 */
public infix fun <T : Class<out MsgGet>, V> MsgGet.findSequenceIn(typeCollections: Map<T, V>): Sequence<V> {
    if (typeCollections.isEmpty()) return emptySequence()

    val thisClass: Class<MsgGet> = javaClass
    return typeCollections.asSequence().filter {
        it.key.isAssignableFrom(thisClass)
    }.map { it.value }
}

/**
 * 判断一个MsgGet的实例是否存在于某个Map中。
 *
 * 即从此集合中寻找此是否存在此类型的父类。
 *
 * 如果存在，返回在这个集合中的结果集，否则返回一个空集合.
 */
public infix fun <T : Class<out MsgGet>, V> MsgGet.findValuesIn(typeCollections: Map<T, V>): List<V> =
    (this findSequenceIn typeCollections).toList()


/**
 * 事件父接口，
 * 是当一个监听类型为得不到 [消息文本][msg] 的事件的时候使用的接口。
 *
 * 此父接口与 [MsgGet] 的唯一区别就是此接口为 [msg] 提供了无效化的默认实现。
 */
public interface EventGet : MsgGet {
    @JvmDefault
    override val msg: String? get() = null
}


/**
 * 与消息有关的事件。
 *
 * [MessageEventGet]中除了需要实现[MsgGet]以外, 还要实现[FlagContainer]以标识一个消息内容的标识。
 * 但是一般来讲, [FlagContent] 都可以用 [id] 来代替。
 *
 * 因此 [FlagContent] 提供为默认方法并使用 [id] 作为返回值。如果有特殊需要则重写。
 */
@ParentListenerType("消息事件父接口")
public interface MessageEventGet : MsgGet, FlagContainer<MessageEventGet.MessageFlagContent> {

    /**
     *  消息事件的消息正文文本。
     *
     * [消息正文][msgContent] 不允许为`null`，但是其中的 [msg][MessageContent.msg] 则就不保证了。
     */
    val msgContent: MessageContent

    /**
     * 提供一个简单的方法来获取 [msgContent] 中的文本内容。
     */
    override val msg: String?

    /**
     * 消息类型的标识
     */
    override val flag: Flag<MessageFlagContent>

    /**
     * [MessageEventGet] 所对应的 [标识][Flag]
     */
    public interface MessageFlagContent : FlagContent
}


/**
 * 与消息撤回有关的事件,
 * 例如 [群消息撤回][GroupMsgRecall]
 * 或者 [私聊撤回][PrivateMsgRecall]
 *
 *
 * 一般来讲应该可以得到撤回的[消息内容][MsgGet.msg]以及[撤回时间][recallTime]
 */
@ParentListenerType("消息撤回父接口")
public interface MessageRecallEventGet : MsgGet {
    /**
     * 撤回时间。毫秒时间戳。
     */
    val recallTime: Long
}


/**
 * 成员变动事件接口，是[增加事件][IncreaseEventGet] 与 [减少事件][ReduceEventGet]的父接口.
 */
@ParentListenerType("成员数量变动父接口")
public interface MemberChangesEventGet : EventGet

/**
 * 与 **增加** 有关的事件，例如 群友增加 或者 好友增加
 *
 */
@ParentListenerType("数量增加父接口")
public interface IncreaseEventGet : MemberChangesEventGet


/**
 * 与 **减少** 有关的事件，例如 群友减少 或者 好友减少
 *
 */
@ParentListenerType("数量减少父接口")
public interface ReduceEventGet : MemberChangesEventGet


/**
 * 与 **请求** 相关的父接口
 */
@ParentListenerType("请求相关事件父接口")
public interface RequestGet : MsgGet, FlagContainer<RequestGet.RequestFlagContent> {

    /**
     * 获取一个请求类型的标识
     */
    override val flag: Flag<RequestFlagContent>

    /**
     * 在请求类型下的 [标识主体][FlagContent] 类型。
     */
    interface RequestFlagContent : FlagContent
}


/**
 * 出现变化的事件。例如**权限变更**、**头像变更**、**名称变更**等等。
 * 大多数情况下，此类事件都是在变更完了之后触发的。
 */
@ParentListenerType("变更相关事件父接口")
public interface ChangedGet<out T> : EventGet {
    /**
     * 变更之前。 不能够保证此值可以获得
     */
    val beforeChange: T?

    /**
     * 变更之后。 此值应当始终可以获取。
     */
    val afterChange: T
}






