# Version 1.3 #



## Introduction ##

This software offers the possibility to synchronize databases hosted on different servers.

You can transfer all the tables from one to an other or just pick up some fields.

So if you need a software that selects data in a database (or table) and stores them into an other database (or table), you are in the good place!

## Requirements ##

This software has been tested on Linux (Red Hat Server, Ubuntu) and on Microsoft Windows Server 2003.

You need a [JVM](http://en.wikipedia.org/wiki/Java_Virtual_Machine) to be installed in the machine that runs the program. You can download it [here](http://www.oracle.com/technetwork/java/javase/downloads/index.html).

If this software does not work with your server, please contact us or [add an issue](http://code.google.com/p/dbsynchro/issues/entry).

## How to use this program? ##

If you want to launch the graphical version, just double click on it (or type `java -jar DbSynchroGui-x.x.jar` in a terminal).

The program is developed with [Java](http://en.wikipedia.org/wiki/Java_%28programming_language%29) can be launched like any [jar file](http://en.wikipedia.org/wiki/JAR_%28file_format%29).

You must specify both [configuration](ConfigurationFile.md) and [queries](SQLFile.md) files if you run the _non graphical_ version. Besides, you can specify other option such as the level of the logger.

### Examples ###

```
java -jar DbSynchroGui-x.x.jar
```

```
java -jar DbSynchro-x.x.jar --config config.xml --queries queries.sql --logger CONFIG
```

```
java -jar DbSynchro-x.x.jar -c config.xml -q queries.sql
```

**_OR_**

![![](http://etud.insa-toulouse.fr/~buisson/TEMP/run.png)](http://etud.insa-toulouse.fr/~buisson/TEMP/run.png)

## Libraries ##

This software is developed with [Eclipse](http://www.eclipse.org/).

Database Synchro Tool uses:
  * [The Guava Libraries](http://code.google.com/p/guava-libraries/)
  * [JavaMail](http://kenai.com/projects/javamail)
  * [MySQL Connector](http://www.mysql.com/downloads/connector/j/)
  * [Microsoft JDBC Driver for SQL Server](http://msdn.microsoft.com/en-us/sqlserver/aa937724)
  * [Swing for the graphical version](http://en.wikipedia.org/wiki/Swing_%28Java%29)