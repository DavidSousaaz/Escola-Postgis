# Projeto: Sistema de Localização Geoespacial com PostGIS

## Descrição
Este projeto é um sistema em **Java** que utiliza **PostgreSQL** com extensão **PostGIS** para manipulação de dados geoespaciais.
Permite a inserção, busca, atualização, remoção e consultas complexas envolvendo coordenadas geográficas, como pontos, linhas e polígonos.

---

## Funcionalidades
1. **Inserção de Pontos**: Adiciona locais ao banco de dados utilizando coordenadas geográficas (latitude e longitude).
2. **Atualização de Coordenadas**: Atualiza as coordenadas de um ponto específico.
3. **Exclusão de Pontos**: Remove um ponto do banco de dados utilizando seu identificador.
4. **Visualização de Pontos**: Mostra informações detalhadas de um ponto com base no ID ou no nome.
5. **Busca por Distância**: Retorna os pontos que estão dentro de uma certa distância de um ponto fornecido.
6. **Busca por Polígonos**: Retorna todos os pontos que estão dentro de uma área delimitada (polígono WKT).
7. **Busca por Linhas**: Identifica e retorna os pontos que estão próximos a uma linha formada por dois pontos.

---

## Tecnologias Utilizadas
- **Java**: Linguagem principal do projeto.
- **PostgreSQL**: Banco de dados relacional utilizado.
- **PostGIS**: Extensão para PostgreSQL para manipulação geoespacial.
- **Maven** (Opcional): Gerenciamento de dependências do projeto.


## Configuração do Banco de Dados

1. **Instalação do PostgreSQL e PostGIS**:
   ```sql
   CREATE EXTENSION postgis;
   ```

2. **Criação da Tabela**:
   ```sql
   CREATE TABLE locais (
       id SERIAL PRIMARY KEY,
       nome VARCHAR(255),
       coordenadas GEOMETRY(Point, 4326) -- 4326 é o sistema de coordenadas WGS84
   );
   ```


