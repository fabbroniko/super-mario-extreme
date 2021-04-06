# super-mario-extreme

A custom made version of the famous platformer made with Swing in Java. 
This is an old project I completed years ago and I'm picking it up again to do some improvements and share it with people that would like to make their first simple 2D game without having to dive in OpenGL.

Working with Swing rather than OpenGL comes with performance penalties, but for a game as simple as this it won't make too much of a difference and you can expect to have a working prototype quite quickly. 
For those interested in OpenGL I will create the same game using LWJGL once I'm done finishing this one up.

# Current and Future Development

The game itself is quite simple and minimalistic, so far there is only one level with some bugs here and there but I'm working towards:
- Removing the existing bugs;
- Improved graphics with higher resolution sprites and tilemaps drawing on a bigger base canvas.
- Improve overall user experience.
- Refactor (this was one of my first JAVA projects many years ago therefore it can use some code improvements)
- Add more GameObjects and enemies.
- Create an in-game editor to build custom levels.
- A WebAPP to share these custom levels. The game will be able to browse through the repository and play any level shared by any other player.

# Development Requirements:
- Java 11 JDK Installed 
- Your favourite IDE, I like IntelliJ IDEA

If you see a ClassNotFound exception or a similar ClassLoader expeptions, just make sure both `src` and `resources` directories are in your `Build Path`.
