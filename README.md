# Unnamed Twig Extension

Improved Twig support for IntelliJ IDEA. Not even close to done yet.

Using the official Handlebars plugin as inspiration for project structure.

## TODO

- Autocomplete
    - Braces
    - User-defined macros
    - User-defined template variables
        - Local scope
        - From embed/include parents
    - Standard twig filters
    - Standard twig functions
- Fix HTML compatiblity
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

### Regenerating Flex

You will want the idea-flex.skeleton supplied by IntelliJ and an installation of JFlex.

The easiest way to do this is by just installing the Grammar Kit extension and running
the generator by right-clicking the file. This will handle everything for you. 