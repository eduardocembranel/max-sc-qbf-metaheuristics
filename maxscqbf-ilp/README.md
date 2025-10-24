# MAX-SC-QBF Solver

Este projeto implementa um programa para resolver o problema **MAX-SC-QBF** (Maximum Set Covering Quadratic Binary Function), utilizando **Programação Linear Inteira (PLI)** com o solver **Gurobi**.

## Requisitos

- Sistema operacional baseado em UNIX
- Python 3.11 ou superior  
- [Gurobi Optimizer](https://www.gurobi.com/) versão **12.0.3**  
- Licença válida do Gurobi

## Instalação

### 1. Clone o repositório

```bash
git clone https://github.com/eduardocembranel/max-sc-qbf.git
cd max-sc-qbf
```

### 2. Crie e ative um ambiente virtual (venv)

```bash
python3 -m venv venv
source venv/bin/activate
```

### 3. Instale os pacotes

```bash
pip install -r requirements.txt

```

## Execução

Para rodar o solver PLI com uma instância localizada no diretório `instances`, use o seguinte comando:

```bash
python maxscqbf_ilp.py < instances/<nome_do_arquivo>
````
Substitua `<nome_do_arquivo>` pelo nome da instância desejada, por exemplo:

```bash
python maxscqbf_ilp.py < instances/exact_n400.txt
````

## Geração de instâncias

As instâncias de testes localizadas do diretório `instances` foram geradas com o seguinte comando:

```bash
python maxscqbf_generator.py
````
> A reexecução do comando acima irá gerar os mesmos 15 arquivos já presentes em `instances`, uma vez que o gerador faz uso de uma seed pré definida.

## Caso desejar executar o algoritmo de força bruta

Esse algoritmo é útil para resolver instâncias menores `n<=15` a fim de gerar uma solução ótima cujo valor pode ser comparado com o resultado obtido pelo solver PLI. Isso possibilita a validação da corretude da modelagem implementada.

Primeiramente, gere as instâncias menores com:

```bash
python maxscqbf_generator.py --type=small
````
Em seguida rode o seguinte comando para executar o algoritmo de força bruta (para `small_1` por exemplo):

```bash
python maxscqbf_bruteforce.py < instances/small_1.txt
```
Agora, execute o seguinte comando para resolver a mesma instância pelo solver PLI e compare os resultados obtidos.
```bash
python maxscqbf_ilp.py < instances/small_1.txt
```
