# Logger #

## Introduction ##

You may want to save a history of what the software does.

This can be done thanks to a logging feature. Information will be stored in a log file.

This is useful when this program is runned by a task scheduler ([cron](http://en.wikipedia.org/wiki/Cron) or [at](http://en.wikipedia.org/wiki/At_%28Windows%29) for example).

## How to use it? ##

With the _graphical_ version:

<img src='http://etud.insa-toulouse.fr/~buisson/TEMP/home.png' alt='Graphical version' />

When running the _non graphical version_ of the software, the logging level can be specified with:
```
-l LEVEL
```
**or**
```
-logger LEVEL
```

### Levels ###

The token `LEVEL` must be replaced by either (in descending order):
| OFF | No log (equivalent to nothing) |
|:----|:-------------------------------|
| SEVERE | Only severe errors will be logged |
| WARNING | Sever errors and warnings will be logged |
| INFO | Same as above plus some information |
| CONFIG | Same as above plus executed queries in both source and target databases |
| FINE | Fine level (for debug mostly) |
| FINER | Finer level (for debug mostly) |
| FINEST | Finest level (for debug mostly) |
| ALL | Every activity is logged (not recommended) |

## Files ##

When running the _graphical_ version of the software, the logging file is erased at each new instance.

When running the _non graphical_ version of the software, logging information is appended to the previous file.
When the size file reaches 3Mb, a new logging file is created. Up to five files can be created.