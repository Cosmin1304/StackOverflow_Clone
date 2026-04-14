package com.utcn.demo.dto.Requests;

import java.util.List;

public record TopicRequestDTO(
        String title,
        String text,
        String pictureUrl,          //opțional
        List<String> tagNames
) {}
