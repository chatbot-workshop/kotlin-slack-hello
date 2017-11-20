package ch.jug.workshop.chatbot

import com.ullink.slack.simpleslackapi.SlackSession
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener
import java.util.Properties

private var botName = ""
private var authToken = ""
private var channelName = ""
private var botId: String = ""

fun main(args: Array<String>) {
    loadConfig()
    val session = SlackSessionFactory.createWebSocketSlackSession(authToken)
    session.connect()
    botId = "<@${session.findUserByUserName(botName).id}>"
    session.addMessagePostedListener(messagePostedListener)
}

fun loadConfig() {
    val stream = Thread.currentThread().contextClassLoader.getResourceAsStream("bot.conf")
    val conf = Properties()
    conf.load(stream)
    botName = conf.getProperty("botName")
    authToken = conf.getProperty("authToken")
    channelName = conf.getProperty("channelName")
}

val messagePostedListener = SlackMessagePostedListener { event, session ->
    if (event.channel.name == channelName && event.messageContent.contains(botId)) {
        sayHello(event, session)
    }
}

fun sayHello(event: SlackMessagePosted, session: SlackSession) {
    session.sendMessage(event.channel, "Hello, ${event.sender.realName}.")
}
