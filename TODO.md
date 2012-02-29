# Things to do! #

 - Scan a site!
 - Move to multi-project set up with interface for `Site` defined in a separate file (also `Checker`)
 - Make `Site` based on configuration, defined in special config
 - The above should be an Akka extension
 - Make navigation template helper to make it easier to have the top bar done correctly
 - Write up instructions to build and run.

## Checkers ##

 - Check all `img` tags have an attribute `alt` (alt text). Note: it can be
   the empty string. (e.g. if the image is purely decorative.)
 - Check all links get to valid pages. (Including outside the site!)
 - Check that all imputs that are not hidden or submit have a label
