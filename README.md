# WXS Generator Maven Plugin

The WXS Generator Maven Plugin generates a WXS file from your maven project. 
WXS Files serve as Input for the Windows Installer XML Toolset (https://wixtoolset.org). 

## Requirements
In order to create an installer from a Maven Project, all files to be installed must be assembled into a distribution directory (e.g. which needs to be set in the configured rootPath).
The Project must be assembled in a way that it might be started from the distribution directory. I personally recommend using the maven-assembly-plugin for this task (http://maven.apache.org/plugins/maven-assembly-plugin/).   

## Goals

generate-wxs  

## Usage

    <build>
        <plugins>
            <plugin>
                <groupId>com.ptrxwsmitt</groupId>
                <artifactId>wxs-generator-maven-plugin</artifactId>
                <version>0.5</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>generate-wxs</goal>
                        </goals>
                        <configuration>
                            <productUid>54e94bd2-44bb-4b88-99d4-944d1a1a4af5</productUid>
                            <rootPath>target/dist</rootPath>
                            <targetFile>target/dist/MyAppSetup.wxs</targetFile>
                            <mainExecutable>runMyApp.bat</mainExecutable>
                            <iconPath>myApp.ico</iconPath>
                            <installerLocale>en-us</installerLocale>
                            <licenceRtfPath>LICENCE.rtf</licenceRtfPath>
                            <manufacturer>My Company</manufacturer>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    <build>

## Configuration

The plugin will automatically create a wsx file with the installer definition .\
The installer will automatically create a desktop link, which is not configurable yet!

### Parameters

##### productUid (mandatory)

Give your product a UID (I recommend using a UUID Generator).

##### productName
Defaults to maven ${project.name}.

##### productVersion
Defaults to maven ${project.version}. Suffix "-SNAPSHOT" will be removed automatically.\
The Version must match regex [0-9]+(\\.[0-9]+)*; WIX Toolset will fail otherwise.
- Valid examples: 1.3.5, 2019.3
- Invalid examples: 2.3a, 0.5-SNAPSHOT

##### productDescription
Defaults to maven ${project.description}.

##### manufacturer (mandatory)
Defaults to maven ${project.organization.name}.

##### rootPath (mandatory)
Path to the directory which contains all files in the required structure of the target installation (e.g. assembled by maven assembly plugin).

##### targetFile
Filename of the wxs-file to be generated (e.g. "installer.wxs").

##### mainExecutable
Path to the main Executable (e.g. *.bat file) relative to rootPath   

##### iconPath
Path to the application's Icon (*.ico) relative to rootPath

##### licenceRtfPath
Path to Licence (*.rtf) relative to rootPath

##### provideBuildBatch
Decide whether you want the plugin to generate a "buildInstaller.bat" file for easy integration into your build process. This requires the WIX Toolset commands "candle" and "light" to be available within your systems PATH environment variable.

##### dialogBackground
Path to the Background Image for the welcome and finish screens (493 x 312)

##### bannerTop
Path to the Banner Image for banner on the top (493 x 58)

## Licence Information
MIT Licence (see LICENCE File)

## Changelog

##### 2019-07-17 Initial Version