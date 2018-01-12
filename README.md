# (WIP) idea-community-twig ![Travis](https://api.travis-ci.org/fiskie/idea-community-twig.svg?branch=master)

(WIP) A plugin to replace the official closed-source Twig plugin found in IntelliJ IDEA/PhpStorm, adding enhanced functionality
such as support for Find Usages and named element refactors.

## Differences with the official plugin

* Code style is extended from the current HTML code style settings. This means your indents will be determined by the HTML indent settings.

## TODO

### Milestone 1: Feature parity with the official Twig plugin

- [x] Autocompletion for end braces
    - [x] Statements (`{% %}`)
    - [x] Expressions (`{{ }}`)
    - [x] Comments (`{% %}`)
- [x] Composite Twig/HTML structure view
- [x] Live templates
- [x] Remove additional whitespace around braces during code cleanup
- [x] Code folding
- [x] Prioritise end tag hinting for close statements
- [ ] Continuation indent for expressions
- [ ] Formatting/code style configuration
    - [x] Statement block indenting
    - [ ] Hard wrap
    - [ ] Wrap on typing
    - [ ] Visual guides
    - [ ] Spaces inside statement braces toggle
    - [ ] Spaces inside expression braces toggle

### Milestone 2: Supplementary PSI features

- [x] Breadcrumbs
- [ ] Line mover: Move entire statement block if caret on start/end tag (add StatementUpDownMover)
- [x] Whitespace control modifier (`{{-`) paired brace autocomplete -- currently only removes on backspace press, where is the delete handler?
- [ ] Annotations/Inspections
    - [ ] Expected end block tag -- doing while parsing has a coalescence problem and should probably be implemented as an annotation
    - [ ] Declared child blocks not found in the parent template
    - [x] Unclosed comment inspection
    - [x] Anything other than a `block` in an `embed`/`extends`
- [ ] Reference support - Find usages and name refactor for the following:
    - [x] Variables - basic, unstable -- matches to property and block names right now; expression PSI needs improving.
    - [ ] Macro names
    - [ ] Scoped variables in a `for` block
    - [ ] Scoped variables in a `macro`
    - [ ] Add tests
- [ ] Automatic matching tag rename on type a la HTML plugin
- [ ] Add the special `loop` object when in `for` context
- [x] Auto-collapse Twig blocks as a folding option
- [ ] String iterpolation (`#{foo}, #{1 + 2}`)

### Milestone 3: Feature parity with community Symfony plugin Twig extensions

- [ ] Goto parent block
- [ ] Template include/extends/block resolution goto
- [ ] Parent block implements/extends goto on line marker
- [ ] Macro goto on line marker
- [ ] PHPDoc annotation support ({# @var thing \Core\Thing #})

## Development

Contributing to this project will require an environment ready to develop IDEA plugins. [Read the guide](https://www.jetbrains.com/help/idea/configuring-intellij-platform-plugin-sdk.html)

This is a Gradle project, so import `gradle.build` and it should do everything for you without additional configuration. 

Make sure all tests pass, then make a pull request. If Travis fails to build your PR, please rectify any problems.
If you are writing a new feature, it is vital that you add sufficient tests! 