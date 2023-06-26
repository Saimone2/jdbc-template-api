package com.saimone.jdbctemplateapi.request;

import java.time.LocalDate;
import java.util.List;

public record MovieRequest(String name, List<String> actors, LocalDate releaseDate) {
}
