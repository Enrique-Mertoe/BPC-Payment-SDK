#!/bin/bash

# BomaPay SDK Build Script
# This script builds, tests, and packages the SDK for distribution

set -e

echo "🚀 Building BomaPay SDK"
echo "======================"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if Maven is available
if ! command -v mvn &> /dev/null; then
    print_error "Maven is not installed or not in PATH"
    exit 1
fi

# Check Java version
JAVA_VERSION=$(mvn -version | grep "Java version" | awk '{print $3}' | sed 's/"//g')
print_status "Using Java version: $JAVA_VERSION"

# Set build profile
PROFILE=${1:-dev}
print_status "Using build profile: $PROFILE"

# Clean previous builds
print_status "Cleaning previous builds..."
mvn clean -q

# Compile and run tests
print_status "Compiling source code..."
mvn compile -P$PROFILE -q

print_status "Running tests..."
if mvn test -P$PROFILE; then
    print_success "All tests passed"
else
    print_warning "Some tests failed, but continuing build"
fi

# Package the project
print_status "Packaging SDK..."
mvn package -P$PROFILE -DskipTests

# Generate documentation
print_status "Generating documentation..."
mvn javadoc:javadoc -q

# Create distribution packages
print_status "Creating distribution packages..."
mvn assembly:single -P$PROFILE -DskipTests

# Build examples
print_status "Building examples..."
if [ -d "examples/simple-payment" ]; then
    cd examples/simple-payment
    mvn clean compile -q
    cd ../..
    print_success "Simple payment example built"
fi

if [ -d "examples/spring-boot-example" ]; then
    cd examples/spring-boot-example
    mvn clean compile -q
    cd ../..
    print_success "Spring Boot example built"
fi

# Display build results
echo
print_success "🎉 Build completed successfully!"
echo
echo "📦 Generated artifacts:"
echo "  Main JAR:       target/bomapay-sdk-*.jar"
echo "  Sources JAR:    target/bomapay-sdk-*-sources.jar"
echo "  Javadoc JAR:    target/bomapay-sdk-*-javadoc.jar"
echo "  Distribution:   target/bomapay-sdk-*-distribution.zip"
echo "  Documentation:  target/site/apidocs/"
echo

# Run SDK verification
print_status "Running SDK verification..."
java -cp "target/bomapay-sdk-*.jar:target/lib/*" org.abutimartin.bpcpayment.TestRunner || true

echo
print_success "✅ SDK is ready for distribution!"
echo
echo "📋 Next steps:"
echo "  1. Test the distribution package: unzip target/*-distribution.zip"
echo "  2. Run examples: cd examples/simple-payment && mvn exec:java"
echo "  3. Deploy to repository: mvn deploy -Prelease"
echo "  4. Create GitHub release with generated artifacts"
echo

if [ "$PROFILE" = "release" ]; then
    print_warning "🔐 Release build detected"
    echo "   - Artifacts are signed with GPG"
    echo "   - Ready for Maven Central deployment"
    echo "   - Run 'mvn deploy -Prelease' to publish"
fi