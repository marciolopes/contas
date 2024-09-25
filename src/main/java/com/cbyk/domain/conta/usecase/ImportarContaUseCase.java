package com.cbyk.domain.conta.usecase;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImportarContaUseCase {

    void handle(MultipartFile file) throws IOException;
}
