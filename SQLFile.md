# SQL File #

## Introduction ##
This page is dedicated to the SQL queries file.

This file is the core of the software. It is a SQL file that contains queries which will be run on both source and target servers.

## How to write the SQL file? ##

If you get here, you need a software that selects data in a database (or table) and stores them into an other database (or table).

### Source statements ###
The first thing to do is to write a `SELECT` query that will pick up the columns you want to copy.

This query must always start with: `s:` followed by the name of the source server you defined in the [configuration file](ConfigurationFile#Database_description.md).
Do not forget to end your query with `;`.

Example:
```sql

```
s:My_Database: SELECT s.name, p.email

	       FROM personne p

	       WHERE p.age > 20;
```
```

### Target statements ###

A target (or distant) statement must directly follow the source statement it refers to.

A source statement can have as many target statements as you need.

The query must be a [CUD query](http://en.wikipedia.org/wiki/Create,_read,_update_and_delete). That is to say it must begin with `INSERT`, `UPDATE` or `DELETE`.

This query must always start with: `d:` followed by the name of the target server you defined in the [configuration file](ConfigurationFile#Database_description.md).
Do not forget to end your query with `;`.

To fetch the data you selected in the source query, write the number of the column between square-brackets: `[1]` for the name, `[2]` for the email in the example above.

Example:
```sql

```
s:Target_Database: INSERT INTO workers (name, email)
                   VALUES ('[1]',[2]);
```
```

## Complete SQL file example ##
```sql

```
--

-- MS_Server

--

s:MS_Server: SELECT s.stklevel, s.itemno

	FROM stock s

	WHERE s.type=1;

--

-- Website

--

d:RedHat_Server: UPDATE items

	SET ourstock=[1]

	WHERE itemtype=3 AND id=

	(

		SELECT av.item

		FROM attributes_values av

		WHERE av.attribute=16

		AND av.value LIKE '[2]' LIMIT 1

	) AND id NOT IN

	(

		SELECT ol.item

		FROM orders_lines ol

		JOIN orders o ON o.id=ol.ord);

--

d:Debian_Server: UPDATE products

	SET ourstock=[1]

	WHERE itemtype=3 AND id=

	(

		SELECT av.item

		FROM attributes_values av

		WHERE av.attribute=16

		AND av.value LIKE '[2]' LIMIT 1

	) AND id NOT IN

	(

		SELECT ol.item

		FROM orders_lines ol

		JOIN orders o ON o.id=ol.ord);

--

--

--

s:MS_Server: SELECT temp.level, s.itemno

		FROM stock s JOIN

		(

			SELECT a.parent itemno, FLOOR(MIN(s2.stklevel/a.qty)) AS level

			FROM dbo.access a, dbo.stock s2

			WHERE a.qty > 0 AND s2.itemno=a.itemno

			GROUP BY a.parent

		) temp ON (temp.itemno = s.itemno)

		WHERE s.type=3;

--

-- Website

--

d:Debian_Server: UPDATE products

		SET ourstock=[1]

		WHERE itemtype=3

		AND id=

		(

			SELECT av.item

			FROM attributes_values av

			WHERE av.attribute=16

			AND av.value like "[2]"

			LIMIT 1

		) AND id NOT IN

		(

			SELECT ol.item

			FROM orders_lines ol

			JOIN orders o ON o.id=ol.ord);
```
```