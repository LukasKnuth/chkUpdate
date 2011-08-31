# chkUpdate #

A Java library which is used to check for updates (newer versions) of a software
 hosted on sites like GitHub or SourceForge.
 
## Features ##
The library comes with a few simple Providers, own Providers might
 be created by the user, too.
 
The library also offers two ways of checking if there is a newer version of the
 software available:
 
 * By using a "Date"-Object
 * By using the librarys "Version"-Object
 
Both those ways are 
 [documented in the Wiki](https://github.com/LukasKnuth/chkUpdate/wiki/Basic-How-To)
 of this project. There are also Wiki-entry's for the 
 [standard Providers](https://github.com/LukasKnuth/chkUpdate/wiki/Standard-Providers).

A little tutorial on how to 
 [create own Providers](https://github.com/LukasKnuth/chkUpdate/wiki/Create-your-own-Provider)
 might also be found in the Wiki.

### Used projects ###
This library uses [JSON-java](https://github.com/douglascrockford/JSON-java) by
 [Douglas Crockford](https://github.com/douglascrockford) to parse JSON-Strings.

## How to build ##
The library ships with SourceCode and Documentation only!

The provided Ant-buildscript can be used to create a `.jar`-file for easy integration
 in your Java projects.
 
## ToDo list ##
Those are things which might or might not be implemented in the near/not so near future:

 * A "Google Code"-Provider
 * Some standard Providers for reading XML or CSV-files.
 
## Changelog ##

### Version 0.1 ###
First release of the library.