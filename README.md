# chkUpdate #

A Java library which is used to check for updates (newer versions) of a software
 hosted on sites like GitHub or SourceForge.
 
## Features ##
The library comes with Providers for GitHub and SourceForge, own Providers might
 be created by the user.
 
The library also offers two ways of checking if there is a newer version of the
 software available:
 
 * By using a "Date"-Object
 * By using the librarys "Version"-Object
 
 Both those ways are documented in the Wiki of this project. There are also Wiki-
 entry's for the standard Providers:
 
 * `GitHubProvider`
 * `SourceForgeProvider`
 
A little tutorial on how to create own Providers might also be found here.

## How to use ##
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