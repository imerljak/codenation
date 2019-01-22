package br.com.codenation;

import br.com.codenation.desafio.annotation.Desafio;
import br.com.codenation.desafio.app.MeuTimeInterface;
import br.com.codenation.desafio.exceptions.CapitaoNaoInformadoException;
import br.com.codenation.desafio.exceptions.IdentificadorUtilizadoException;
import br.com.codenation.desafio.exceptions.JogadorNaoEncontradoException;
import br.com.codenation.desafio.exceptions.TimeNaoEncontradoException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class DesafioMeuTimeApplication implements MeuTimeInterface {

    private Map<Long, Time> times = new HashMap<>();
    private Map<Long, Jogador> jogadores = new HashMap<>();

    @Desafio("incluirTime")
    public void incluirTime(Long id, String nome, LocalDate dataCriacao, String corUniformePrincipal, String corUniformeSecundario) {
        final Time time = times.putIfAbsent(id, new Time(id, nome, dataCriacao, corUniformePrincipal, corUniformeSecundario));

        if (time != null) {
            throw new IdentificadorUtilizadoException();
        }
    }

    @Desafio("incluirJogador")
    public void incluirJogador(Long id, Long idTime, String nome, LocalDate dataNascimento, Integer nivelHabilidade, BigDecimal salario) {
        final Time time = times.get(idTime);

        if (time == null) {
            throw new TimeNaoEncontradoException();
        }

        final Jogador jogador = jogadores.putIfAbsent(id, new Jogador(id, idTime, nome, dataNascimento, nivelHabilidade, salario));

        if (jogador != null) {
            throw new IdentificadorUtilizadoException();
        }
    }

    @Desafio("definirCapitao")
    public void definirCapitao(Long idJogador) {
        final Jogador jogador = jogadores.get(idJogador);

        if (jogador == null) {
            throw new JogadorNaoEncontradoException();
        }

        times.get(jogador.idTime).setIdCapitao(idJogador);
    }

    @Desafio("buscarCapitaoDoTime")
    public Long buscarCapitaoDoTime(Long idTime) {
        final Time time = times.get(idTime);
        if (time == null) {
            throw new TimeNaoEncontradoException();
        }

        if (time.getIdCapitao() == null) {
            throw new CapitaoNaoInformadoException();
        }

        return time.getIdCapitao();
    }

    @Desafio("buscarNomeJogador")
    public String buscarNomeJogador(Long idJogador) {
        final Jogador jogador = jogadores.get(idJogador);
        if (jogador == null) {
            throw new JogadorNaoEncontradoException();
        }

        return jogador.getNome();
    }

    @Desafio("buscarNomeTime")
    public String buscarNomeTime(Long idTime) {
        final Time time = times.get(idTime);
        if (time == null) {
            throw new TimeNaoEncontradoException();
        }

        return time.getNome();
    }

    @Desafio("buscarJogadoresDoTime")
    public List<Long> buscarJogadoresDoTime(Long idTime) {

        if (times.get(idTime) == null) {
            throw new TimeNaoEncontradoException();
        }

        return jogadores.entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .filter(jogador -> jogador.getIdTime().equals(idTime))
                .map(Jogador::getId)
                .sorted()
                .collect(Collectors.toList());
    }

    @Desafio("buscarMelhorJogadorDoTime")
    public Long buscarMelhorJogadorDoTime(Long idTime) {

        if (times.get(idTime) == null) {
            throw new TimeNaoEncontradoException();
        }

        return jogadores.entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .filter(jogador -> jogador.getIdTime().equals(idTime))
                .sorted(Comparator.comparing(Jogador::getNivelHabilidade).reversed())
                .map(Jogador::getId)
                .findFirst()
                .orElse(null);
    }

    @Desafio("buscarJogadorMaisVelho")
    public Long buscarJogadorMaisVelho(Long idTime) {
        if (times.get(idTime) == null) {
            throw new TimeNaoEncontradoException();
        }

        return jogadores.entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .filter(jogador -> jogador.getIdTime().equals(idTime))
                .sorted(Comparator.comparing(Jogador::getDataNascimento).thenComparing(Jogador::getId))
                .map(Jogador::getId)
                .findFirst()
                .orElse(null);
    }

    @Desafio("buscarTimes")
    public List<Long> buscarTimes() {
        return times.entrySet()
                .stream()
                .map(Map.Entry::getKey)
                .sorted()
                .collect(Collectors.toList());
    }

    @Desafio("buscarJogadorMaiorSalario")
    public Long buscarJogadorMaiorSalario(Long idTime) {
        if (times.get(idTime) == null) {
            throw new TimeNaoEncontradoException();
        }

        return jogadores.entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .filter(jogador -> jogador.getIdTime().equals(idTime))
                .sorted(Comparator.comparing(Jogador::getSalario).reversed().thenComparing(Jogador::getId))
                .map(Jogador::getId)
                .findFirst()
                .orElse(null);
    }

    @Desafio("buscarSalarioDoJogador")
    public BigDecimal buscarSalarioDoJogador(Long idJogador) {
        final Jogador jogador = jogadores.get(idJogador);

        if (jogador == null) {
            throw new JogadorNaoEncontradoException();
        }

        return jogador.getSalario();
    }

    @Desafio("buscarTopJogadores")
    public List<Long> buscarTopJogadores(Integer top) {
        return jogadores.entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .sorted(Comparator.comparing(Jogador::getNivelHabilidade).reversed().thenComparing(Jogador::getId))
                .limit(top)
                .map(Jogador::getId)
                .collect(Collectors.toList());
    }

    @Desafio("buscarCorCamisaTimeDeFora")
    public String buscarCorCamisaTimeDeFora(Long timeDaCasa, Long timeDeFora) {

        final Time daCasa = times.get(timeDaCasa);
        final Time deFora = times.get(timeDeFora);

        if (daCasa == null || deFora == null) {
            throw new TimeNaoEncontradoException();
        }

        if (daCasa.getCorUniformePrincipal().equalsIgnoreCase(deFora.getCorUniformePrincipal())) {
            return deFora.getCorUniformeSecundario();
        }

        return deFora.getCorUniformePrincipal();
    }


    public static class Time {
        private Long id;
        private String nome;
        private LocalDate dataCriacao;
        private String corUniformePrincipal;
        private String corUniformeSecundario;

        private Long idCapitao;


        public Time(Long id, String nome, LocalDate dataCriacao, String corUniformePrincipal) {
            this.id = Objects.requireNonNull(id);
            this.nome = Objects.requireNonNull(nome);
            this.dataCriacao = Objects.requireNonNull(dataCriacao);
            this.corUniformePrincipal = Objects.requireNonNull(corUniformePrincipal);
        }

        public Time(Long id, String nome, LocalDate dataCriacao, String corUniformePrincipal, String corUniformeSecundario) {
            this(id, nome, dataCriacao, corUniformePrincipal);
            this.corUniformeSecundario = corUniformeSecundario;
        }

        public Long getId() {
            return id;
        }

        public String getNome() {
            return nome;
        }

        public LocalDate getDataCriacao() {
            return dataCriacao;
        }

        public String getCorUniformePrincipal() {
            return corUniformePrincipal;
        }

        public String getCorUniformeSecundario() {
            return corUniformeSecundario;
        }

        public Long getIdCapitao() {
            return idCapitao;
        }

        public void setIdCapitao(Long idCapitao) {
            this.idCapitao = idCapitao;
        }
    }

    public static class Jogador {
        private Long id;
        private Long idTime;
        private String nome;
        private LocalDate dataNascimento;
        private Integer nivelHabilidade; // 0 - 100
        BigDecimal salario;

        public Jogador(Long id, Long idTime, String nome, LocalDate dataNascimento, Integer nivelHabilidade, BigDecimal salario) {
            this.id = Objects.requireNonNull(id);
            this.idTime = Objects.requireNonNull(idTime);
            this.nome = Objects.requireNonNull(nome);
            this.dataNascimento = Objects.requireNonNull(dataNascimento);
            this.nivelHabilidade = Objects.requireNonNull(nivelHabilidade);
            this.salario = salario;
        }

        public Long getId() {
            return id;
        }

        public Long getIdTime() {
            return idTime;
        }

        public String getNome() {
            return nome;
        }

        public LocalDate getDataNascimento() {
            return dataNascimento;
        }

        public Integer getNivelHabilidade() {
            return nivelHabilidade;
        }

        public BigDecimal getSalario() {
            return salario;
        }
    }

}
