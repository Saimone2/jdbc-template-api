package com.saimone.jdbctemplateapi.actor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Actor {
    private Integer id;
    private String name;
    private List<String> movies;
}
