package love.forte.test.listener;

import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.MessageGet;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.MsgSender;

@Beans
public class TestListener {


    @OnPrivate
    public void listen1(PrivateMsg msg, MsgSender sender) {
        System.out.println("listen1");
        // 直接复读消息的最优方案
        sender.SENDER.sendPrivateMsg(msg, msg.getMsgContent());
    }

    @OnPrivate
    @Filter("hi1")
    public void listen2(PrivateMsg msg, MsgSender sender) {
        System.out.println("listen2");
        throw new RuntimeException("text err.");
    }

    @OnPrivate
    @Filter("hi2")
    public void listen3(PrivateMsg msg, MsgSender sender) {
        System.out.println("listen2");
        throw new IllegalStateException("state err.");
    }

    @OnGroup
    @Filter(groups = "703454734")
    public void m2(GroupMsg msg, MsgSender sender) {
        String getMsg = msg.getMsg();
        System.out.println(getMsg);
        sender.SENDER.sendGroupMsg(msg, getMsg);
    }

}
