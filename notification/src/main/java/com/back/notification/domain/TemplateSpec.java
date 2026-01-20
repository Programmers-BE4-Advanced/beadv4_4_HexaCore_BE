package com.back.notification.domain;

public record TemplateSpec(
        String templateKey,
        Object[] args
) {
}
