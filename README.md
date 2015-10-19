# Papa Pojo
Papa Pojo is command line tool for Java developers. It generates code files with classes, interfaces and enums that follow common best practices and design patterns. Using __extremely simple__ JSON formatted templates as source files it has almost no learning curve.  
By automating some of the more mundane and error prone Java coding tasks it increases developer productivity and helps enforce standard naming conventions. It also better protects your code from schema migration errors by surfacing problems earlier at compile time.  
#### Supported Tasks ####
* Pojo class
* Pojo shallow copy
* Read write interface
* Read only interface
* Immutable class
* Fluent builder class
* Field enum

#### Benefits Of Using Papa Pojo ####
* Easy to learn and set up.
* Many options to customize code output.
* Detailed __[Wiki](https://github.com/gary-harpaz/Papa-Pojo/wiki)__ and code samples.
* Very strict template parser with comprehensive error messages.

## FAQ ##
#### What's the difference between Papa Pojo and other text template engines?
They are completely different. Other template engines out there are all purpose solutions. As such they have a steep learning curve. Papa Pojo handles specific Java coding tasks and best practices. It's just not meant to solve the same problems.  
#### What happens if I want custom methods or fields, then I'm stuck right?
Not really. You can use Papa Pojo to create base classes and extend them with your own functionality. Also by overriding the base class methods you can completely customize the generated code to do whatever you need it to do.
#### I'm still not convinced.
Maybe you should take a look at a simple __[Template Example](https://github.com/gary-harpaz/Papa-Pojo/wiki/Example)__, or perhaps you just lack the proper __[Motivation](https://github.com/gary-harpaz/Papa-Pojo/wiki/Motivation)__?
## Project ##
#### Author ####
[Gary Harpaz](https://github.com/gary-harpaz)
#### License ####
Code is under the [Apache Licence v2](https://www.apache.org/licenses/LICENSE-2.0.txt).

