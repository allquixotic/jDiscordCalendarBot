package com.github.allquixotic.jDiscordCalendarBot;

import com.google.api.client.util.Strings;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.discordjson.json.AllowedMentionsData;
import discord4j.discordjson.json.EmbedData;
import discord4j.discordjson.json.MessageEditRequest;
import discord4j.discordjson.possible.Possible;
import discord4j.gateway.intent.Intent;
import discord4j.gateway.intent.IntentSet;
import lombok.val;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Main {

    public static final Logger log = Logger.getGlobal();

    public static void main(String[] args) throws IOException {
        val conf = args.length == 0 ? Config.readConfig() : Config.readConfig(args[0]);
        val client = DiscordClient.create(conf.getDiscordSecret());
        val gateway = client.gateway().setEnabledIntents(IntentSet.of(Intent.GUILDS)).login().block();
        val pool = Executors.newCachedThreadPool();
        val scraper = new EventScraper(conf);

        final Runnable post = () -> {
            log.info("Processing update...");
            val chan = gateway.getChannelById(Snowflake.of(conf.getDiscordChannel())).block();
            log.info("Got the channel we're working on.");
            val calens = new Calen[] {
                    scraper.getCalendar(LocalDate.now().minusDays(1)),
                    scraper.getCalendar(LocalDate.now()),
                    scraper.getCalendar(LocalDate.now().plusDays(1))
            };

            var eye = 0;
            for(val calen : calens) {
                if(calen != null) {
                    if(!Strings.isNullOrEmpty(calen.getMessageId())) {
                        try {
                            val msg = gateway.getMessageById(Snowflake.of(conf.getDiscordChannel()), Snowflake.of(calen.getMessageId())).block();
                            val theMsg = msg.getContent();
                            if(!calen.toString().trim().equalsIgnoreCase(theMsg)) {
                                log.info("Updating message for " + calen.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
                                var rm = chan.getRestChannel().getRestMessage(Snowflake.of(calen.getMessageId()));
                                rm.edit(MessageEditRequest.builder().content(calen.toString()).build()).block();
                            }
                        }
                        catch(Exception e) {
                            Main.logSevere(e);
                            val newMsg = chan.getRestChannel().createMessage(calen.toString()).block();
                            calen.setMessageId(newMsg.id());
                            scraper.updateCalen(calen.getDate(), calen);
                        }
                    }
                    else {
                        val newMsg = chan.getRestChannel().createMessage(calen.toString()).block();
                        calen.setMessageId(newMsg.id());
                        scraper.updateCalen(calen.getDate(), calen);
                    }
                }
                else {
                    log.info("Calen null: " + eye);
                }
                eye++;
            }

            try {
                val obs = scraper.getCalendar(LocalDate.now().minusDays(2));
                if(obs != null) {
                    val obsolete = obs.getMessageId();
                    if(obsolete != null) {
                        val rm = chan.getRestChannel().getRestMessage(Snowflake.of(obsolete));
                        rm.delete("Obsolete message deleted by Calendar bot").block();
                        obs.setMessageId(null);
                        scraper.updateCalen(obs.getDate(), obs);
                    }
                    else {
                        log.info("No obsolete message ID present on Calen.");
                    }
                }
                else {
                    log.info("DB didn't have a calendar record for obsolete day.");
                }
            }
            catch(Exception ee) {
                logSevere(ee);
            }
        };

        gateway.onDisconnect().repeat(() -> {
            while(true) {
                try {
                    sleepOrExit(15 * 60);
                    client.login().block();
                    break;
                } catch (Exception e) {
                    logSevere(e);
                    sleepOrExit(10 * 60);
                }
            }
            return true;
        });

        gateway.on(ReadyEvent.class).subscribe((rdy) -> {
            log.info("Discord Ready event received.");
            client.getCoreResources().getReactorResources().getTimerTaskScheduler().schedulePeriodically(() -> {
                pool.submit(post);
            }, 0, 15, TimeUnit.MINUTES);
        });
    }

    public static String getThrowableInfo(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.println(t.getMessage());
        t.printStackTrace(pw);
        return sw.toString();
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
