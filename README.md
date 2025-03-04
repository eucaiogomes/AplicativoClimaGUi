<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Aplicativo Clima GUI</title>
</head>
<body>
    <h1>Aplicativo Clima GUI</h1>
    <p>Este é um aplicativo de clima simples desenvolvido em Java, que utiliza a API Open-Meteo para obter dados climáticos em tempo real, como temperatura, umidade, condição do tempo e velocidade do vento, com uma interface gráfica de usuário (GUI).</p>

<h2>Funcionalidades</h2>
    <ul>
        <li>Exibe o clima atual de uma localização fornecida pelo usuário.</li>
        <li>Conecta-se à API Open-Meteo para obter dados como:
            <ul>
                <li>Temperatura</li>
                <li>Umidade relativa</li>
                <li>Condição climática (ex: claro, nublado, chuvoso)</li>
                <li>Velocidade do vento</li>
            </ul>
        </li>
        <li>Interface gráfica simples para facilitar o uso.</li>
    </ul>

<h2>Requisitos</h2>
    <ul>
        <li>Java 8 ou superior.</li>
        <li>Conexão com a internet (necessária para buscar dados climáticos da API).</li>
    </ul>

<h2>Como Usar</h2>
    <h3>Passo 1: Clonar o Repositório</h3>
    <p>Primeiro, clone este repositório para o seu ambiente local:</p>
    <pre><code>git clone https://github.com/eucaiogomes/AplicativoClimaGUi.git</code></pre>

<h3>Passo 2: Configurar o Projeto</h3>
    <p>Certifique-se de ter o Java instalado no seu computador. Se não tiver, instale a versão mais recente do <a href="https://www.oracle.com/java/technologies/javase-jdk11-downloads.html" target="_blank">Java JDK</a>.</p>

<h3>Passo 3: Compilar e Executar</h3>
    <ol>
        <li>Navegue até o diretório do projeto no terminal ou Git Bash.</li>
        <li>Compile o projeto com o comando:
            <pre><code>javac WeatherApp.java</code></pre>
        </li>
        <li>Execute o projeto com o comando:
            <pre><code>java WeatherApp</code></pre>
        </li>
        <li>O aplicativo pedirá o nome de uma localização. Digite o nome da cidade ou região desejada e o aplicativo retornará as informações climáticas atuais.</li>
    </ol>

 <h2>Como Contribuir</h2>
    <ol>
        <li>Faça um fork deste repositório.</li>
        <li>Crie uma branch com a sua nova funcionalidade (<code>git checkout -b minha-nova-funcionalidade</code>).</li>
        <li>Faça commit das suas alterações (<code>git commit -am 'Adicionando nova funcionalidade'</code>).</li>
        <li>Envia para o seu repositório (<code>git push origin minha-nova-funcionalidade</code>).</li>
        <li>Envie um Pull Request para este repositório.</li>
    </ol>

   

<h2>Agradecimentos</h2>
    <p>A API Open-Meteo por fornecer dados climáticos em tempo real.</p>
</body>
</html>
