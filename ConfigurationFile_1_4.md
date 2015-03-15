Version 1.4

# Configuration file #




## Introduction ##
This page is dedicated to the configuration file.

It is a XML file that contains information about:
  * databases you want to modify
  * email for notification


## How to write the configuration file? ##

If you downloaded archive of the last version of the software, you already have an example of a valid configuration file.

The configuration file must begin with a XML header:
```xml

<?xml version="1.0" encoding="UTF-8"?>
```

Feel free to change the XML version and the encoding.

Then, all the configuration must be placed between a "config" tag:
```xml

<config>

Unknown end tag for &lt;/config&gt;


```

### Database description ###
You can as many databases as you want. The minimum is two: one source and one target.
The source and the target can be the same but must have a different name.

Attributes must be enclosed by: ```xml

<database>

Unknown end tag for &lt;/database&gt;


```

Tags are specified in the table bellow:
| **Name** | **Description** | **Example** |  **Required** |
|:---------|:----------------|:------------|:--------------|
| name | The name of the database which will be used for the statements (must not contain any space) | ```xml
<name>My_Database

Unknown end tag for &lt;/name&gt;

``` | yes |
| url | The protocol, URL, port and name of the database on your server | ```xml
<url>jdbc:mysql://localhost:3306/dbName

Unknown end tag for &lt;/url&gt;

``` | yes |
| driver | Name of the driver corresponding to your database type (currently only MySQL and MSSQL are available) | ```xml
<driver>com.mysql.jdbc.Driver

Unknown end tag for &lt;/driver&gt;

``` | yes |
| login | The user login for the connection to the database (this is not the login of a server user but of a database user) | ```xml
<login>admin

Unknown end tag for &lt;/login&gt;

```  | yes |
| password | The user login for the connection to the database (this is not the login of a server user but of a database user) | ```xml
<password>your_password

Unknown end tag for &lt;/password&gt;

```  | yes |

#### Driver information ####
Currently only MySQL and MSSQL are available but if you need other database driver, please feel free to [add an issue](http://code.google.com/p/dbsynchro/issues/entry).

  * MySQL driver: **com.mysql.jdbc.Driver**
  * MSSQL driver: **com.microsoft.sqlserver.jdbc.SQLServerDriver**

#### MSSQL driver ####
With MSSQL you can run several instances on the same server. To get further information, visit: [Building the Connection URL](http://msdn.microsoft.com/en-us/library/ms378428.aspx).

The URL syntax is:
```
jdbc:sqlserver://[serverName[\instanceName][:portNumber]][;property=value[;property=value]]
```

#### MySQL driver ####

To get further information, visit: [URL Syntax and Configuration Properties](http://dev.mysql.com/doc/refman/5.0/en/connector-j-reference-configuration-properties.html).

The URL syntax is:
```
jdbc:mysql://[host:port],[host:port].../[database][?propertyName1][=propertyValue1][&propertyName2][=propertyValue2]...
```

### Email notification description ###
When running the _non graphical version_ of the software, emails can be sent to warn that an error occurred.

This feature is useful when this program is run by a task scheduler ([cron](http://en.wikipedia.org/wiki/Cron) or [at](http://en.wikipedia.org/wiki/At_%28Windows%29) for example).

**But you are not obliged to specify email configuration if you do not want to receive emails.**

Attributes must be enclosed by:
```xml

<email>

Unknown end tag for &lt;/email&gt;

```

Tags are specified in the table bellow:
| **Name** | **Description** | **Example** |  **Required** |
|:---------|:----------------|:------------|:--------------|
| smtp | The SMTP address of the server. The port must be placed in attribute | ```xml
<smtp port="25">smtp.myserver.com

Unknown end tag for &lt;/smtp&gt;

``` | yes |
| from | The sender email address | ```xml
<from>DbSynchro@domain.com

Unknown end tag for &lt;/from&gt;

``` | yes |
| subject | The subject of the email you will receive | ```xml
<subject>[DbSynchro] An error occurred

Unknown end tag for &lt;/subject&gt;

``` | no |
| recipient | Email address of a recipient. You can as many recipients as you want. | ```xml
<login>you@domain.com

Unknown end tag for &lt;/login&gt;

```  | at least one |

## Complete configuration file example ##
```xml

```
<?xml version="1.0" encoding="UTF-8"?>
<config>
	<email>
		<smtp port="25">localhost</smtp>
		<from>dbSynchro@gmail.com</from>
		<subject>[DbSynchroTool] Synchronisation failed</subject>
		<recipient>your_email@gmail.com</recipient>
                <recipient>another_email@gmail.com</recipient>
	</email>
	<database>
		<name>MS_Server</name>
		<url>jdbc:sqlserver://localhost:1434;instance=instanceName;databaseName=db1</url>
		<login>admin</login>
		<password>admin</password>
		<driver>com.microsoft.sqlserver.jdbc.SQLServerDriver</driver>
	</database>
	<database>
		<name>RedHat_Server</name>
		<url>jdbc:mysql://localhost/db1</url>
		<login>root</login>
		<password>redhat</password>
		<driver>com.mysql.jdbc.Driver</driver>
	</database>
	<database>
		<name>Debian_Server</name>
		<url>jdbc:mysql://localhost/database</url>
		<login>root</login>
		<password>debian</password>
		<driver>com.mysql.jdbc.Driver</driver>
	</database>
</config>
```
```