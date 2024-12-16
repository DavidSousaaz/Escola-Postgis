package escolas.view;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class JanelaInicial extends JFrame {

    public JanelaInicial() {
        // Criar a janela principal
        JFrame frame = new JFrame("Buscar Escolas no Mapa de Monteiro");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Centraliza a janela na tela

        // Painel principal com layout absoluto
        JPanel panel = new JPanel();
        panel.setLayout(null); // Layout absoluto para posicionar os componentes manualmente

        // Criando os botões
        JButton botaoCadastrar = new JButton("Cadastrar Escola");
        JButton botaoRemover = new JButton("Remover Escola");
        JButton botaoVisualizar = new JButton("Visualizar Mapa");
        JButton botaoSair = new JButton("Sair");

        // Definindo o tamanho manualmente dos botões com setBounds
        botaoCadastrar.setBounds(100, 80, 200, 40); // Posição (100, 80) e tamanho (200, 40)
        botaoRemover.setBounds(100, 130, 200, 40); // Posição (100, 130) e tamanho (200, 40)
        botaoVisualizar.setBounds(100, 180, 200, 40); // Posição (100, 180) e tamanho (200, 40)
        botaoSair.setBounds(100, 230, 200, 40); // Posição (100, 230) e tamanho (200, 40)

        // Adicionando os botões ao painel
        panel.add(botaoCadastrar);
        panel.add(botaoRemover);
        panel.add(botaoVisualizar);
        panel.add(botaoSair);
        
        //ligando ao ouvintes
        
        JanelaInicialListener ouvinte= new JanelaInicialListener(frame);
        
        botaoCadastrar.addActionListener(ouvinte);
        botaoRemover.addActionListener(ouvinte);
        botaoVisualizar.addActionListener(ouvinte);
        botaoSair.addActionListener(ouvinte);

        // Texto em negrito e caixa alta
        JLabel titulo = new JLabel("BUSCAR ESCOLAS NO MAPA DE MONTEIRO");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setForeground(Color.BLACK); // Cor preta para o título para contrastar com a imagem
        titulo.setBounds(0, 20, 400, 30); // Centralizar o título no topo
        panel.add(titulo);

        // Adicionando o painel principal à janela
        frame.add(panel);

        // Tornar a janela visível
        frame.setVisible(true);
    }
}

