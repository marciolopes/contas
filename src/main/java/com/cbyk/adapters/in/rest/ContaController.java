package com.cbyk.adapters.in.rest;


import com.cbyk.domain.conta.app.ContaQueryAppService;
import com.cbyk.domain.conta.dto.ContaDto;
import com.cbyk.domain.conta.enums.SituacaoEnum;
import com.cbyk.domain.conta.usecase.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("conta")
@CrossOrigin
public class ContaController {

    ContaQueryAppService contaQueryAppService;

    ImportarContaUseCase importarContaUseCase;

    AlterarSituacaoDaContaUseCase alterarSituacaoDaContaUseCase;

    AtualizarContaUseCase atualizarContaUseCase;

    CriarContaUseCase criarContaUseCase;

    @PostMapping
    public ResponseEntity<ContaDto> criarConta(@RequestBody ContaDto conta) {
        ContaDto savedUser = criarContaUseCase.handle(conta);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<ContaDto> obterContaPeloId(@PathVariable("id") Long contaId) {
        ContaDto conta = contaQueryAppService.obterContaPeloId(contaId);
        return new ResponseEntity<>(conta, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<ContaDto> atualizarConta(@PathVariable("id") Long contaId, @RequestBody ContaDto conta) {
        ContaDto updatedConta = atualizarContaUseCase.handle(contaId, conta);
        return new ResponseEntity<>(updatedConta, HttpStatus.OK);
    }


    @PatchMapping("/{id}/situacao")
    public ResponseEntity<ContaDto> alterarSituacaoDaConta(
            @PathVariable Long id,
            @RequestBody SituacaoEnum novaSituacao) {

        ContaDto contaDTO = alterarSituacaoDaContaUseCase.handle(id, novaSituacao);
        return ResponseEntity.ok(contaDTO);
    }


    @Operation(
            summary = "filtrar Contas Por Situacao E DataVencimento",
            description = "Caso não coloque os parametros situacao e data vencimento, retornará todos registros de acordo com a paginação"
    )
    @GetMapping("/filtrarContasPorSituacaoEDataVencimento")
    public Page<ContaDto> filtrarContasPorSituacaoEDataVencimento(
            @RequestParam(required = false) SituacaoEnum situacao,
            @RequestParam(required = false) LocalDate dataVencimento,
            @ParameterObject @Parameter(description = "Parâmetros de paginação e ordenação",
                    schema = @Schema(defaultValue = "{\"page\": 0, \"size\": 10, \"sort\": [\"dataVencimento,asc\"]}")) Pageable pageable) {

        return contaQueryAppService.filtrarContasPorSituacaoEDataVencimento(situacao, dataVencimento, pageable);

    }

    @GetMapping("/listarContasPorPeriodoDePagamento")
    public Page<ContaDto> listarContasPorPeriodoDePagamento(
            LocalDate dataPagamentoInicio,
            LocalDate dataPagamentoFim,
            @ParameterObject @Parameter(description = "Parâmetros de paginação e ordenação",
                    schema = @Schema(defaultValue = "{\"page\": 0, \"size\": 10, \"sort\": [\"dataVencimento,asc\"]}")) Pageable pageable) {

        return contaQueryAppService.findByDataPagamentoBetween(dataPagamentoInicio, dataPagamentoFim, pageable);
    }


    @PostMapping(value = "/importar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> importarConta(
            @Parameter(description = "File to upload", required = true)
            @RequestPart(value = "file")
            MultipartFile file) throws Exception {
        importarContaUseCase.handle(file);
        String message = "Uploaded realizado com sucesso: " + file.getOriginalFilename();
        return ResponseEntity.status(HttpStatus.OK).body(message);

    }

}
