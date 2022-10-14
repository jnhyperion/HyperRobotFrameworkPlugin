<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# HyperRobotFrameworkPlugin Changelog
## [0.1.0]
### Added
- Better file structure view
- Folding function for multi line statements

### Fixed
- No word completion for `tasks`

## [0.0.9]
### Changed
- `RETURN` is changed from keyword to robot syntax marker

### Fixed
- Variable cannot be resolved after multi line statements
- Incorrect paring after robot syntax markers
- Other minor bugs

## [0.0.8]
### Fixed
- Reference link is incorrect sometimes when refactoring projects
- Incorrect inspection warning when `resource` file is not directly used in transitive import 
- Cannot parse variable properly in syntax maker statement block

## [0.0.7]
### Added
- When plugin cannot find resource/library/variable file by the exact given path, it will try to search the entire project for the most possible files. (Will be useful when your file path is dynamic during the runtime.)
- If plugin find more than one results during the fuzzy search, this file is still marked as unrecognized, you can find the multi results by clicking the references.
- Note, this feature may ignore your potential runtime importing errors.

### Fixed
- Some potential IDE errors
- Robot builtin variable cannot be recognized sometimes
- Some defined variables are missing in word completion list
- Some defined variables' formats are incorrect in word completion list

## [0.0.6]
### Added
- Variables dynamically set in settings `Test Setup` & `Suite Setup` now can be resolved
- Variables dynamically set in `__init__.robot` now can be resolved

### Fixed
- Incorrect import reference when there are multi python/resource files with the same name

## [0.0.5]
### Fixed
- Some plugin errors when updating project structure
- Some plugin errors when updating project python interpreter

## [0.0.4]
### Added
- Support `WHILE` `CONTINUE` `BREAK` syntax
- Support defined variable word completion in keyword and test
### Fixed
- Some builtin variables are incorrect in recommendation word completion
- Some library keywords completion contains string `'`
- Dict variables are not considered as variables
- Number variable such as `${1}` will be recognized as undefined variable
- Variable definition is not recognized after `FOR` statement


## [0.0.3]
### Fixed
- Fix some plugin errors
### Added
- Support more Pycharm Editions
- Support `TRY` `EXCEPT` syntax
- Support `*** Tasks ***` syntax
- Support data drive style tests
### Changed
- Keyword with arguments tail is changed from 2 blanks to 4
- Optimize recommendation word completion
- Optimize syntax highlight color


## 0.0.2
### Changed
- Optimize recommendation word completion
- Optimize syntax highlight color
- Support `TRY` `EXCEPT` syntax
- Support `*** Tasks ***` syntax
- Support data drive style tests
- Fix some plugin errors

## 0.0.1
- Initial scaffold created from [IntelliJ Platform Plugin Template](https://github.com/JetBrains/intellij-platform-plugin-template)
