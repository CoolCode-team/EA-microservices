

# 📦 Projeto da 2ª Unidade - Arquitetura de Microserviços

## 📌 Visão Geral

Este projeto dá continuidade ao trabalho iniciado na 1ª Unidade, onde foi desenvolvida uma aplicação monolítica com um front-end conectado a um back-end centralizado (ver Figura 01 no material da disciplina). Para esta 2ª Unidade, o objetivo é reestruturar o back-end para uma **arquitetura de microserviços**, mantendo inicialmente o front-end inalterado, exceto pela URL de acesso à nova API.

---

## 🧱 Objetivo da 2ª Unidade

O foco é **identificar os candidatos a microserviços** a partir da aplicação monolítica e reconstruí-los seguindo os princípios de:

* 🔗 Baixo Acoplamento
* 🔍 Abstração
* 🔁 Reúso
* 🧠 Autonomia
* 🚫 Ausência de Estado
* 🎯 Alta Coesão

Cada microserviço deve estar isolado em seu respectivo **subdomínio**, com **seu próprio banco de dados (PostgreSQL)**.

---

## 🏗️ Arquitetura da Solução

A nova arquitetura será composta por:

### ✅ Microserviços

* Cada microserviço representa um subdomínio específico
* Desenvolvidos em **Java**
* Conectados a **bancos PostgreSQL independentes**

### 🧰 API Gateway

* Responsável por:

  * Roteamento
  * Autenticação
  * Agregação de dados
  * Ponto único de entrada
* Recomendação: uso de **Spring Cloud Gateway**

### 📡 Service Discovery

* Serviço que permite o registro e a descoberta dinâmica dos microserviços
* Recomendação: uso do **Eureka Server (Spring Netflix Eureka)**

---

## 🛠️ Tecnologias Utilizadas

* **Java (Spring Boot / Spring Cloud)**
* **PostgreSQL**
* **Eureka Server (Service Discovery)**
* **Spring Cloud Gateway (API Gateway)**
* **Ferramenta para testes: Postman**

---

## ⚠️ Requisitos Obrigatórios

* O projeto **deve estar funcional** (sem erros)
* Todos os recursos definidos devem ser implementados
* Cada microserviço com **seu banco separado** (SGBD: PostgreSQL)
* A linguagem usada deve ser **exclusivamente Java**
* Testes poderão ser demonstrados via ferramentas de consumo de API
* Não é necessário apresentar com o front-end, mas se utilizado, dispensa o uso das ferramentas de testes

---

## 👥 Membros da Equipe

| Matrícula | Nome Completo                         | E-mail                                                                            |
| --------- | ------------------------------------- | --------------------------------------------------------------------------------- |
| 200028802 | Maria Clara Ferreira Mendes           | [mariaclara.mendes@ucsal.edu.br](mailto:mariaclara.mendes@ucsal.edu.br)           |
| 200028863 | Maria Fernanda Pereira Barçante Costa | [mariafernanda.costa@ucsal.edu.br](mailto:mariafernanda.costa@ucsal.edu.br)       |
| 200016718 | Davi dos Santos Ribeiro               | [davi.ribeiro@ucsal.edu.br](mailto:davi.ribeiro@ucsal.edu.br)                     |
| 200029600 | Alisson de Oliveira dos Santos        | [alissonoliveira.santos@ucsal.edu.br](mailto:alissonoliveira.santos@ucsal.edu.br) |
| 200029897 | Larissa da Silva Santana              | [larissasilva.santana@ucsal.edu.br](mailto:larissasilva.santana@ucsal.edu.br)     |
| 200028754 | Murilo Moreno dos Santos Silva        | [murilomoreno.santos@ucsal.edu.br](mailto:murilomoreno.santos@ucsal.edu.br)       |



r
