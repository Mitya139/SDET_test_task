FROM eclipse-temurin:25-jdk-noble

ENV DEBIAN_FRONTEND=noninteractive \
    GRADLE_USER_HOME=/home/tester/.gradle \
    JAVA_TOOL_OPTIONS="-Djava.awt.headless=false"

RUN apt-get update && apt-get install -y --no-install-recommends \
    bash \
    ca-certificates \
    curl \
    fontconfig \
    fonts-dejavu-core \
    git \
    libasound2t64 \
    libcups2 \
    libgbm1 \
    libgtk-3-0 \
    libnss3 \
    libsecret-1-0 \
    libx11-6 \
    libxext6 \
    libxi6 \
    libxrandr2 \
    libxrender1 \
    libxss1 \
    libxtst6 \
    procps \
    tar \
    unzip \
    xauth \
    xvfb \
    xz-utils \
    && rm -rf /var/lib/apt/lists/*

RUN useradd --create-home --shell /bin/bash tester

WORKDIR /workspace
COPY --chown=tester:tester . .
RUN chmod +x ./gradlew \
    && chown tester:tester /workspace

USER tester

CMD ["bash", "-lc", "Xvfb :99 -screen 0 1920x1080x24 -ac +extension RANDR -nolisten tcp & export DISPLAY=:99; sleep 2; ./gradlew test --no-daemon --stacktrace"]
