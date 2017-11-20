/**
 * Kotlin Slack Hello Bot - A simple example to post a message to Slack
 * Copyright (C) 2017 Marcus Fihlon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
