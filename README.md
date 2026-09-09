# Simulador Hospitalar Multiagente

Sistema de simulação multiagente para atendimento hospitalar em um ambiente discreto representado por um grid bidimensional. O projeto foi desenvolvido em Java com Processing e modela pacientes como agentes que percorrem diferentes etapas do atendimento hospitalar, interagindo com o mapa, filas de atendimento, sistema de senhas, classificação de risco e movimentação pelo ambiente.

O sistema é organizado em módulos responsáveis pelo mapa do hospital, representação dos pacientes, atendimento, estruturas de dados, movimentação, controle de tempo e interface gráfica.

---

## 👥 Equipe do Projeto

|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
|        Integrante         |  Usuário GitHub   |                                   Responsabilidade principal                                  |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| **Pedro Mendes Santana**  | `@Pedro-Santtana` | Algoritmo Wavefront, movimentação dos agentes, fila de coordenadas e gerenciamento do tempo   |
| **Caio Henrique Macedo**  | `@Caio01-10`      | Interface gráfica, gerenciamento das telas e renderização dos agentes                         |
| **Davi Campos Montijo**   | `@DaviMontijo`    | Modelagem do mapa, grid hospitalar, renderização do hospital e sistema de senhas              |
| **Bento Martins Tristão** | `@BentoTristao`   | Lógica de atendimento, paciente, estruturas de dados e classificação de risco Manchester      |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|


---

## 🤝 Apoio na Integração

A integração entre Processing e Java contou com auxílio de:

