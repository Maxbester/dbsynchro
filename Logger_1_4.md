Version 1.4

# Logger #

## Introduction ##

You may want to save a history of what the software does.

This can be done thanks to a logging feature. Information will be stored in a log file.

This is useful when this program is runned by a task scheduler ([cron](http://en.wikipedia.org/wiki/Cron) or [at](http://en.wikipedia.org/wiki/At_%28Windows%29) for example).

Since the previous version, the logging system has changed. It is based on the famous [Apache Log4j](http://logging.apache.org/log4j/).

## How to use it? ##

When running the _non graphical version_ of the software, the logging definition is made using a specific Log4j configuration file. You have to give its path as an argument:

```
--logger logger.properties
```

**OR**

```
-l logger.properties
```


Here is a complete example:
```
java -jar DbSync-x.x.jar --config config.xml --queries queries.sql --logger logger.properties
```

You can find information about how to write those kind of files here: [IBM: Working with the Apache log4j Configuration File](http://publib.boulder.ibm.com/infocenter/p8docs/v4r5m1/index.jsp?topic=%2Fcom.ibm.p8.doc%2Fdeveloper_help%2Fcontent_engine_api%2Fguide%2Flogging_procedures.htm)

By default, the logging configuration is:
```

log4j.rootLogger=info, stdout, R

log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout

# Pattern to output the caller's file name and line number.
log4j.appender.stdout.layout.ConversionPattern=%5p (%F:%L) - %m%n

log4j.appender.R=org.apache.log4j.RollingFileAppender
log4j.appender.R.File=DbSyncTool.log

# Max file size
log4j.appender.R.MaxFileSize=1MB
# Backup file number
log4j.appender.R.MaxBackupIndex=3

log4j.appender.R.layout=org.apache.log4j.PatternLayout
log4j.appender.R.layout.ConversionPattern=%d %5p (%F:%L) - %m%n
```

## Files ##

By default, logging information is appended to the previous file.
When the size file reaches 3Mb, a new logging file is created. Up to five files can be created.