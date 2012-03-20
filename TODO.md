# High Priorities #

 - Prevent divergence by making a particular URL (without query params
   hittable only 40 times), and limit number of pages in site to 20,000
 - Run Axess on itself
 - JTidy HTML check
 - Check every link returns 2XX-type response
 - Surface scan stats in a status page.
 - Add timing (i.e. daily run tests) (+ in dev mode, hitting a URL triggers it.)

 - Update Play to Bootstrap2.X
 - Test more!
 - Checkers
 - Make everything more modular and configurable (and injectable).
   (Consider using Guice?)

# Things to do! #

 - Move to multi-project set up with interface for `Site` defined in a separate file (also `Checker`)
 - Make `Site` based on configuration, defined in special config
 - The above should be an Akka extension
 - Make navigation template helper to make it easier to have the top bar done correctly
 - Write up instructions to build and run.
 - Make landing page useful with rant about instructions, etc.

## Checkers ##

 - Check all links get to valid pages. (Including outside the site!)
 - Check that all inputs that are not hidden or submit have a label

# Low Priority #

 - Configure a set of "always there" classes