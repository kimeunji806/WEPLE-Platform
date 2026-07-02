package com.weple.cloud.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.mock.env.MockEnvironment;

class GithubTokenPropertyTests {

    @Test
    void githubTokenPropertyUsesGithubApiTokenEnvironmentVariable() throws IOException {
        Properties applicationProperties = PropertiesLoaderUtils.loadAllProperties("application.properties");
        Properties apiProperties = PropertiesLoaderUtils.loadAllProperties("application-api.properties");

        assertThat(applicationProperties.getProperty("github.api.token")).isEqualTo("${GITHUB_API_TOKEN:}");
        assertThat(apiProperties.getProperty("github.api.token")).isEqualTo("${GITHUB_API_TOKEN:}");
    }

    @Test
    void githubTokenPlaceholderResolvesFromGithubApiToken() {
        MockEnvironment environment = new MockEnvironment()
                .withProperty("GITHUB_API_TOKEN", "test-github-token")
                .withProperty("github.api.token", "${GITHUB_API_TOKEN:}");

        String resolvedToken = environment.resolvePlaceholders(environment.getProperty("github.api.token"));

        assertThat(resolvedToken).isEqualTo("test-github-token");
    }
}
