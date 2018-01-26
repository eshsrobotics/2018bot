2018 vision strategy
====================

What is this year's target?
---------------------------

Each target consists of two vertical, 16-inch high, 2-inch wide
retro-reflective strips spaced 4 inches apart.  (Ref:
2018FRCGameSeasonManual.pdf, p. 31.)

There is one target centered on the left platform immediately in front
of the starting area, and one target centered on the right.  (ibid.,
p. 32.)

The targets represent the center of the nearest wall of the platforms
that face the robots during autonomous mode.  We can use these targets
to drive up to the platform and ensure that we can get a box into the
target before the 15-second period has passed.


Vision strategy
===============

We wish to drop a pre-loaded box into the appropriate vision target in
front of the starting area within 15 seconds.

Using either information from the field API (TODO: identify link to
API call) or a manual switch setting, the robot determines which of
the three starting positions it is located in.  Using the same API,
the robot will also determine which platform the pre-loaded cube must
be taken to.

The robot should use this combination of factors to determine in which
direction to aim for the vision target.  (Remember that there can
potentiall be two in the camera's field of view at once.)  Then we use
the PapasVision code to minimize the PapasAngle and bear toward the
target until we have reached a threshold distance.

At the apropriate distance, we activate the solenoid launcher and fire
the cube in a parabola.  Hopefully it will make it in.


What about dead reckoning?
==========================

That is a viable strategy this year due to the close proximity of the
platforms to the robots.  Care will need to be taken to ensure that we
don't collide with other friendly robots that are also moving during
autonomous mode.

Changes from 2017bot
--------------------

-   No third-party vision subsystem like the TK1 \* Ergo, no networking problems with the two-port roboRIO
-   Vision processing on driver station
-   **?** Use NetworkTables to talk back to the roboRIO more simply

What hasn't changed
-------------------

-   **?** Vision processing code still written in C++ \* Advantage :: Much less rewriting \* Disadvantage :: NetworkTables in C++ is apparently hard. Not sure why.
-   Vision solutions are still sent to the roboRIO from whoever is calculating them
-   RoboRIO still cares only about solutionFound, papasDistance, and papasAngle
-   Vision targets are two vertical strips again (adjusted peg solution)

New challenges
--------------

1.  **How** will we get the camera image from the roboRIO to the driver station?
	* UA: How does GRIP do it? Find out, then do what it's doing. \*
      The only alternative to this is vision processing on the robot,
      which ~~even GRIP won't do.~~ ?
2.  **How** do we build NetworkTables for C++?
3.  **How** do we test NetworkTables independently of the local presence of the robot?
4.  We're supposed to receive a signal from the field that tells us
    which of the two platforms we need to drop boxes in.  **How** do we
    program that?

GRIP research
-------------

We're mostly interested in the code that GRIP generates.

* [Introduction to GRIP](https://wpilib.screenstepslive.com/s/4485/m/24194/l/463566-introduction-to-grip)
* The HttpCamera seems to be part of WpiLib, which includes [these files](https://pdocs.kauailabs.com/vmx-rtk/software/wpi-camera-server-cscore/). 
  - We can use the HttpCamera to stream camera images from the
    roboRIO.  This does not seem to not be a part of the VisionBuild
    code, and there are both Java and C++ examples.

NetworkTables research
----------------------

* [Basic NetworkTables programming in Java](http://wpilib.screenstepslive.com/s/3120/m/7912/l/80205-writing-a-simple-networktables-program-in-c-and-java-with-a-java-client-pc-side)
