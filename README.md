# RotinaAgenteTI — Simulador de Rotina Diária (Agente de TI)

Aplicação em **Java (LTS)** que simula uma rotina diária de um candidato a **Agente de Tecnologia**, modelando eventos **determinísticos e estocásticos** para exercitar **POO**, **arquitetura em camadas** e boas práticas de engenharia de software.  
> Projeto com foco didático: organização, legibilidade, regras de negócio e evolução incremental.

---

## Sumário
- [Motivação](#motivação)
- [O que este projeto faz](#o-que-este-projeto-faz)
- [Arquitetura](#arquitetura)
- [Tecnologias](#tecnologias)
- [Como executar](#como-executar)
- [Estrutura do projeto](#estrutura-do-projeto)
- [Roadmap](#roadmap)
- [Contribuição](#contribuição)
- [Licença](#licença)

---

## Motivação

Este repositório existe para praticar, com um cenário “realista” porém controlado:
- **Programação Orientada a Objetos**
- **Separação de responsabilidades (camadas)**
- Evolução do código com **refatoração** e **manutenibilidade**
- Modelagem de uma rotina com regras e variações (probabilidade)

---

## O que este projeto faz

Atualmente, o projeto simula um ciclo de rotina com componentes de domínio e dispositivos, com foco em:
- Ciclo de vida da simulação (inicialização, deslocamento, alocação de recursos e processamento)
- Entidades imutáveis no domínio (ex.: `record`)
- Componentes simulados (ex.: dispositivo com bateria e conexão)
- Orquestração das regras na camada de serviço

> Conforme a evolução do projeto, a simulação tende a se aproximar de um ecossistema mais “enterprise”.

---

## Arquitetura

O sistema segue uma **arquitetura em camadas**, visando desacoplamento e manutenibilidade:

### 1) Model (Dados / Domínio)
Estruturas imutáveis e entidades do domínio.  
- `Assento.java`: entidade de recurso usando **Java Record**, favorecendo imutabilidade e integridade.

### 2) Equipment (Dispositivos / Hardware simulado)
Encapsula comportamento de componentes físicos simulados.  
- `FoneBluetooth.java`: simula dispositivo de áudio com **gestão de bateria** e **estado de conexão**.

### 3) Service (Regras de negócio / Orquestração)
Controla o fluxo da simulação e integra componentes.  
- `Rotina.java`: orquestra o ciclo da rotina (boot, deslocamento, alocação e viagem).  
  > (Se aplicável no código) pode usar cálculo probabilístico para apoiar decisões de alocação.

---

## Tecnologias

- **Java 17+**
- **Maven**
- **Git**

---

## Como executar

### Pré-requisitos
- **JDK 17+**
- **Maven** instalado (ou use o Maven da sua IDE)

### Passo a passo
1. Clone o repositório:
   ```bash
   git clone https://github.com/Pablit0rg/RotinaAgenteTI.git
   cd RotinaAgenteTI

## 🚀 Roadmap (Próximos Passos)
- [x] **Arquitetura Modular**: Separação em Camadas (Model, Service, Repository).
- [x] **Persistência de Dados**: Implementação de SQL com H2 Database e Spring Data JPA.
- [x] **Interoperabilidade**: Exportação de relatórios diários em formato JSON via HTTP Client.
- [x] **Automação (RPA)**: Integração com **n8n** e **Gmail** para alertas automáticos.
- [ ] **Cibersegurança**: Refinamento de variáveis de ambiente (.env) para proteção de credenciais.