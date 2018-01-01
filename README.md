# (WIP) intellij-community-twig

(WIP) A plugin to replace the official Twig plugin found in IntelliJ.

This is not production ready yet!! A lot of code has been sampled from the Handlebars plugin; this will eventually be replaced to suit Twig as I learn more about how IntelliJ works. 

## Why this exists

The official IntelliJ Twig plugin is pretty barebones. There are also some weird bugs that cause indentation to go wonky. But it's closed source and we can't do anything about it.

The Symfony plugin extends it with some nice features such as PHPDoc, but it's unable to add features like syntax validation without fully reimplementing the parser.

I would love to bridge the gap -- make the official plugin obsolete, and incorporate the extra features from the Symfony plugin. 

## TODO

- Autocomplete
    - Braces
    - User-defined macros
    - User-defined template variables
        - Local scope
        - From embed/include parents
    - Standard twig filters
    - Standard twig functions
- ~~HTML interop~~
- Syntax highlighting
    - ~~Support the whitespace control modifier ({{-, {%-, {#-)~~
    - Brace matching 
    - ~~Block matching (block and endblock, etc)~~ -- kind of there
- Inspections
    - Syntax errors
    - Declared child blocks not found in the parent template
- Rename handler, names validator ([Rename refactoring](https://www.jetbrains.org/intellij/sdk/docs/reference_guide/custom_language_support/rename_refactoring.html))
- Find usages ([Find usages](https://www.jetbrains.org/intellij/sdk/docs/reference_guide/custom_language_support/find_usages.html))
- PHPDoc annotation (`{# @var thing \Core\Thing #}`)
- Folding
- Block parent goto
- Template include resolution
- Override/implement block generation
- More tests for indenting (strange bug with two simple statements, second one not indenting at all?)
 
    
## Development

Contributing to this project will require an environment ready to develop IntelliJ plugins. [Read the guide](https://www.jetbrains.com/help/idea/configuring-intellij-platform-plugin-sdk.html)

This is a Gradle project, so import `gradle.build` and it should do everything for you without additional configuration. 

Make sure tests pass, then make a pull request. If Travis fails to build your PR, please make additional changes.