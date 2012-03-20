# High Priorities #

 - Prevent divergence by making a particular URL (without query params
   hittable only 40 times), and limit number of pages in site to 20,000
 - JTidy HTML check
 - Check every link returns 2XX-type response (check inside & outside differently)
 - Make sure links aren't "click here", or numbers. (Should be warning.)
 - Surface scan stats in a status page.
 - Add timing (i.e. daily run tests) (+ in dev mode, hitting a URL triggers it.)
 - Fix infinite redirect issue by displaying a "not scanning yet" page
 - Write up instructions to build, deploy and run in README.md

 - Update Play to Bootstrap2.X
 - Checkers
 - Make everything more modular and configurable (and injectable).
   (Consider using Guice?)

# Things to do! #

 - Move to multi-project set up with interface for `Site` defined in a separate file (also `Checker`)
 - Make `Site` based on configuration, defined in special config
 - The above should be an Akka extension
 - Create 2 different message types "Warnings", and "Errors"
 - Add the ability to suppress Warnings that match a particular pattern.
