# (WIP) idea-community-twig ![Travis](https://api.travis-ci.org/fiskie/idea-community-twig.svg?branch=master)

(WIP) A plugin to replace the official closed-source Twig plugin found in IntelliJ IDEA/PhpStorm, adding enhanced functionality
such as support for Find Usages and named element refactors.

## Current functional differences with the official plugin

* Code style is extended from the current HTML code style settings. This means your indents will be determined by the HTML indent settings.

## TODO

### Milestone 1: Feature parity with the official Twig plugin

- [x] Autocompletion for end braces
- [x] Composite Twig/HTML structure view
- [x] Live templates
- [x] Remove additional whitespace around braces during code cleanup
- [x] Code folding
- [x] Prioritise end tag hinting for close statements
- [x] Formatting/code style configuration 
- [ ] Allow a closing brace for a hash to be automatically inserted if not expecting an expression brace
- [ ] Continuation indent for expressions
- [ ] File path referencing
- [x] `for` `if` filter keyword recognition
- [ ] ~~Block ctrl-click goto~~ - will use line markers instead

### Milestone 2: Supplementary PSI features

- [x] Breadcrumbs
- [ ] Line mover: Move entire statement block if caret on start/end tag (add own StatementMover)
- [x] Whitespace control modifier (`{{-`) paired brace autocomplete -- currently only removes on backspace press, where is the delete handler?
- [ ] Annotations/Inspections
    - [x] Mismatched end block tag
    - [ ] Declared child blocks not found in the parent template
    - [x] Unclosed comment inspection
    - [x] Anything other than a `block` in an `embed`/`extends`
    - [x] Anything other than a `include` in a `sandbox`
    - [x] Unexpected inverse tags
- [x] Annotation fixes
    - [x] Mismatched twig tag
- [ ] Reference support - Find usages and name refactor for the following:
    - [x] Local variables
    - [ ] Tags - rename matching tag
    - [ ] Block names
    - [ ] Macro names
    - [ ] Scoped variables in a `for` block
    - [ ] Scoped variables in a `macro`
    - [ ] Variables declared in a parent block, recursively
    - [ ] Add tests
- [x] More color scheme options 
- [ ] Automatic matching tag rename on type a la HTML plugin
- [x] Add the special `loop` object when in `for` context
- [x] Auto-collapse Twig blocks as a folding option
- [ ] String interpolation (`#{foo}, #{1 + 2}`)
- [ ] Safe delete for parent templates
- [ ] Additional formatting/code style configuration
    - [ ] Spacing around operators 

### Milestone 3: Feature parity with community Symfony plugin Twig extensions

- [ ] Goto parent/use block
- [ ] Template include/extends/use block resolution goto
- [ ] Parent block implements/extends goto on line marker
- [ ] Macro goto on line marker
- [ ] PHPDoc annotation support ({# @var thing \Core\Thing #})

## Development

Contributing to this project will require an environment ready to develop IDEA plugins. [Read the guide](https://www.jetbrains.com/help/idea/configuring-intellij-platform-plugin-sdk.html)

This is a Gradle project, so import `gradle.build` and it should do everything for you without additional configuration. 

Make sure all tests pass, then make a pull request. If Travis fails to build your PR, please rectify any problems.
If you are writing a new feature, it is vital that you add sufficient tests! 