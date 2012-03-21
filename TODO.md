# High Priorities #

 - JTidy HTML check
 - Check every link returns 2XX-type response (check inside & outside differently)
 - Write up instructions to build, deploy and run in README.md
 - Write up instructions for extending Axess
 - Make sure links aren't "click here", or numbers. (Should be warning.)
 - Add timing (i.e. daily run tests) (+ in dev mode, hitting a URL triggers it.)

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
 - More intelligently prune the URL search tree (rather than cutting it off at 15000)