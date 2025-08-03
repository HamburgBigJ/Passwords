# Passwords - Minecraft Plugin

Passwords is an innovative and user-friendly plugin designed to enhance the security of your Minecraft server. It allows both players and administrators to set personal passwords, ensuring a secure and personalized login experience. With its intuitive graphical user interface (GUI), managing passwords becomes seamless for everyone.

## Features

- **Custom Passwords for Each Player**  
  Players can create unique passwords, ensuring personalized and secure access to their accounts.

- **Adjustable Password Lengths**  
  Customize the password length to achieve the desired balance between security and convenience.

- **Seamless Integration**  
  Works effortlessly with any Minecraft server without requiring complex setup procedures.

## Upcoming Features

- **Enhanced Customization**  
  Future versions will introduce additional options for tailoring the plugin to meet diverse server needs.

## Installation

1. **Download the Plugin**  
   Obtain the latest version of the Passwords plugin from the [Modrinth website](https://modrinth.com/plugin/passwords).

2. **Add to Server Plugins Folder**  
   Place the downloaded plugin file into the `plugins` directory of your Minecraft server.

3. **Restart the Server**  
   Restart your server to allow the plugin to initialize and function correctly.

## Why Choose Passwords?

- **Enhanced Server Security**  
  Protect your server from unauthorized access with customizable password settings.

- **Personalized Player Experience**  
  Allow players to secure their accounts, enhancing their gameplay experience.

## Developer Resources

For developers interested in integrating or extending the Passwords plugin, Maven and Gradle setups are available:

**Maven:**

```xml
<dependency>
    <groupId>cho.info</groupId>
    <artifactId>passwords</artifactId>
    <version>VERSION</version>
</dependency>

<repositories>
    <repository>
        <id>local-repo</id>
        <url>file:/path/to/my-local-repo</url>
    </repository>
</repositories>
```

**Gradle:**

```groovy
implementation files("$projectDir/lib/passwords-VERSION.jar")
```


## License

Passwords is licensed under the MIT License, allowing for free use, modification, and distribution. For more details, see the [LICENSE](https://github.com/HamburgBigJ/Passwords/blob/main/LICENSE) file in the repository.

Enhance your Minecraft server's security and provide a personalized experience for your players with Passwords! 
