# Atividade Computacional 1 - Inteligência Artificial UNIFOR

## Descrição
Este projeto é parte da disciplina de Inteligência Artificial da Universidade de Fortaleza (UNIFOR), sob a orientação do Professor Vasco Furtado. A atividade envolve a especificação e implementação de agentes inteligentes (racionais) em um jogo chamado "poupador".

## Objetivos
- Implementar agentes inteligentes que atuem como personagens no jogo.
- Utilizar o framework em Java fornecido, focando na parte comportamental dos agentes.

## Agentes
- **Agente-Poupador**: Deve coletar moedas e depositá-las em um banco para evitar roubos pelo Agente-Ladrão.
- **Agente-Ladrão**: Tem o objetivo de roubar moedas do Agente-Poupador.

## Ambiente
- O jogo se passa em um labirinto com moedas, pastilhas de poder, e agências bancárias.
- O labirinto é representado por uma matriz de células.

## Sensores e Atuadores
- Os agentes têm sensores visuais e olfativos para perceber o ambiente.
- Atuadores permitem movimentação no labirinto, coleta de moedas e interação com o banco.

## Implementação
- O código para o Agente-Poupador deve herdar da classe `ProgramaPoupador`.
- O código para o Agente-Ladrão deve herdar da classe `ProgramaLadrao`.
- Métodos para percepção e atuação já estão disponíveis no framework.

## Termino da Partida
- O jogo termina quando o tempo se esgota, e o vencedor é o agente com mais moedas coletadas.
