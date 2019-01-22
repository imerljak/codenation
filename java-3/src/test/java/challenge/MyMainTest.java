package challenge;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class MyMainTest {

    private static Main main;

    @BeforeClass
    public static void beforeClass() {
        main = new Main();
    }

    @Test
    public void q1DeveRetornar164RegistrosUnicos() {
        int respostaQ1 = main.q1();

        Assert.assertEquals(164, respostaQ1);
    }

    @Test
    public void q2DeveRetornar647RegistrosUnicos() {
        int respostaQ1 = main.q2();

        Assert.assertEquals(647, respostaQ1);
    }

    @Test
    public void deveTer1088JogadoresCom19Anos() {
        Map<Integer, Integer> mapIdades = main.q6();

        Assert.assertEquals(1088, mapIdades.get(19).intValue());
    }

    @Test
    public void deveTer1053JogadoresCom28Anos() {
        Map<Integer, Integer> mapIdades = main.q6();

        Assert.assertEquals(1053, mapIdades.get(28).intValue());
    }

    @Test
    public void q3DeveTerRonaldoDosSantosNoInicio() {
        List<String> jogadores = main.q3();
        Assert.assertEquals("C. Ronaldo dos Santos Aveiro", jogadores.get(0));
    }

    @Test
    public void q4DeveTerNeymarNoInicio() {
        List<String> jogadores = main.q4();

        Assert.assertEquals("Neymar da Silva Santos Jr.", jogadores.get(0));
    }

    @Test
    public void q5DeveTerBenjaminNivetAoFinal() {
        List<String> jogadores = main.q5();

        Assert.assertEquals("Benjamin Nivet", jogadores.get(jogadores.size() - 1));
    }

}
