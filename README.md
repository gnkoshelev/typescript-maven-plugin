# typescript-maven-plugin
Maven plugin to compile TypeScript sources.
Plugin supports TypeScript at least version 2.1.4 and older.

## Requirements
* Required Node.js to be installed with TypeScript module (npm i -g typescript)
* Required PATH presence for tsc (check availability by typing **tsc --version**)
* Required Java 1.6+

## Usage example
```xml
<plugin>
	<groupId>ru.kgn.typescript</groupId>
	<artifactId>typescript-maven-plugin</artifactId>
	<version>0.5.1</version>
	<executions>
		<execution>
			<phase>compile</phase>
			<goals>
				<goal>tsc</goal>
			</goals>
		</execution>
	</executions>
	<configuration>
		<targetDirectory>${basedir}/target/js</targetDirectory>
		<sourceDirectory>${basedir}/src/main/webapp/WEB-INF/client/ts</sourceDirectory>
		<module>amd</module>
		<target>ES5</target>
		<sources>
			<source>application.ts</source>
		</sources>
		<sourcemap>false</sourcemap>
	</configuration>
</plugin>
```
* `targetDirectory` - directory for compiled JS-files, *required*
* `sourceDirectory` - directory with TS source files, *required*
* `module` - module code generation, *default* value is `amd`
* `target` - version of ECMAScript, *default* value is `ES5`
* `sources` - files to compile, *required*
* `sourcemap` - add TS-files and JS.MAP-files to targetDirectory, *default* value is `false`

## Installing plugin
`mvn install:install-file -Dfile=typescript-maven-plugin-0.5.1.jar`

or build and install from sources:

`mvn install`

## Additional information

* Plugin has been tested on Windows (7, 10) and Linux (Ubuntu 15.04) platforms. Linux compatibility is available since __0.5.1__.
* Plugin is compatible with both major versions of TypeScript: it has been tested with TS 1.6.2 (and older) and TS 2.1.4.
* Plugin supports only some subset of available [compiler options](https://www.typescriptlang.org/docs/handbook/compiler-options.html) but the most important subset. See [Usage example](#usage-example) section.
