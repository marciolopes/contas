package com.cbyk.adapters.in.rest;

import com.cbyk.domain.conta.app.ContaQueryAppService;
import com.cbyk.domain.conta.dto.ContaDto;
import com.cbyk.domain.conta.enums.SituacaoEnum;
import com.cbyk.domain.conta.usecase.AlterarSituacaoDaContaUseCase;
import com.cbyk.domain.conta.usecase.AtualizarContaUseCase;
import com.cbyk.domain.conta.usecase.CriarContaUseCase;
import com.cbyk.domain.conta.usecase.ImportarContaUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContaController.class)
public class ContaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContaQueryAppService contaQueryAppService;

    @MockBean
    private ImportarContaUseCase importarContaUseCase;

    @MockBean
    private AlterarSituacaoDaContaUseCase alterarSituacaoDaContaUseCase;

    @MockBean
    private AtualizarContaUseCase atualizarContaUseCase;

    @MockBean
    private CriarContaUseCase criarContaUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCriarConta() throws Exception {
        ContaDto contaDto = ContaDto.builder().id(1L).situacao(SituacaoEnum.PENDENTE).build();

        when(criarContaUseCase.handle(any(ContaDto.class))).thenReturn(contaDto);

        mockMvc.perform(post("/conta")
                        .with(httpBasic("login", "123456"))
                        .with(csrf())  // Adiciona o token CSRF
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contaDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.situacao").value(SituacaoEnum.PENDENTE.name()));

    }

    @Test
    void testObterContaPeloId() throws Exception {
        ContaDto contaDto = ContaDto.builder().id(1L).situacao(SituacaoEnum.PENDENTE).build();

        when(contaQueryAppService.obterContaPeloId(1L)).thenReturn(contaDto);

        mockMvc.perform(get("/conta/1").with(httpBasic("login", "123456"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.situacao").value(SituacaoEnum.PENDENTE.name()));
    }

    @Test
    void testAtualizarConta() throws Exception {
        ContaDto contaDto = ContaDto.builder().id(1L).situacao(SituacaoEnum.PAGA).build();

        when(atualizarContaUseCase.handle(any(Long.class), any(ContaDto.class))).thenReturn(contaDto);

        mockMvc.perform(put("/conta/1").with(httpBasic("login", "123456"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contaDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.situacao").value(SituacaoEnum.PAGA.name()));
    }

    @Test
    void testAlterarSituacaoDaConta() throws Exception {
        ContaDto contaDto = ContaDto.builder().id(1L).situacao(SituacaoEnum.PAGA).build();

        when(alterarSituacaoDaContaUseCase.handle(1L, SituacaoEnum.PAGA)).thenReturn(contaDto);

        mockMvc.perform(patch("/conta/1/situacao").with(httpBasic("login", "123456"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(SituacaoEnum.PAGA)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.situacao").value(SituacaoEnum.PAGA.name()));
    }

    @Test
    void testFiltrarContasPorSituacaoEDataVencimento() throws Exception {
        // Simular uma resposta de página de ContaDto
        Page<ContaDto> contasPage = new PageImpl<>(Collections.singletonList(
                ContaDto.builder().id(1L).situacao(SituacaoEnum.PENDENTE).build()
        ));

        when(contaQueryAppService.filtrarContasPorSituacaoEDataVencimento(any(SituacaoEnum.class), any(LocalDate.class), any(Pageable.class)))
                .thenReturn(contasPage);

        mockMvc.perform(get("/conta/filtrarContasPorSituacaoEDataVencimento").with(httpBasic("login", "123456"))
                        .with(csrf())
                        .param("situacao", "PENDENTE")
                        .param("dataVencimento", "2024-09-25")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].situacao").value(SituacaoEnum.PENDENTE.name()));
    }

    @Test
    void testListarContasPorPeriodoDePagamento() throws Exception {
        Page<ContaDto> contasPage = new PageImpl<>(Collections.singletonList(
                ContaDto.builder().id(1L).situacao(SituacaoEnum.PENDENTE).build()
        ));

        when(contaQueryAppService.findByDataPagamentoBetween(any(LocalDate.class), any(LocalDate.class), any(Pageable.class)))
                .thenReturn(contasPage);

        mockMvc.perform(get("/conta/listarContasPorPeriodoDePagamento").with(httpBasic("login", "123456"))
                        .with(csrf())
                        .param("dataPagamentoInicio", "2024-09-01")
                        .param("dataPagamentoFim", "2024-09-30")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].situacao").value(SituacaoEnum.PENDENTE.name()));
    }

    @Test
    void testImportarConta() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.csv", MediaType.MULTIPART_FORM_DATA_VALUE, "conteúdo do arquivo".getBytes());

        mockMvc.perform(multipart("/conta/importar")
                        .file(file).with(httpBasic("login", "123456")).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Uploaded realizado com sucesso: test.csv"));
    }
}

