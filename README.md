# Jackut — Rede Social Acadêmica

Projeto da disciplina de **Programação 2 (P2)** do Instituto de Computação da UFAL.

Jackut é uma rede social simplificada em Java com criação de contas, edição de perfil, sistema de amizades e envio de recados entre usuários.

---

## Funcionalidades

- **US1 — Criação de conta**: cadastro com login, senha e nome; validações de login/senha inválidos e login duplicado
- **US2 — Edição de perfil**: atributos livres (chave-valor); qualquer atributo pode ser criado ou editado
- **US3 — Adição de amigos**: sistema de convites; amizade confirmada apenas quando ambos os lados se adicionam
- **US4 — Envio de recados**: fila FIFO por usuário; recados lidos em ordem de chegada

---

## Estrutura do Projeto

```
P2-2023.1-JACKUT/
├── lib/
│   └── easyaccept.jar
├── src/
│   ├── Main.java
│   └── br/ufal/ic/p2/jackut/
│       ├── Facade.java
│       ├── Sistema.java
│       ├── Usuario.java
│       └── Sessao.java
├── tests/
│   ├── us1_1.txt / us1_2.txt
│   ├── us2_1.txt / us2_2.txt
│   ├── us3_1.txt / us3_2.txt
│   └── us4_1.txt / us4_2.txt
├── .vscode/
│   └── settings.json
└── out/  (gerado pela compilação)
```

---

## Como Compilar e Executar

### Pré-requisitos

- Java JDK 11 ou superior
- PowerShell (Windows)

### Compilar

```powershell
javac -encoding UTF-8 -parameters -cp "lib\easyaccept.jar" -d out src\br\ufal\ic\p2\jackut\*.java src\Main.java
```

### Executar os testes

```powershell
java "-Dfile.encoding=ISO-8859-1" -cp "out;lib\easyaccept.jar" Main
```

>  **O `-Dfile.encoding=ISO-8859-1` é obrigatório.** Os arquivos de teste estão em ISO-8859-1 e o EasyAccept compara as mensagens de erro byte a byte nesse encoding. Sem esse parâmetro, os acentos nas mensagens de exceção não batem e os testes falham.
>
>  **No PowerShell**, a flag deve estar entre aspas: `"-Dfile.encoding=ISO-8859-1"`. Sem aspas, o PowerShell quebra o argumento no `=`.

---

## Resultado dos Testes

| Arquivo    | User Story              | Testes | Resultado   |
|------------|-------------------------|--------|-------------|
| us1_1.txt  | Criação de conta        | 17     | ✅ PASSOU   |
| us1_2.txt  | Persistência — conta    | 7      | ✅ PASSOU   |
| us2_1.txt  | Edição de perfil        | 36     | ✅ PASSOU   |
| us2_2.txt  | Persistência — perfil   | 13     | ✅ PASSOU   |
| us3_1.txt  | Adição de amigos        | 46     | ✅ PASSOU   |
| us3_2.txt  | Persistência — amigos   | 10     | ✅ PASSOU   |
| us4_1.txt  | Envio de recados        | 42     | ✅ PASSOU   |
| us4_2.txt  | Persistência — recados  | 13     | ✅ PASSOU   |
| **TOTAL**  |                         | **184**| **184/184** |

---

## Configuração do VS Code

Os arquivos `.txt` de teste precisam estar em **ISO-8859-1**. O projeto já inclui `.vscode/settings.json` configurado para isso. Se precisar criar manualmente:

```powershell
New-Item -ItemType Directory -Force .vscode | Out-Null
'{"[plaintext]":{"files.encoding":"latin1"}}' | Set-Content .vscode\settings.json
```

Se os acentos aparecerem corrompidos no editor, clique em **UTF-8** no canto inferior direito → **Reopen with Encoding** → **Western (ISO 8859-1)**. Isso é apenas visual — se os testes passam, o arquivo em disco está correto.

---

## Persistência

Os dados são gravados automaticamente ao chamar `encerrarSistema()` no arquivo `jackut_dados.ser` (serialização Java). Na próxima execução os dados são carregados automaticamente. Para apagar tudo, use `zerarSistema()`.

---

## Tecnologias

- Java (JDK 11+)
- [EasyAccept](https://github.com/fbrubbo/easyaccept) — framework de testes de aceitação
