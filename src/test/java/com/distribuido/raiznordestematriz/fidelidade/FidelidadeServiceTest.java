package com.distribuido.raiznordestematriz.fidelidade;

import com.distribuido.raiznordestematriz.consolidacao.model.Canal;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FidelidadeServiceTest {

    @Test
    void arredondaPontosParaBaixo() {
        assertEquals(64, FidelidadeService.calcularPontos(new BigDecimal("64.90")));
        assertEquals(0, FidelidadeService.calcularPontos(new BigDecimal("0.99")));
        assertEquals(0, FidelidadeService.calcularPontos(null));
    }

    @Test
    void canalValueOfRejeitaValorDesconhecido() {
        assertEquals(Canal.APP, Canal.valueOf("APP"));
        assertThrows(IllegalArgumentException.class, () -> Canal.valueOf("DRONE"));
    }
}
