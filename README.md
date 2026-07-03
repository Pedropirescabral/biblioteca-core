# Biblioteca Core — Projeto Integrador

Núcleo de regras de negócio do sistema de gerenciamento de empréstimos de uma biblioteca, desenvolvido como parte do Projeto Integrador (Etapa 6).

## Objetivo

Este projeto reorganiza as regras de negócio de um sistema de biblioteca em classes coesas e desacopladas, aplicando os princípios SOLID e o padrão Repository, para que o mesmo núcleo (`biblioteca-core`) possa ser reutilizado tanto em uma aplicação desktop quanto em uma aplicação web (ver `biblioteca-web`, Etapa 9).

## Estrutura de pacotes

```
br.com.biblioteca
 ├── model        -> Entidades de domínio (Livro, Membro, Emprestimo, StatusEmprestimo)
 ├── exception     -> Exceções de negócio específicas
 ├── repository    -> Interfaces de acesso a dados (abstrações)
 │    └── impl     -> Implementações em memória (para testes/demonstração)
 ├── service       -> Regras de negócio (LivroService, MembroService, EmprestimoService, CalculadoraMulta)
 └── Main.java     -> Teste manual (smoke test) executado via main()
```

## Como executar

Projeto Maven, compatível com NetBeans (`Abrir Projeto` → selecionar a pasta `biblioteca-core`).

Via linha de comando:
```bash
mvn compile exec:java -Dexec.mainClass="br.com.biblioteca.Main"
```

## Documentação

Veja o relatório completo (princípios SOLID aplicados, refatorações e code smells eliminados) na pasta `/docs` do repositório.
