The Jobber
==========

At the time of writing, The Jobber is almost a 12 years old project born as just as a fun exercise to learn programming in Java, something rather useful with a small, easy-to-use GUI.
It eventually turned out that this bunch of crappy code helped me out with doing one of the most utterly annoying task known to human beings: keep track of the time spent in tasks, projects, and eventually create a report for accounting purposes.

Yes, because my former company wanted to know how many minutes, hours and days were spent on a bug fix, a minor enhancement and, in total, on a project.
Bug fixings, enhancements and projects were differently cost-timed, and my former, greedy company needed to know how much to get paid from its (only one) customer (poor guys...) for my (precious!) work.

How can I remember minutes or days spent on dozens of freaking tasks called *ME-xyz* (yep, *ME* stands for *Minor Enhancement* and *xyz* is just an incremental number) or on those boring bug fixes called *INC-xyz* (yeah, *INC* stands for *Incident* and *xyz*... you already know that) completed days, weeks (or months) ago?

How can I even sum up all those different tasks within the proper project they belong to? (The *wrong* answer is *Excel*)

"Hey, hold on! You know what? We got something called *Intranet*... why don't you use that?". Yeah, there was *some* sort of Intranet portal, developed in a buggy JetShait-something. You might use it, but the *click-here-and-there-and-pray* plus "*remember* to do it every now and then or you will eventually forget your timings" strategy didn't work out very well.

Why not having some "start" and "stop" button, two simple, easy peasy clicks and something else will deal with the rest on my behalf?

This is where The Jobber came into action: a small piece of software that did the dirty job for me.

Features
--------

- Highly portable: written in Java SE
- XML (local) property file configuration
- Database-driven : connects to a local or remote MySQL database to store projects, tasks, user accounts and timings
- Optionally HTTP-driven: connects to some external web application and, using some obscure API, stores stuff as described before
- Web configuration: projects, tasks, users and project-user associations are configured via web application
- Small, light-weight, easy-to-use GUI
- Keeps track of timings even if not running: just open it to click "start" or "stop" (and switch the task) when you need to

TODO List, Code review and Known Issues
---------------------------------------

- Very very old, almost unreadable code
- Poorly modularised: no concept of packages, modules, ...
- Coupled to MySQL: why not using some light-weight local DB as such as SQLLite and then synchronise with a remote DB?
- Nobody likes XML: why not JSON?
- HTTP APIs are not RESTFul
- Some class is really "big" (too many code lines)
- At the time of writing, web application (I recall some sort of PHP web app) is missing / lost
- Database schema incomplete
- Passwords are not actually encrypted, just base64 encoded
- Mobile friendly?

Setup and Configuration
-----------------------

Not possible until I get all those missing pieces...

So why did I bother to put The Jobber here?
-------------------------------------------

Legitimate question. Straight answer: this is a *tribute* to those old days and to whom secretly believed that the Jobber was "cool" but never had the guts to use it (don't go against The System!). I may recall someone asking me "How did you get the timings done so quickly?", "He's got *The Jobber*...!" was the answer, whispered by some random, subversive colleague.

I admit it, MySQL was *the* barrier: we did not use MySQL at all... so why install it just for a small, controversial piece of Java (and SQL), probably unreliable (it wasn't, actually, never), certainly *unauthorised* by The System.
I did not go against The System, I found a tricky way to bypass it. This is the *power* of the The Jobber!

If you are reading this
-----------------------

- I gave you this repo link in the first place: enjoy it!
- You are some random dude snooping around: enjoy it!
- You are a recruiter, HR / IT "agent" before a (potential) job interview: don't waste your time here!
