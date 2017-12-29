# Unnamed Twig Intellij Plugin

The official IntelliJ Twig plugin is closed-source and lacks features which I'd like to see, so I wanted to do something about it.  

Based on the official Handlebars plugin, since I'm completely new to IntelliJ plugin development and it's the closest thing I had, I suppose. 

## TODO

- Autocomplete
    - Braces
    - User-defined macros
    - User-defined template variables
        - Local scope
        - From embed/include parents
    - Standard twig filters
    - Standard twig functions
- HTML interop
- Syntax highlighting
    - Support the whitespace control modifier ({{-, {%-, {#-)
    - Brace matching 
    - Block matching (block and endblock, etc)
- Inspections
    - Syntax errors
    - Declared child blocks not found in the parent template
- Extension support for popular third party Twig libraries (e.g. twig-cache-extension, html-compress-twig) 
- Long-term goal: PHP controller property reflection
    - Resolve the types of variables passed to a template
    
## Development

Contributing to this project will require an environment ready to develop IntelliJ plugins. [Read the guide](https://www.jetbrains.com/help/idea/configuring-intellij-platform-plugin-sdk.html)

Make sure tests pass, then make a pull request. If Travis fails to build your PR, please make additional changes.