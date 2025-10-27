package com.bingebox.controller.request;

import jakarta.validation.constraints.NotEmpty;

public record CategoryRequest(@NotEmpty(message = "Nome da categoria é obrigatorio!") String name) {
}
