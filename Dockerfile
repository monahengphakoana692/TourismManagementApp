# Use Ubuntu 22.04 as base
FROM ubuntu:22.04

# Install essential tools
RUN apt-get update && \
    apt-get install -y wget unzip zip git curl build-essential libz-dev zlib1g-dev

# Install GraalVM for Java 17
RUN wget https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-22.3.2/graalvm-ce-java17-linux-amd64-22.3.2.tar.gz && \
    tar -xzf graalvm-ce-java17-linux-amd64-22.3.2.tar.gz -C /usr/lib && \
    rm graalvm-ce-java17-linux-amd64-22.3.2.tar.gz && \
    ln -s /usr/lib/graalvm-ce-java17-22.3.2 /usr/lib/graalvm

# Set GraalVM environment variables
ENV GRAALVM_HOME=/usr/lib/graalvm
ENV PATH=$GRAALVM_HOME/bin:$PATH


# Install Android SDK
RUN mkdir -p /opt/android-sdk && \
    cd /opt/android-sdk && \
    wget https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip -O cmdline-tools.zip && \
    unzip cmdline-tools.zip && \
    rm cmdline-tools.zip && \
    mv cmdline-tools latest && \
    mkdir -p cmdline-tools && \
    mv latest cmdline-tools/

# Set Android environment variables
ENV ANDROID_HOME=/opt/android-sdk
ENV PATH=$ANDROID_HOME/cmdline-tools/latest/bin:$PATH

# Accept Android licenses
RUN yes | sdkmanager --licenses

# Install Android tools
RUN sdkmanager "platform-tools" "platforms;android-33" "build-tools;33.0.0" "ndk;25.2.9519653"

# Set working directory (your project will be mounted here)
WORKDIR /app

# Install Maven
RUN apt-get install -y maven