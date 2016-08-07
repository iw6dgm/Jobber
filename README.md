Jobber
======

At the time of writing, Jobber is almost a 12 years old project born as just as fun exercise to learn programming in Java, something rather useful with a small, easy-to-use GUI.
It eventually turned out that this bunch of crappy code helped me out to do one of the most utterly annoying task known to human beings: keep track of the time spent in tasks and projects, and eventually create a report for accounting purposes.

Yes, because my former company wanted to know how many minutes, hours and days were spent on a bug fix, a minor enhancement and, in total, on a project.
Bug fixings, enhancements and projects were differently cost-timed, and my former, greedy company needed to know how much to get payed from its (only) customer (poor guys...) for my (precious!) work.

How could I have even remembered minutes or days spent on dozens of freaking tasks called *ME-xyz* (yep, *ME* stands for *Minor Enhancement* and *xyz* is just an incremental number) or on those boring bug fixes called *INC-xyz* (yeah, *INC* stands for *Incident* and *xyz*... you already know that) completed days, weeks (or months) ago?
How could I have even sum up all those different tasks within the proper project they belong to? (The *wrong* answer is *Excel*)

This is where Jobber came into action: a small piece of software that did the dirty job for me.

Features
--------

- Highly portable: written in Java SE
- XML local property configuration
- Database-driven : it connects to a local or remote MySQL database to store projects, tasks, user accounts and timings
- Optionally HTTP-driven: it connects to some external web application and, using some obscure API, it stores stuff as described before
- Web configuration: projects, tasks, users and project-user associations are configured via web application
- Small, light-weight, easy-to-use GUI

TODO List, Code review and issues
---------------------------------

- Very very old, unreadable code
- Poorly modularised: no concept of packages, modules, ...
- Coupled to MySQL: why not using some light-weight local DB as such SQLLite and then synchronise to a centralised MySQL / general DB?
- Nobody likes XML: why not JSON?
- HTTP APIs are not RESTFul
- Some class is really "big" (too many code lines)
- At the time of writing, web application (I recall some sort of PHP web app) is missing / lost
- Database schema incomplete