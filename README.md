JobHunter
=========

Open Source Jobs tracking tool.

This project is abandoned in favor of [https://github.com/Eldelshell/jobhunter-chrome-app](Jobhunter for Google Chrome)

It's still a good example of a JavaFX application with a Maven build process and a simple plugin system.

### Main view with some jobs
![http://i.imgur.com/Ns3RhgG](http://i.imgur.com/Ns3RhgG.png)

### Dialog to add/edit job positions
![http://i.imgur.com/wp9WvKt](http://i.imgur.com/wp9WvKt.png)

### Main view with some RSS feeds
![http://i.imgur.com/SlF4zBb](http://i.imgur.com/SlF4zBb.png)

The idea is to ease the burden of managing/accessing data about your job applications in different portals.

The application can be extended with plugins to extract jobs from different portals using APIs or scrapping.

The things you can keep track of are:

* Job position, salary, description and rating.
* Company information.
* Contacts related to the position (HeadHunter, HR representative, etc.)
* Events like when you have/had an interview.

JobHunter also works as a RSS feed reader which you can use to consume the feeds provided
by different job portals to stay always up-to-date with new jobs.

## Installation Windows

You'll need Java Version 8_20 or later.

Download the file `jobhunter-X.X.X.jar` from [releases](https://github.com/Eldelshell/JobHunter/releases) page and double click on it.

_If double click doesn't work, follow this instructions:_ [StackOverflow](http://stackoverflow.com/a/8511277/48869)

### Custom Installation

In case you don't want all the plugins included, download the `jobhunter-0.1.2-distribution.zip` file from [releases](https://github.com/Eldelshell/JobHunter/releases) and unzip it in your `C:` drive. Then run with:

```
C:> cd C:\jobhunter-X.X.X
C:> java -cp lib\* jobhunter.gui.MainApp
```


## Installation Linux/Mac

You'll need Java Version 8_20 or later.

Download the file `jobhunter-X.X.X.jar` from [releases](https://github.com/Eldelshell/JobHunter/releases) page and double click on it.

If double click doesn't work use `java -jar jobhunter-X.X.X.jar`

_In MacOS you'll get a warning because the application is not signed. This doesn't mean it's not secure, but that we don't have an Apple developer account to get a certificate to sign applications for MacOS_

### Custom Installation

In case you don't want all the plugins included, download the `jobhunter-X.X.X-distribution.zip` file from Releases and unzip it in any folder. Then run with:

```
$ cd path/to/jobhunter-X.X.X
$ ./jobhunter
```

## Plugins

Plugins are used to import a job posting from different portals.

Plugins can be added by placing them in the `lib` folder.

Currently you have plugins for:

* Monster
* Dice.com
* CareerBuilder
* InfoEmpleo (Spain)
* TecnoEmpleo (Spain)
* InfoJobs (Spain)
* StackOverflow Careers
* Craiglist

## JHF Files

JobHunter stores data in JHF files, which are a ZIP file with some XML files inside. This files can be added to cloud drives (Box, Dropbox, Drive) so you can share them between devices.

## Development

Clone the repo and import the Maven projects in your IDE. Set the following parameters when running: `-Ddebug=true -Ddevelopment=true`

### Plugins

Creating your own import plugin is very easy and most of the tools are already provided:

* Commons HTTP (HTTP Client)
* jSOUP (Parse HTML)
* XStream (Parse XML)
* Jackson (Parse JSON)

It would be better if you didn't include any dependency, since all of them have to be addded to the `lib` folder, making installation a little harded.

Simply implement the `Plugin` interface and start coding. Take a look at the plugins I've already developed to see how they work.
