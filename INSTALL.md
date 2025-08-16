# Installation Guide

This guide will help you install and set up the BomaPay SDK in your Java/Kotlin project.

## System Requirements

- **Java**: JDK 17 or higher
- **Build Tool**: Maven 3.6+ or Gradle 7.0+
- **Operating System**: Any OS that supports Java (Windows, macOS, Linux)

## Installation Methods

### 1. Maven Central (Recommended)

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>org.abutimartin.bpcpayment</groupId>
    <artifactId>bomapay-sdk</artifactId>
    <version>${project.version}</version>
</dependency>
```

### 2. Gradle

Add to your `build.gradle`:

```gradle
implementation 'org.abutimartin.bpcpayment:bomapay-sdk:${project.version}'
```

### 3. Manual Installation from Distribution

1. Download the distribution package from releases
2. Extract the archive
3. Add the JAR files to your classpath:
   - `lib/bomapay-sdk-${project.version}.jar` (main library)
   - `lib/dependencies/*.jar` (required dependencies)

#### Manual Installation Example

```bash
# Extract distribution
unzip bomapay-sdk-${project.version}-distribution.zip
cd bomapay-sdk-${project.version}

# Add to classpath (Linux/macOS)
export CLASSPATH="lib/bomapay-sdk-${project.version}.jar:lib/dependencies/*:$CLASSPATH"

# Add to classpath (Windows)
set CLASSPATH=lib\bomapay-sdk-${project.version}.jar;lib\dependencies\*;%CLASSPATH%
```

## Verify Installation

Create a simple test to verify the installation:

```java
import org.abutimartin.bpcpayment.BomaPayClient;
import org.abutimartin.bpcpayment.config.BomaPayConfig;

public class InstallationTest {
    public static void main(String[] args) {
        try {
            BomaPayConfig config = BomaPayConfig.builder()
                .username("test_user")
                .password("test_password")
                .baseUrl("https://dev.bpcbt.com/payment")
                .build();
            
            BomaPayClient client = new BomaPayClient(config);
            System.out.println("✅ BomaPay SDK installed successfully!");
            
        } catch (Exception e) {
            System.err.println("❌ Installation failed: " + e.getMessage());
        }
    }
}
```

## Configuration

### Basic Configuration

```java
BomaPayConfig config = BomaPayConfig.builder()
    .username("your_api_username")
    .password("your_api_password")
    .clientId("your_client_id")
    .merchantLogin("your_merchant_login")
    .baseUrl("https://api.bomapay.com/payment") // Production URL
    .build();
```

### Spring Boot Configuration

#### 1. Add properties to `application.yml`:

```yaml
bomapay:
  username: ${BOMAPAY_USERNAME:test_user}
  password: ${BOMAPAY_PASSWORD:test_password}
  client-id: ${BOMAPAY_CLIENT_ID:your_client_id}
  merchant-login: ${BOMAPAY_MERCHANT_LOGIN:your_merchant}
  base-url: ${BOMAPAY_BASE_URL:https://dev.bpcbt.com/payment}
  currency: ${BOMAPAY_CURRENCY:978}
  language: ${BOMAPAY_LANGUAGE:en}
  timeout: ${BOMAPAY_TIMEOUT:30000}
```

#### 2. Create configuration class:

```java
@Configuration
@ConfigurationProperties(prefix = "bomapay")
@Data
public class BomaPayProperties {
    private String username;
    private String password;
    private String clientId;
    private String merchantLogin;
    private String baseUrl;
    private String currency = "978";
    private String language = "en";
    private int timeout = 30000;
}

@Configuration
@EnableConfigurationProperties(BomaPayProperties.class)
public class BomaPayConfiguration {
    
    @Bean
    public BomaPayClient bomaPayClient(BomaPayProperties properties) {
        BomaPayConfig config = BomaPayConfig.builder()
            .username(properties.getUsername())
            .password(properties.getPassword())
            .clientId(properties.getClientId())
            .merchantLogin(properties.getMerchantLogin())
            .baseUrl(properties.getBaseUrl())
            .currency(properties.getCurrency())
            .language(properties.getLanguage())
            .timeout(properties.getTimeout())
            .build();
            
        return new BomaPayClient(config);
    }
}
```

## Environment Setup

### Development Environment

```bash
export BOMAPAY_USERNAME="test_user"
export BOMAPAY_PASSWORD="test_password"
export BOMAPAY_BASE_URL="https://dev.bpcbt.com/payment"
export BOMAPAY_CLIENT_ID="259753456"
export BOMAPAY_MERCHANT_LOGIN="OurBestMerchantLogin"
```

### Production Environment

```bash
export BOMAPAY_USERNAME="your_production_username"
export BOMAPAY_PASSWORD="your_production_password"
export BOMAPAY_BASE_URL="https://api.bomapay.com/payment"
export BOMAPAY_CLIENT_ID="your_production_client_id"
export BOMAPAY_MERCHANT_LOGIN="your_production_merchant"
```

## Dependencies

The SDK includes the following dependencies:

- **OkHttp 4.12.0**: HTTP client
- **Jackson 2.16.1**: JSON processing
- **SLF4J 2.0.9**: Logging abstraction

### Optional Dependencies

For testing:
- **JUnit Jupiter 5.10.1**: Unit testing
- **Mockito 5.8.0**: Mocking framework

## Troubleshooting

### Common Issues

#### 1. ClassNotFoundException
**Problem**: `java.lang.ClassNotFoundException: org.abutimartin.bpcpayment.BomaPayClient`

**Solution**: Ensure the SDK JAR is in your classpath and all dependencies are included.

#### 2. SSL/TLS Errors
**Problem**: SSL handshake failures

**Solution**: 
- Ensure your Java version supports modern TLS
- Check firewall and proxy settings
- Verify the base URL is correct

#### 3. Authentication Errors
**Problem**: HTTP 401 Unauthorized

**Solution**:
- Verify username and password are correct
- Check if credentials are for the correct environment (sandbox vs production)
- Ensure credentials are properly URL-encoded if they contain special characters

#### 4. Dependency Conflicts
**Problem**: Version conflicts with existing dependencies

**Solution**:
- Use Maven dependency management to resolve conflicts
- Exclude conflicting transitive dependencies if necessary

```xml
<dependency>
    <groupId>org.abutimartin.bpcpayment</groupId>
    <artifactId>bomapay-sdk</artifactId>
    <version>${project.version}</version>
    <exclusions>
        <exclusion>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

### Getting Help

1. **Check Documentation**: Review the [README.md](README.md) for detailed usage examples
2. **Test Your Setup**: Use the provided test utilities in the `test-utils/` directory
3. **Enable Debug Logging**: Set logging level to DEBUG for the SDK package
4. **GitHub Issues**: Report issues at the project repository

### Debug Logging

Enable debug logging to troubleshoot issues:

#### Logback (logback.xml)
```xml
<logger name="org.abutimartin.bpcpayment" level="DEBUG" />
<logger name="okhttp3" level="DEBUG" />
```

#### Log4j2 (log4j2.xml)
```xml
<Logger name="org.abutimartin.bpcpayment" level="DEBUG" />
<Logger name="okhttp3" level="DEBUG" />
```

## Next Steps

1. **Read the Documentation**: Review the complete [README.md](README.md)
2. **Explore Examples**: Check the `examples/` directory for sample implementations
3. **Run Tests**: Execute the test suite to verify everything works
4. **Start Integrating**: Begin implementing payment functionality in your application

## Support

- **Documentation**: [README.md](README.md)
- **Examples**: `examples/` directory
- **Test Utilities**: `test-utils/` directory
- **Issues**: GitHub Issues page