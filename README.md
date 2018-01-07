# (WIP) idea-community-twig ![Travis](https://api.travis-ci.org/fiskie/idea-community-twig.svg?branch=master)

(WIP) A plugin to replace the official closed-source Twig plugin found in IntelliJ IDEA/PhpStorm, adding enhanced functionality
such as support for Find Usages and named element refactors. 

## TODO

### Milestone 1: Feature parity with the official Twig plugin

- [x] Autocompletion for end braces
    - [x] Statements (`{% %}`)
    - [x] Expressions (`{{ }}`)
    - [x] Comments (`{% %}`)
- [x] Composite Twig/HTML structure view
- [x] Live templates
- [x] Code folding
- [x] Statement block indenting
- [ ] Multi-line expression hash indenting
- [ ] Prioritise end tag hinting for close statements
- [ ] Remove additional whitespace in code cleanup
- [ ] Code style configuration page
    - [ ] Hard wrap
    - [ ] Wrap on typing
    - [ ] Visual guides
    - [ ] Spaces inside statement braces toggle
    - [ ] Spaces inside expression braces toggle
- [ ] Reindent lines on line move up/down

### Milestone 2: Supplementary PSI features

- [x] Breadcrumbs
- [ ] Line mover: Move entire statement block if caret on start/end tag (add StatementUpDownMover)
- [ ] Update whitespace control modifier on paired brace
    - [ ] Improve removal - currently only works on backspace press (where is the delete handler?)
- [ ] Annotations/Inspections
    - [ ] Expected end block tag -- protection against coalescence is desired, but tricky
    - [ ] Declared child blocks not found in the parent template
    - [ ] Unclosed comment inspection
- [ ] Reference support - Find usages and name refactor for the following:
    - [x] Variables - basic, unstable -- matches to property names right now, expression PSI needs improving.
    - [ ] Macro names
    - [ ] Scoped variables in a `for` block
    - [ ] Scoped variables in a `macro`
    - [ ] Add tests
- [ ] Automatic matching tag rename on type a la HTML plugin

### Milestone 3: Feature parity with community Symfony plugin Twig extensions

- [ ] Goto parent block
- [ ] Template include/extends/block resolution goto
- [ ] Parent block implements/extends goto on line marker
- [ ] Macro goto on line marker
- [ ] PHPDoc annotation support ({# @var thing \Core\Thing #})

## Development

Contributing to this project will require an environment ready to develop IntelliJ plugins. [Read the guide](https://www.jetbrains.com/help/idea/configuring-intellij-platform-plugin-sdk.html)

This is a Gradle project, so import `gradle.build` and it should do everything for you without additional configuration. 

Make sure tests pass, then make a pull request. If Travis fails to build your PR, please make additional changes.
