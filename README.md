# Rotina Agente TI - Sistema de Simulação de Rotina Diária

## Visão Geral do Projeto
O projeto Rotina Agente TI é uma aplicação baseada em Java (LTS) desenvolvida para simular e otimizar a rotina diária de um candidato ao cargo de Agente de Tecnologia. O software modela processos estocásticos e determinísticos, aplicando conceitos de Engenharia de Software, Programação Orientada a Objetos (POO) e Arquitetura em Camadas.

## Arquitetura da Solução
O sistema foi refatorado para seguir o padrão de arquitetura em camadas, visando desacoplamento e manutenibilidade:

### 1. Model (Camada de Dados)
Responsável pelas estruturas de dados imutáveis do sistema.
- **Assento.java**: Implementação de Java Record para representar a entidade de recurso (assento), garantindo imutabilidade e integridade dos dados.

### 2. Equipment (Camada de Hardware/Dispositivos)
Encapsula a lógica de componentes físicos simulados.
- **FoneBluetooth.java**: Simula o comportamento de um dispositivo de áudio, incluindo gestão de bateria (decaimento linear) e estado de conexão.

### 3. Service (Camada de Regra de Negócio)
Orquestra o fluxo de execução e a interação entre os componentes.
- **Rotina.java**: Controlador principal que gerencia o ciclo de vida da simulação (boot, deslocamento, alocação de recursos e processamento de viagem). Utiliza algoritmos de Regressão Linear para cálculo probabilístico de alocação de assentos.

## Tecnologias Utilizadas
- **Java 17+**: Linguagem principal.
- **Maven**: Gerenciamento de dependências e build.
- **Git**: Controle de versão.

## Como Executar
1. Certifique-se de ter o JDK 17 instalado.
2. Clone este repositório.
3. Execute a classe principal via Maven ou IDE.