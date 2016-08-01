Westerstrand Clock Controller
=============================

Overview
---------
This is a small Java app I wrote to configure a Westerstrand WDP M+S master clock system for a school.  The system was installed before the 2007 DST rules came into effect.  As a result, the clock system is off by 1 hour for several weeks out of the year.  As the school uses this system for their bell system, this represents a significant disruption.  The system isn't installed in a location that is particularly easy to get to, and the built in user interface is not particularly easy to use for this purpose.  It is even less easy to use for the purpose of changing the event schedule, which they want to do occasionally.  There is supposedly a software option available for this clock, but at the time I was working on this, I was unable to get ahold of Westerstrand to discuss it with them, and could not find any more information about it anywhere on the internet.  So I wrote my own.  It communicates with the clock system using an inexpensive RS232-ethernet bridge.  It was designed so that you could easily implement a direct serial link if you wanted to, but I never had a need for it.

Features
--------
* Supports Westerstrand WDP M+S (and possibly other) master clock systems
* Works with an RS232-ethernet bridge to provide remote access to clock system
* Update clock times and offsets for 1- or 2- clock systems
* Download and upload relay event schedules
* Easily copy and paste relay event schedules between week days
* Save and restore relay event schedules to CSV files

Setup/Use
----------
1. Connect your ethernet bridge to your network.  Configure its network settings as appropriate.  Set it for 19200, 8N1 serial communications.
2. Using the buttons on the master clock, set it to communicate over RS232 at 19200 Baud, 8N1, protocol 1.
3. With the bridge powered off, connect the bridge to your clock device.  You may need to build or buy a "DB9 to bare wire" cable in order to make the connection.
4. Power up the bridge and verify that it is connected to the network.
5. Enter the IP and port number for the bridge and click connect
6. From the main menu, click the adjust clock or adjust bells butons and make changes as desired.

There is a more detailed user manual in LibreOffice format included in this repository.

Known Issues
------------
* When sending schedules to the clock system, a delay is used to space out the packets, rather than looking for an acknowledgement from the clock after each one.  The main effect of this is that it takes slightly longer than it otherwise would to upload a complete schedule.  It's definitely not the best way to implement it, but it seems to work well enough.
* The protocol document mentions that there is a command to toggle the relay outputs manually.  I attempted to implement it, but it did not seem to work when I tested it.  It is possible it is not implemented correctly, or not available on certain units.  I didn't have a real compelling need for it, so I never pursued it further.  The user interface is still in the program, though.
* The clock system supports events which repeat Monday-Friday, Saturday-Sunday, and 7 days/week.  This is not implemented currently when uploading schedules to the clock.  You could in theory look for events in your schedule which repeat on one of these patterns and create the appropriate event type for the clock automatically when uploading, but I never implemented such a feature.  If you download a schedule containing these types of events, they will be replaced with multiple single-day events when re-uploading.
* There are a number of other commands that I didn't implement such as holidays and front panel control.
* Support for direct serial communication is partially implemented but not complete or functional.  It should be fairly straightforward to implement a direct serial class that basically drops in along side the existing network communications class.

Other Notes
-----------
A NetBeans 8.0 project is included.  You can package the application into a single JAR file by going to the files tab, expanding the Clock Controller folder, right clicking on build.xml, and choosing Run Target > Other Targets > package-for-store.  The compiled JAR file will show up in the Store folder.
The app has been tested on Mac OS X 10.10, Windows 8.1, and Windows 10  It should work on any platform with Java 7 or greater installed.

Protocol Notes
--------------
There is a file called "Westerstrand Clock Docs.pdf" included in this repository.  This is the only information I've been able to find online about the protocol these clock systems use.  The majority this PDF it isn't in English, but fortunately, the parts we're interested in are.  Protocol information starts on page 12.  It looks like it may have been OCRed at one point, as there are a few odd typos.  Most of the protocol is fairly self-explanatory, but I was disappointed to see that one of the main functions I wanted to use, the "WW" command ("Write program to master clock") did not contain a description of any of the data in it.  I was able to reverse-engineer this packet (at least the parts I cared about) after capturing some of them (requested using the "W0"/"WN" packets).  The format of this packet is as follows:
```
A A W W {xxxx} {800m} {dd} {nnnn} {hh} {mm}

xxxx=program number

{800m}=program configuration (m=output number) (800=probably related to schedule)

{dd}=duration of signal in seconds

{nnnn}=day of week
     S-S=0300
     Mon=4001
     Tues=2002
     Wed=1003
     Thu=0804
     Fri=0405
     Sat=0206
     Sun=0107
     M-F=7C08
     All=7F09

{hh}=hour of program as hexadecimal
{mm}=minute of program as hexadecimal
```
I don't believe I ever confirmed the format for the non-weekday entries as I didn't have any events that used them, but these were my best guess.  Use with caution.


Sample Captures
--------------
There are a few sample serial data captures in the "Captures" folder.  They show the request and response for a few different types of packets.  They might be useful if you're trying to understand exactly how the serial protocol works.

Licensing
-------------
This software is released under the MIT License with absolutely no warranty.  It works for me, but I can't guarantee it will work for you.  Use at your own risk.

If you use this code in another project, I would appreciate a mention in your documentation.  If you do something cool with it, I'd love to hear about it as well.