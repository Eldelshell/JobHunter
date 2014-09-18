JobHunter
=========

Open Source Jobs tracking tool. 

![Imgur](http://i.imgur.com/xjHQPYp.png)

![Imgur](http://i.imgur.com/Jnwhjug.png)

The idea is to ease the burden of managing/accessing data about your job applications in different portals.

The application can be extended with plugins to extract jobs from different portals using APIs or scrapping.

The things you can keep track of are:

* Job position, salary, description and rating.
* Company information.
* Contacts related to the position (HeadHunter, HR representative, etc.)
* Events like when you have/had an interview.

JobHunter also works as a RSS feed reader which you can use to consume the feeds provided
by different job portals to stay always up-to-date with new jobs.

## Installation

You'll need Java Version 8_20 or later.

Download the latest ZIP file from Releases and execute the `jobhunter` script.

To run the application in debug mode execute `./jobhunter debug`

### Plugins

Plugins are used to import a job posting from different portals.

Plugins can be added by placing them in the `lib` folder.

Currently you have plugins for:

* Monster
* Dice.com
* CareerBuilder
* InfoEmpleo (Spain)
* TecnoEmpleo (Spain)
* InfoJobs (Spain)

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
