# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2024-08-16

### Added
- Initial release of BomaPay SDK for Java/Kotlin
- Core payment processing functionality
- Order management (register, pre-auth, deposit, refund, reverse, decline)
- Instant payment processing
- MOTO (Mail Order/Telephone Order) payment support
- Stored credential (binding) management
- Mobile wallet payment support scaffolding (Apple Pay, Google Pay, Samsung Pay)
- P2P transfer support scaffolding
- Comprehensive test suite with official BomaPay test cards
- HTTP client with proper error handling and logging
- Configuration management with builder pattern
- Input validation utilities
- Currency conversion utilities
- Spring Boot integration examples
- Maven Central publishing configuration

### Security
- Secure HTTP client implementation with OkHttp
- Proper credential handling and authentication
- Input validation for all payment data
- No logging of sensitive information

### Testing
- Complete test coverage with JUnit 5
- Official BomaPay test cards integration
- Success and failure scenario testing
- Integration test suite for complete payment flows
- Parameterized testing for different card types
- Test configuration utilities
- Example test usage documentation

### Documentation
- Comprehensive README with usage examples
- API reference documentation
- Testing guide with official test cards
- Spring Boot integration examples
- Security best practices guide
- Error handling documentation

### Dependencies
- OkHttp 4.12.0 for HTTP client
- Jackson 2.16.1 for JSON processing
- SLF4J 2.0.9 for logging
- JUnit Jupiter 5.10.1 for testing
- Mockito 5.8.0 for testing

### Build & Distribution
- Maven Central publishing configuration
- Source and Javadoc JAR generation
- GPG signing for releases
- Assembly plugin for distribution packages
- Multiple build profiles (dev, release, test)
- Continuous integration ready configuration

## [Unreleased]

### Planned
- Additional mobile wallet implementations (Apple Pay, Google Pay, Samsung Pay)
- Enhanced P2P transfer functionality
- Webhook handling support
- Additional currency support
- Performance optimizations
- Enhanced error handling and retry mechanisms