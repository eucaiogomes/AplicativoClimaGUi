import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WeatherAppGui extends JFrame {
    private JSONObject dadosClima;

    public WeatherAppGui(){
        // configura nossa interface gráfica e adiciona um título
        super("Aplicativo de Clima");

        // configura a interface gráfica para encerrar o processo do programa depois que ele for fechado
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // define o tamanho da nossa GUI (em pixels)
        setSize(450, 650);

        // carrega nossa interface gráfica no centro da tela
        setLocationRelativeTo(null);

        // torna nosso gerenciador de layout nulo para posicionar manualmente nossos componentes dentro da GUI
        setLayout(null);

        // impede qualquer redimensionamento da nossa interface gráfica
        setResizable(false);

        adicionarComponentesGui();
    }

    private void adicionarComponentesGui(){
        // campo de busca
        JTextField campoBusca = new JTextField();

        // define a localização e tamanho do nosso componente
        campoBusca.setBounds(15, 15, 351, 45);

        // altera o estilo e tamanho da fonte
        campoBusca.setFont(new Font("Dialog", Font.PLAIN, 24));

        add(campoBusca);

        // imagem da condição climática
        JLabel imagemCondicaoClima = new JLabel(carregarImagem("src/assets/cloudy.png"));
        imagemCondicaoClima.setBounds(0, 125, 450, 217);
        add(imagemCondicaoClima);

        // texto da temperatura
        JLabel textoTemperatura = new JLabel("10 C");
        textoTemperatura.setBounds(0, 350, 450, 54);
        textoTemperatura.setFont(new Font("Dialog", Font.BOLD, 48));

        // centraliza o texto
        textoTemperatura.setHorizontalAlignment(SwingConstants.CENTER);
        add(textoTemperatura);

        // descrição da condição climática
        JLabel descricaoCondicaoClima = new JLabel("Nuvens");
        descricaoCondicaoClima.setBounds(0, 405, 450, 36);
        descricaoCondicaoClima.setFont(new Font("Dialog", Font.PLAIN, 32));
        descricaoCondicaoClima.setHorizontalAlignment(SwingConstants.CENTER);
        add(descricaoCondicaoClima);

        // imagem da umidade
        JLabel imagemUmidade = new JLabel(carregarImagem("src/assets/humidity.png"));
        imagemUmidade.setBounds(15, 500, 74, 66);
        add(imagemUmidade);

        // texto da umidade
        JLabel textoUmidade = new JLabel("<html><b>Umidade</b> 100%</html>");
        textoUmidade.setBounds(90, 500, 85, 55);
        textoUmidade.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(textoUmidade);

        // imagem da velocidade do vento
        JLabel imagemVelocidadeVento = new JLabel(carregarImagem("src/assets/windspeed.png"));
        imagemVelocidadeVento.setBounds(220, 500, 74, 66);
        add(imagemVelocidadeVento);

        // texto da velocidade do vento
        JLabel textoVelocidadeVento = new JLabel("<html><b>Velocidade do Vento</b> 15km/h</html>");
        textoVelocidadeVento.setBounds(310, 500, 85, 55);
        textoVelocidadeVento.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(textoVelocidadeVento);

        // botão de busca
        JButton botaoBusca = new JButton(carregarImagem("src/assets/search.png"));

        // altera o cursor para um cursor de mão ao passar o mouse sobre o botão
        botaoBusca.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botaoBusca.setBounds(375, 13, 47, 45);
        botaoBusca.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // obtém a localização do usuário
                String entradaUsuario = campoBusca.getText();

                // valida a entrada - remove espaços em branco para garantir que o texto não esteja vazio
                if(entradaUsuario.replaceAll("\\s", "").length() <= 0){
                    return;
                }

                // recupera os dados do clima
                dadosClima = WeatherApp.getWeatherData(entradaUsuario);

                // atualiza a interface gráfica

                // atualiza a imagem da condição climática
                String condicaoClima = (String) dadosClima.get("weather_condition");

                // dependendo da condição, vamos atualizar a imagem do clima correspondente
                switch(condicaoClima){
                    case "Clear":
                        imagemCondicaoClima.setIcon(carregarImagem("src/assets/clear.png"));
                        break;
                    case "Cloudy":
                        imagemCondicaoClima.setIcon(carregarImagem("src/assets/cloudy.png"));
                        break;
                    case "Rain":
                        imagemCondicaoClima.setIcon(carregarImagem("src/assets/rain.png"));
                        break;
                    case "Snow":
                        imagemCondicaoClima.setIcon(carregarImagem("src/assets/snow.pngImage"));
                        break;
                }

                // atualiza o texto da temperatura
                double temperatura = (double) dadosClima.get("temperature");
                textoTemperatura.setText(temperatura + " C");

                // atualiza o texto da condição climática
                descricaoCondicaoClima.setText(condicaoClima);

                // atualiza o texto da umidade
                long umidade = (long) dadosClima.get("humidity");
                textoUmidade.setText("<html><b>Umidade</b> " + umidade + "%</html>");

                // atualiza o texto da velocidade do vento
                double velocidadeVento = (double) dadosClima.get("windspeed");
                textoVelocidadeVento.setText("<html><b>Velocidade do Vento</b> " + velocidadeVento + "km/h</html>");
            }
        });
        add(botaoBusca);
    }

    // usado para criar imagens nos nossos componentes da interface gráfica
    private ImageIcon carregarImagem(String caminhoRecurso){
        try{
            // lê o arquivo de imagem a partir do caminho fornecido
            BufferedImage imagem = ImageIO.read(new File(caminhoRecurso));

            // retorna um ícone de imagem para que nosso componente possa exibi-lo
            return new ImageIcon(imagem);
        }catch(IOException e){
            e.printStackTrace();
        }

        System.out.println("Não foi possível encontrar o recurso");
        return null;
    }
}
