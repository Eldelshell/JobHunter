JobHunter
=========

Open Source Jobs tracking tool. 

![Imgur](http://i.imgur.com/XUUXoJm.png)

![Imgur](http://i.imgur.com/7L3oXZW.png)

The idea is to ease the burden of managing/accessing data about your job applications in different portals.

For some portals (those which provide a public API) the application can be integrated via a simple plugin system to extract job offers.

## Installation

You'll need Java Version 8 or later.

Download the latest ZIP file from Releases and execute the `jobhunter` script.

Plugins can be added by placing them in the `lib` folder.

You can run the application in debug mode with `./jobhunter debug`

## JHF Files

JobHunter stores data in JHF files, which are a ZIP file with some XML files inside. This files can be added to cloud drives (Box, Dropbox, Drive) so you can share them between devices (Android App coming soon)

## Development

Clone the repo and import the Maven projects in your IDE. Set the following parameters when running:

```
-Ddebug=true -Ddevelopment=true
```
