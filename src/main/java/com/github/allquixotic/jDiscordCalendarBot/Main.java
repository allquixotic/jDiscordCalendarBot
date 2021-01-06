package com.github.allquixotic.jDiscordCalendarBot;

import com.google.api.client.util.Strings;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import lombok.val;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Main {

    public static final Logger log = Logger.getGlobal();

    public static void main(String[] args) throws IOException {
        val conf = args.length == 0 ? Config.readConfig() : Config.readConfig(args[0]);
        val client = DiscordClient.create(conf.getDiscordSecret());
        val gateway = client.login().block();
        val pool = Executors.newCachedThreadPool();
        val scraper = new EventScraper(conf);

        final Runnable post = () -> {
            val chan = gateway.getChannelById(Snowflake.of(conf.getDiscordChannel())).block();
            val calens = new Calen[] {
                    scraper.getCalendar(LocalDate.now().minusDays(2)),
                    scraper.getCalendar(LocalDate.now().minusDays(1)),
                    scraper.getCalendar(LocalDate.now()),
                    scraper.getCalendar(LocalDate.now().plusDays(1))
            };

            for(val calen : calens) {
                if(calen != null) {
                    if(!Strings.isNullOrEmpty(calen.getMessageId())) {
                        try {
                            val msg = gateway.getMessageById(Snowflake.of(conf.getDiscordChannel()), Snowflake.of(calen.getMessageId())).block();
                            msg.edit((mes) -> {
                                mes.setContent(calen.toString());
                            });
                        }
                        catch(Exception e) {
                            Main.logSevere(e);
                            val newMsg = chan.getRestChannel().createMessage(calen.toString()).block();
                            calen.setMessageId(newMsg.id());
                        }
                    }
                    else {
                        val newMsg = chan.getRestChannel().createMessage(calen.toString()).block();
                        calen.setMessageId(newMsg.id());
                    }
                }
            }
        };

        gateway.onDisconnect().repeat(() -> {
            while(true) {
                try {
                    client.login().block();
                    break;
                } catch (Exception e) {
                    logSevere(e);
                    sleepOrExit(5 * 60);
                }
            }
            return true;
        });

        gateway.on(ReadyEvent.class).subscribe((rdy) -> {
            client.getCoreResources().getReactorResources().getTimerTaskScheduler().schedulePeriodically(() -> {
                pool.submit(post);
            }, 0, 2, TimeUnit.HOURS);
        });
    }

    public static String getThrowableInfo(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.println(t.getMessage());
        t.printStackTrace(pw);
        return pw.toString();
    }

    public static void logSevere(String context, Throwable t) {
        log.severe(String.format("%s\n%s", context, getThrowableInfo(t)));
    }

    public static void logSevere(Throwable t) {
        log.severe(getThrowableInfo(t));
    }

    public static void sleepOrExit(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        }
        catch(InterruptedException ie) {
            logSevere(ie);
            System.exit(1);
        }
    }

    public static void sleepOrExit(int seconds, int exitCode) {
        try {
            Thread.sleep(seconds * 1000);
        }
        catch(InterruptedException ie) {
            logSevere(ie);
            System.exit(exitCode);
        }
    }
}
