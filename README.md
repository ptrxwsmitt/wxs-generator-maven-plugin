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
