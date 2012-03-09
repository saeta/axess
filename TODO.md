# High Priorities #

 - When started up, mark all scans that aren't completed already as error'd
 - Store Page Title in ScanMsg for more readable display
 - Keep track of number of pages scanned in Scan db entry
 - Keep track of start & end times for scan in DB
 - See if the number of pages can be slimmed down. 
   (e.g. ignore everything after #)
 - Test more!
 - Have default landing page be useful!
 - Checkers
 - View past scans

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