- [Eduardo Lamonier](https://github.com/eduardo-lamounier)
- [Heitor Freitas](https://github.com/Zerocentos)

---

## 🎯 Objetivo

O objetivo do projeto é simular o fluxo de pacientes dentro de um setor hospitalar utilizando um modelo multiagente.

Cada paciente possui características próprias, como:

- identificador;
- senha de atendimento;
- indicação de atendimento preferencial;
- sinais vitais;
- classificação de risco;
- posição atual no mapa;
- destino;
- estado atual da máquina de estados.

A simulação integra estruturas de dados implementadas pela equipe, busca de caminhos em grid, filas de atendimento, árvore de decisão e uma interface gráfica desenvolvida com Processing.

---

## 🏥 Modelagem do Hospital

O hospital é representado por uma matriz bidimensional de `Bloco`.

Cada célula possui um tipo identificado por um caractere:

| Símbolo | Elemento | Função |
|---|---|---|
| `.` | Chão | Célula transitável |
| `#` | Parede | Obstáculo físico |
| `G` | Gerador | Ponto de entrada dos pacientes |
| `R` | Removedor | Ponto de saída |
| `T` | Totem | Emissão das senhas |
| `A` | Assento | Local de espera |
| `E` | Enfermeira | Posto de triagem |
| `M` | Médico | Posto de consulta |

O `GridHospital` é responsável por carregar o mapa a partir de um arquivo de texto, validar sua estrutura e identificar as posições do gerador, removedor, totem, enfermeiros, médicos e assentos.

A validação de trânsito é centralizada no método `podeTransitar()`. Dessa forma, a lógica de movimentação consulta o mapa para verificar se uma determinada posição pode ser ocupada.

---

## 🔄 Fluxo do Paciente

O ciclo de vida de um paciente é representado pela enumeração `EstadoPaciente`.

O fluxo é organizado da seguinte forma:

```text
NASCENDO
   ↓
INDO_TOTEM
   ↓
ESPERANDO_TRIAGEM
   ↓
INDO_TRIAGEM
   ↓
EM_TRIAGEM
   ↓
ESPERANDO_CONSULTA
   ↓
INDO_CONSULTA
   ↓
EM_CONSULTA
   ↓
INDO_REMOVEDOR
```

Cada estado representa uma etapa diferente do atendimento e permite que a simulação controle o comportamento do agente conforme sua situação atual.

A classe `Paciente` mantém tanto os dados do agente quanto seu estado, posição e destino, enquanto os gerenciadores da simulação executam as ações correspondentes a cada etapa.

---

## 🎫 Sistema de Senhas e Filas

O `Totem` mantém dois contadores independentes:

- senhas normais;
- senhas preferenciais.

As senhas são geradas no formato:

```text
N001
N002
N003
...

P001
P002
P003
...
```

O atendimento da triagem utiliza duas filas:

```text
Fila de Triagem Normal
Fila de Triagem Preferencial
```

O `GerenciadorAtendimento` aplica a regra de alternância da triagem, permitindo no máximo dois pacientes preferenciais consecutivos antes de atender um paciente normal, quando houver um paciente normal aguardando.

Depois da triagem, o paciente recebe uma classificação de risco e é encaminhado para uma das cinco filas médicas:

```text
VERMELHA
LARANJA
AMARELA
VERDE
AZUL
```

Dentro de cada fila, o atendimento segue a ordem de chegada.

---

## 🌳 Classificação de Risco Manchester

A classificação de risco é implementada pela classe `ArvoreManchester`.

A árvore é armazenada em um vetor de objetos `NoManchester`, utilizando a representação clássica de uma árvore binária em vetor.

Para um nó localizado no índice `i`:

```text
Filho esquerdo = 2 * i + 1
Filho direito  = 2 * i + 2
```

O percurso da árvore utiliza os sinais vitais do paciente para tomar as decisões.

A estrutura utilizada pelo projeto considera:

```text
Índice 0 → Saturação de Oxigênio
Índice 1 → Temperatura Corporal
Índice 2 → Nível de Dor
Índice 3 → Consciência Alterada
```

A árvore implementada possui a seguinte sequência de decisões:

```text
Consciência Alterada == 1?
        │
        ├── SIM → VERMELHA
        │
        └── NÃO
              ↓
       Saturação < 92?
          │         │
        SIM        NÃO
         ↓           ↓
     LARANJA      Dor >= 8?
                    │      │
                  SIM     NÃO
                   ↓        ↓
               AMARELA  Temperatura >= 38?
                              │       │
                            SIM      NÃO
                             ↓         ↓
                           VERDE      AZUL
```

Essa representação permite realizar a classificação por meio de um percurso de árvore sem utilizar estruturas de decisão externas à própria árvore.

---

## 🧱 Estruturas de Dados

O projeto utiliza estruturas de dados implementadas pela própria equipe.

### Lista Encadeada

A classe `ListaEncadeada` mantém os pacientes ativos na simulação.

Sua implementação utiliza objetos `NoPaciente` ligados entre si:

```text
[P1] → [P2] → [P3] → [P4]
```

Principais operações:

```java
inserir(Paciente paciente)
remover(Paciente paciente)
getPaciente(int indice)
tamanho()
vazia()
limpar()
```

A lista mantém uma referência para o primeiro nó e controla a quantidade de elementos.

### Fila de Pacientes

A classe `FilaPacientes` implementa uma fila baseada em nós encadeados.

```text
INÍCIO → [P1] → [P2] → [P3] ← FIM
```

Principais operações:

```java
enfileirar(Paciente paciente)
desenfileirar()
consultarInicio()
tamanho()
vazia()
limpar()
```

A estrutura segue o comportamento FIFO:

```text
First In, First Out
```

O uso de referências para início e fim permite inserir no final e remover do início sem percorrer toda a fila.

### Fila de Coordenadas

A classe `FilaCoordenadas` possui uma estrutura semelhante, mas é utilizada pelo algoritmo de Wavefront para armazenar coordenadas do grid durante a busca.

---

## 🧭 Algoritmo Wavefront

A movimentação dos pacientes utiliza o algoritmo Wavefront, implementado na classe `WavefrontPathfinder`.

O algoritmo funciona como uma busca em largura (`BFS`).

O cálculo começa no destino:

```text
Destino = 0
```

Depois as células vizinhas recebem valores crescentes de distância:

```text
0
1
2
3
4
...
```

Uma matriz de distâncias é criada com as mesmas dimensões do hospital.

As posições inicialmente recebem:

```text
-1
```

indicando que ainda não foram visitadas.

Durante a busca, são analisadas as quatro direções:

```text
        cima
         ↑
esquerda ← → direita
         ↓
       baixo
```

A fila `FilaCoordenadas` controla a ordem em que as posições são processadas.

O algoritmo consulta o `GridHospital` para ignorar células que não podem ser atravessadas. 
Depois que a matriz é calculada, o agente pode escolher entre suas células vizinhas a posição com menor distância até o destino. Caso a melhor posição esteja ocupada, outra alternativa pode ser considerada.

---

## ⏱️ Gerenciamento de Tempo

A classe `GerenciadorTempo` controla o próximo instante de chegada de pacientes.

O intervalo entre chegadas é calculado utilizando uma distribuição exponencial:

```text
X = -μ ln(1 - U)
```

onde:

- `μ` representa a média utilizada na simulação;
- `U` é um valor aleatório;
- `X` representa o próximo intervalo de chegada.

No código, o cálculo é realizado por:

```java
proximaChegada =
    (float) (-mediaSpawn * Math.log(1.0 - u));
```

A utilização da distribuição exponencial evita intervalos fixos e permite que as chegadas ocorram de maneira variável.

O `GerenciadorTempo` também fornece métodos para verificar se chegou o momento de gerar um novo paciente e para calcular um novo intervalo depois de cada chegada.

---

## ⚙️ SimuladorEngine

A classe `SimuladorEngine` funciona como núcleo de integração da simulação.

Ela mantém referências para:

```text
GridHospital
GerenciadorMovimento
GerenciadorAtendimento
GerenciadorTempo
ListaEncadeada de pacientes ativos
```

Durante a atualização da simulação, o Engine controla:

1. passagem do tempo;
2. chegada de novos pacientes;
3. movimentação;
4. atualização do atendimento;
5. remoção de pacientes que terminam o fluxo.

A arquitetura central pode ser representada por:

```text
                    SimuladorEngine
                           │
       ┌───────────────────┼───────────────────┐
       ↓                   ↓                   ↓
 GridHospital      GerenciadorMovimento  GerenciadorAtendimento
                           │                   │
                           ↓                   ↓
                     Wavefront           Filas + Manchester

                           │
                           ↓
                   GerenciadorTempo
```

---

## 🖥️ Interface Gráfica

A interface foi desenvolvida utilizando Processing.

A classe `Sketch` é responsável pela aplicação principal e pela comunicação com os componentes gráficos.

O `GerenciadorTelas` controla os diferentes estados da interface:

```text
MENU_INICIAL
SELECAO_MAPA
EM_EXECUCAO
PAUSADO
CREDITOS
```

O gerenciamento das telas separa a lógica dos menus da lógica interna dos pacientes e do hospital.

---

## 🎨 Renderização

A renderização foi dividida em três classes:

### `RenderizadorMapa`

Responsável por desenhar os elementos do grid do hospital.

### `RenderizadorAgente`

Responsável por desenhar os pacientes e suas informações visuais.

### `RenderizadorHospital`

Coordena os renderizadores e organiza a ordem de desenho:

```text
1. Mapa
2. Pacientes
3. HUD
```

Essa divisão evita concentrar toda a lógica de renderização em uma única classe.

---

## 🗂️ Estrutura do Projeto

```text
.
├── data/
│   ├── fontes/
│   │   └── fonte.ttf
│   └── sprites/
│       ├── enfermeira.png
│       ├── medico.png
│       ├── menuInicial.png
│       ├── menuPause.png
│       └── paciente.png
│
├── src/
│   ├── app/
│   │   ├── SimuladorEngine.java
│   │   └── Sketch.java
│   │
│   ├── atendimento/
│   │   ├── ArvoreManchester.java
│   │   ├── GerenciadorAtendimento.java
│   │   └── NoManchester.java
│   │
│   ├── enums/
│   │   ├── Cor.java
│   │   ├── EstadoPaciente.java
│   │   └── EstadoTela.java
│   │
│   ├── estrutura/
│   │   ├── FilaPacientes.java
│   │   ├── ListaEncadeada.java
│   │   └── NoPaciente.java
│   │
│   ├── interfaceGrafica/
│   │   └── GerenciadorTelas.java
│   │
│   ├── mapa/
│   │   ├── Assento.java
│   │   ├── Bloco.java
│   │   ├── GridHospital.java
│   │   └── Totem.java
│   │
│   ├── modelo/
│   │   ├── Coordenada.java
│   │   └── Paciente.java
│   │
│   ├── movimento/
│   │   ├── FilaCoordenadas.java
│   │   ├── GerenciadorMovimento.java
│   │   ├── GerenciadorTempo.java
│   │   ├── NoCoordenada.java
│   │   └── WavefrontPathfinder.java
│   │
│   └── renderizadores/
│       ├── RenderizadorAgente.java
│       ├── RenderizadorHospital.java
│       └── RenderizadorMapa.java
│
├── uml/
│   └── UML.puml
│
├── pom.xml
├── README.md
├── run.sh
└── runWindows.bat
```

---

## 🚀 Como Executar

### Pré-requisitos

- JDK 17 ou superior;
- Apache Maven;
- ambiente compatível com a execução do Processing utilizado pelo projeto.

### Linux / macOS

```bash
chmod +x run.sh
./run.sh
```

### Windows

```bat
runWindows.bat
```

### Execução direta pelo Maven

```bash
mvn clean compile exec:java 
```

---

## 📐 Diagrama UML

O projeto possui um diagrama UML em:

```text
uml/UMLSimuladorHospitalarMultiagente.pdf
```

O diagrama representa as principais classes, seus atributos, métodos e relacionamentos dentro da arquitetura do sistema.

---

## 🧩 Organização da Arquitetura

A divisão geral do sistema é:

```text
                         Sketch
                           │
                           ↓
                   GerenciadorTelas
                           │
                           ↓
                    SimuladorEngine
                           │
        ┌──────────────────┼──────────────────┐
        │                  │                  │
        ↓                  ↓                  ↓
  GridHospital    GerenciadorMovimento  GerenciadorAtendimento
        │                  │                  │
        │                  ↓                  ↓
        │            WavefrontPathfinder   Filas + Manchester
        │
        └──────────────────┐
                           ↓
                  Pacientes Ativos
                           │
                           ↓
                 RenderizadorHospital
                    ┌──────┴──────┐
                    ↓             ↓
           RenderizadorMapa  RenderizadorAgente
```

A separação de responsabilidades permite que o mapa, os agentes, as estruturas de dados, a movimentação, o atendimento e a interface sejam tratados por componentes específicos.

---

## 📚 Principais Conceitos Aplicados

O projeto reúne os seguintes conceitos:

- Programação Orientada a Objetos;
- classes e objetos;
- encapsulamento;
- enumerações;
- máquina de estados;
- listas encadeadas;
- filas;
- árvores binárias;
- representação de árvore em vetor;
- busca em largura (BFS);
- algoritmo Wavefront;
- manipulação de matrizes;
- geração de números aleatórios;
- distribuição exponencial;
- simulação multiagente;
- interface gráfica com Processing;
- organização modular de um sistema.

---

## 👨‍💻 Histórico de Desenvolvimento

A evolução do projeto pode ser acompanhada pelo histórico de commits do repositório Git.

Para gerar novamente o arquivo de histórico:

```bash
git log --oneline > historico_commits.txt
```

---


## 📌 Observação

O projeto foi desenvolvido com foco na integração entre estruturas de dados, algoritmos de busca, simulação multiagente e interface gráfica, mantendo responsabilidades separadas entre os diferentes módulos do sistema.
