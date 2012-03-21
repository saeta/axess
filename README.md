<!--- Build this file by running `pandoc README.md > README.html` -->
# Axess: A website accessibility checker (and more!) #

 * Author: Brennan Saeta <saeta@cs.stanford.edu>
 * Written: February-March, 2012
 * Status: Beta Release
 * Contributors:
     - Your name here

## Background ##

This winter, I volunteered (for school credit) with [Coursera][coursera].
This tool was initially written (in Java) to help them test their pages
to ensure they maintained a standard of accessibility. They liked it so much,
they wanted me to (1) open source it, and (2) put a web interface on it to make
it self-service for professors and others to run. Because I had initially
written it in Java using Guice, it was nice and modular, but would have
required a substantial rewrite to convert it to a web environment. Thus this
project was born.

[coursera]: http://www.coursera.org/ "Coursera"

### Choice of tools ###

I chose to build this tool on top of the Typesafe stack mostly out of
curiosity. That and when I read through the Play documentation, I really
liked the Selenium API. (It turns out, that API isn't part of the Typesafe
stack, but the first reason applies nonetheless. :-D)

### Choice of Name ##

Stanford has a really bad administrative website called [Axess][axess]. I
named this tool in honor of that website.

[axess]:http://axess.stanford.edu "Axess - Stanford's Administrative Portal"

# Developing #

Pull requests and patches are always welcome! Follow the instructions below
to help set up your workspace and development environment. Please follow the
source code style conventions.

## Prereq's ##

You will need [`play`][play] to compile and run this project. You can download
the framework, and install it (unzip it, and add the folder to your path)
quickly. You will also need Git, the distributed source control system. Finally,
you will optionally need [MySQL](http://www.mysql.com/ "MySQL Database"). On
Ubuntu, or other Debian-based systems, a simple `sudo apt-get install mysql-server`
will suffice. For Mac's, MySQL has installation `pkg`'s available for download.
If you choose not to insall MySQL, check out the instructions for running with
the in-memory database below. (Section: Running without MySQL)

[play]: http://www.playframework.org/ "Play! Framework"

### Installing Git ###

Github has the best guides to installing Git. Check out their
[getting started][github] page.

[github]: http://help.github.com/set-up-git-redirect "Set Up Git"

### Step By Step: Installing Play ###

1. Download the zip from the download section.
2. Unzip the folder in your home directory. (From the command line, this can
   be done by executing: `unzip play-2.0.zip`, or `open play-2.0.zip` on a Mac)
3. Add that folder to your path. If you're using Bash edit your `~/.bashrc`
   and append the line

     ```bash
     export PATH=$PATH:~/play-2.0
     ```

     To edit your `.bashrc`, you can execute either: `gedit ~/.bashrc` (Linux)
     or `open ~/.bashrc` (Mac) on the command prompt.

4. Test that it works by opening a new command prompt (your changes from last
   step) do not take effect in your current command prompt), and type `play`.
   You should see some spew similar to:

		   Getting play console_2.9.1 2.0 ...
			:: retrieving :: org.scala-tools.sbt#boot-app
			confs: [default]
		      	4 artifacts copied, 0 already retrieved (1472kB/40ms)
		                    _            _ 
		              _ __ | | __ _ _  _| |
		             | '_ \| |/ _' | || |_|
		             |  __/|_|\____|\__ (_)
		             |_|            |__/ 
		
		    play! 2.0, http://www.playframework.org
		
		    This is not a play application!
		
		    Use `play new` to create a new Play application in the current directory,
		    or go to an existing application and launch the development console using `play`.
		
		    You can also browse the complete documentation at http://www.playframework.org.

    If you see something unhappy along the lines of "command not found", it hasn't
    worked yet.


### Setting up MySQL ###

1. Log in to mysql on the command prompt. `mysql -uroot -p`
2. Execute the following sequence of commands:

```sql
         CREATE DATABASE axess;
         GRANT ALL ON axess.* TO `axess`@`localhost` IDENTIFIED BY `axess`;
         FLUSH PRIVILEGES;
```

3. Exit the `mysql` shell by typing Ctrl-D.

MySQL is ready to be used. Axess leverages Play!'s database versioning
capabilities (called [Evolutions](https://github.com/playframework/Play20/wiki/Evolutions)).
Therefore, there is no further necessary setup necessary.

## Running a test application ##

Thanks to Play!, compiling and running your own version of `Axess` is easy.
Just:

    play start

By default, this will start a web browser on port 9000. If you would like to
develop, play supports automatic compilation, and can display compilation
errors in the browser. To do this, instead of `play start`, execute `play run`.
On every request, play makes sure every file is up to date, and if not, automagically
compiles it.

### Running without MySQL ###

You can run without MySQL. It will use an in-memory H2 datastore. (Note: every
time the server is stopped, all configuration will be lost. This should only
be used for developing and demoing purposes. MySQL should be the only
database used in a production environment.) To do this, we specify the
`memory.conf` configuration file. Below are a few examples of its use:

    $ play "start -Dconfig.resource=memory.conf" # From Bash
    $ play "run -Dconfig.resource=memory.conf"
    [axess] $ run -Dconfig.resource=memory.conf  # From Play Shell

# Deploying: Using Axess #

You can either run Axess in place, or deploy it to a remote machine. Both are
easily supported by Play. Please see the [Play deploying
documentation][play-deploy].

[play-deploy]: https://github.com/playframework/Play20/wiki/Production "Deploying Play! Applications"

## Eclipse Configuration ##

Eclipse and IDEA IDE support are provided out of the box by the Play Framework.
See the Play article on [Setting up your preferred IDE][ide].

[ide]: https://github.com/playframework/Play20/wiki/IDE "Set up your IDE"

# Extending Axess #

Axess is *designed* to be extensible. It is very easy to add new kinds of
sites to log into, and to add new code verifications.

## Logging into new sites ##

If you would like to log into a new kind of site, look in the 
`app/models/SiteType.scala` file for examples. Please add your own!

## Checking other properties ##

If you would like to make different assertions about pages, look in the
`app/axess/checkers` package for examples. In short, you need to subclass the
`axess.checkers.Checker` class. Then, look at the `app/axess/Worker.scala` file
and add your new checker to the `checkers` list. That's all there is to it!

