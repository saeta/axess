# High Priorities #

 - Have login for Stanford's Axess
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