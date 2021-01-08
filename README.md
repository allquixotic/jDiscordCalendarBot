Bot for keeping a Discord calendar (in a text channel) in sync with an Enjin calendar. Partial rewrite/port of njDiscordCalendarBot.

Handles "flakiness" with external servers **much** better than njDiscordCalendarBot:

 - If the Discord gateway goes down, it just backs off for 10 minutes but keeps trying.
 - If Enjin goes down, it just backs off for 10 minutes but keeps trying.
 - If the proxy goes down, waits 10 minutes and tries a different one from the list of proxies (currently tied to one specific vendor).
 - Tests proxies using `curl` before trying to use them on Enjin.
 - Only bothers Enjin once every 2 hours (configurable).
 - Caches calendar data on disk with automatic 60 day expiration/deletion. Eliminates the need to rebuild the calendar if the process crashes.
 
Also have it hooked up to systemd and configured to restart if it crashes, so it's only really beholden to the physical hardware's reliability.

To-Do:

 - [] Convert MapDB usage over to use transactions.
 - [] Replace some hairy repeated tracts of code with helper functions or reusable lambdas (DRY).
 - [] Add back !events feature like njDiscordCalendarBot.
 - [] Callback when the date changes (midnight) to instantly update Discord.
