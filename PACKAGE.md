# BomaPay SDK - Packaging Guide

This guide explains how to build, package, and distribute the BomaPay SDK.

## 📦 Package Structure

```
bomapay-sdk/
├── src/
│   ├── main/java/          # Source code
│   ├── test/java/          # Test code
│   ├── assembly/           # Distribution assembly
│   └── main/resources/     # Resources
├── examples/
│   ├── simple-payment/     # Basic usage example
│   └── spring-boot-example/ # Spring Boot integration
├── target/                 # Build output
├── docs/                   # Documentation
├── pom.xml                 # Maven configuration
├── README.md               # Main documentation
├── CHANGELOG.md            # Version history
├── LICENSE                 # MIT License
├── INSTALL.md              # Installation guide
├── PACKAGE.md              # This file
└── build.sh               # Build script
```

## 🛠️ Build Profiles

### Development Profile (default)
```bash
mvn clean package -Pdev
# or
./build.sh dev
```
- Skips GPG signing
- Skips Javadoc generation (optional)
- Fast development builds

### Release Profile
```bash
mvn clean package -Prelease
# or
./build.sh release
```
- Generates signed artifacts
- Creates complete Javadoc
- Ready for Maven Central

### Test Profile
```bash
mvn clean package -Ptest
```
- Runs all tests including integration tests
- Generates test reports

## 🏗️ Build Commands

### Quick Build
```bash
./build.sh
```

### Full Build with Tests
```bash
mvn clean verify
```

### Generate Documentation
```bash
mvn javadoc:javadoc
mvn site
```

### Create Distribution
```bash
mvn assembly:single
```

### Deploy to Repository
```bash
mvn deploy -Prelease
```

## 📋 Generated Artifacts

### Main Artifacts
- `bomapay-sdk-1.0.0.jar` - Main library
- `bomapay-sdk-1.0.0-sources.jar` - Source code
- `bomapay-sdk-1.0.0-javadoc.jar` - API documentation

### Distribution Packages
- `bomapay-sdk-1.0.0-distribution.zip` - Complete distribution
- `bomapay-sdk-1.0.0-distribution.tar.gz` - Complete distribution (tar)

### Distribution Contents
```
bomapay-sdk-1.0.0/
├── lib/
│   ├── bomapay-sdk-1.0.0.jar
│   ├── bomapay-sdk-1.0.0-sources.jar
│   ├── bomapay-sdk-1.0.0-javadoc.jar
│   └── dependencies/
├── docs/
│   ├── README.md
│   ├── CHANGELOG.md
│   ├── LICENSE
│   └── INSTALL.md
├── examples/
│   ├── simple-payment/
│   └── spring-boot-example/
├── test-utils/
└── pom.xml
```

## 🚀 Publishing

### Maven Central

1. **Setup GPG signing**:
   ```bash
   gpg --gen-key
   gpg --keyserver keys.openpgp.org --send-keys YOUR_KEY_ID
   ```

2. **Configure settings.xml**:
   ```xml
   <servers>
     <server>
       <id>central</id>
       <username>your-token-username</username>
       <password>your-token-password</password>
     </server>
   </servers>
   ```

3. **Deploy**:
   ```bash
   mvn deploy -Prelease
   ```

### GitHub Releases

1. **Create release**:
   ```bash
   git tag v1.0.0
   git push origin v1.0.0
   ```

2. **Upload artifacts**:
   - `bomapay-sdk-1.0.0-distribution.zip`
   - `bomapay-sdk-1.0.0-distribution.tar.gz`
   - Release notes from CHANGELOG.md

## 🧪 Verification

### Test Distribution Package
```bash
# Extract distribution
unzip target/bomapay-sdk-*-distribution.zip
cd bomapay-sdk-*/

# Test examples
cd examples/simple-payment
mvn compile exec:java

# Test Spring Boot example
cd ../spring-boot-example
mvn spring-boot:run
```

### Verify JAR Contents
```bash
jar -tf target/bomapay-sdk-*.jar | head -20
```

### Test Maven Integration
```bash
# Create test project
mvn archetype:generate -DgroupId=test -DartifactId=bomapay-test

# Add dependency and test
```

## 📊 Quality Checks

### Code Coverage
```bash
mvn jacoco:report
open target/site/jacoco/index.html
```

### Static Analysis
```bash
mvn spotbugs:check
mvn checkstyle:check
```

### Dependency Analysis
```bash
mvn dependency:analyze
mvn dependency:tree
```

## 🔧 Troubleshooting

### Common Issues

1. **Missing GPG key**:
   ```
   [ERROR] Failed to execute goal org.apache.maven.plugins:maven-gpg-plugin
   ```
   Solution: Configure GPG or use `-Dgpg.skip=true`

2. **Test failures**:
   ```
   [ERROR] There are test failures.
   ```
   Solution: Fix tests or use `-DskipTests`

3. **Memory issues**:
   ```
   [ERROR] OutOfMemoryError
   ```
   Solution: Set `MAVEN_OPTS="-Xmx2048m"`

### Build Environment

- **Java**: JDK 17 or higher
- **Maven**: 3.6.0 or higher
- **GPG**: For signing releases
- **Git**: For version control

## 📈 Continuous Integration

### GitHub Actions Example
```yaml
name: Build and Test
on: [push, pull_request]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
      - run: ./build.sh test
      - run: mvn verify
```

## 📚 Documentation

### Update Documentation
1. Update README.md for new features
2. Update CHANGELOG.md for releases
3. Update INSTALL.md for new requirements
4. Generate Javadoc: `mvn javadoc:javadoc`

### Documentation Sources
- **README.md**: Main documentation
- **Javadoc**: API documentation
- **Examples**: Usage examples
- **Tests**: Test documentation

## 🔐 Security

### Signing
- All releases are GPG signed
- Verify signatures: `gpg --verify *.asc`

### Dependencies
- Regular security updates
- Vulnerability scanning: `mvn dependency-check:check`

### Secrets
- Never commit API keys
- Use environment variables
- Secure CI/CD variables

## 📞 Support

- **Issues**: GitHub Issues
- **Documentation**: README.md
- **Examples**: examples/ directory
- **Tests**: Comprehensive test suite