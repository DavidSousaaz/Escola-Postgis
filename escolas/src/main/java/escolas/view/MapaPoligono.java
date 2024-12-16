package escolas.view;

import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.Coordinate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import escolas.dao.EscolaDao;

public class MapaPoligono {

    private static JMapViewer mapViewer;
    private static List<Coordinate> pontosPoligono = new ArrayList<>();

    public static void criarMapa() {
        JFrame frame = new JFrame("Mapa");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Inicializa o mapa
        mapViewer = new CustomMapViewer();
        mapViewer.setZoomControlsVisible(true);
        mapViewer.setDisplayPosition(new Coordinate(-7.8886, -37.1205), 12); // MontCity

        // Ouvinte para salvar os cliques (pontos)
        mapViewer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) { // Clique esquerdo: adicionar ponto
                    Coordinate coord = (Coordinate) mapViewer.getPosition(e.getPoint());
                    pontosPoligono.add(coord);
                    mapViewer.repaint();
                    JOptionPane.showMessageDialog(null, "Ponto adicionado: " + coord, "Informação", JOptionPane.INFORMATION_MESSAGE);
                } else if (e.getButton() == MouseEvent.BUTTON3) { // Clique direito: limpar pontos
                    pontosPoligono.clear();
                    mapViewer.repaint();
                    JOptionPane.showMessageDialog(null, "Todos os pontos foram removidos.", "Informação", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        // Botões para as funcionalidades
        JButton buscarPorDistanciaButton = new JButton("Buscar por Distância");
        buscarPorDistanciaButton.addActionListener(e -> buscarPorDistancia());

        JButton buscarPorPoligonoButton = new JButton("Buscar por Polígono");
        buscarPorPoligonoButton.addActionListener(e -> buscarPorPoligono());

        JButton buscarPorLinhaButton = new JButton("Buscar por Linha");
        buscarPorLinhaButton.addActionListener(e -> buscarPorLinha());

        // Botão para voltar à tela inicial
        JButton voltarTelaInicialButton = new JButton("Voltar à Tela Inicial");
        voltarTelaInicialButton.addActionListener(e -> {
            frame.dispose(); // Fecha o frame do mapa
        });

        // Adicionando botões ao painel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(buscarPorDistanciaButton);
        buttonPanel.add(buscarPorPoligonoButton);
        buttonPanel.add(buscarPorLinhaButton);
        buttonPanel.add(voltarTelaInicialButton);

        // Adicionando o painel e o mapa ao frame
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(mapViewer, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private static void buscarPorDistancia() {
        if (pontosPoligono.size() != 1) {
            JOptionPane.showMessageDialog(null, "Você deve ter exatamente 1 ponto no mapa para realizar essa busca.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Solicita a distância ao usuário
        String distanciaStr = JOptionPane.showInputDialog(null, "Informe a distância em metros:", "Distância", JOptionPane.QUESTION_MESSAGE);
        if (distanciaStr == null || distanciaStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Distância não informada. Operação cancelada.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double distancia = Double.parseDouble(distanciaStr);
            Coordinate ponto = pontosPoligono.get(0);
            double longitude = ponto.getLon();
            double latitude = ponto.getLat();

            // Chama o DAO para buscar pontos
            EscolaDao.buscarPontoPorDistancia(longitude, latitude, distancia);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Distância inválida. Digite um valor numérico.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void buscarPorPoligono() {
        if (pontosPoligono.size() < 3) {
            JOptionPane.showMessageDialog(null, "Você precisa de pelo menos 3 pontos para formar um polígono.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Monta o WKT do polígono
        StringBuilder wkt = new StringBuilder("POLYGON((");
        for (int i = 0; i < pontosPoligono.size(); i++) {
            Coordinate coord = pontosPoligono.get(i);
            wkt.append(coord.getLon()).append(" ").append(coord.getLat());
            if (i < pontosPoligono.size() - 1) {
                wkt.append(", ");
            }
        }
        // Fecha o polígono conectando o último ponto ao primeiro
        wkt.append(", ").append(pontosPoligono.get(0).getLon()).append(" ").append(pontosPoligono.get(0).getLat());
        wkt.append("))");

        // Chama o DAO para buscar pontos no polígono
        EscolaDao.buscarPontosPorPoligono(wkt.toString());
    }

    private static void buscarPorLinha() {
        if (pontosPoligono.size() != 2) {
            JOptionPane.showMessageDialog(null, "Você deve ter exatamente 2 pontos no mapa para realizar essa busca.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Monta o WKT da linha
        Coordinate ponto1 = pontosPoligono.get(0);
        Coordinate ponto2 = pontosPoligono.get(1);
        
     // Solicita a distância ao usuário
        String distanciaStr = JOptionPane.showInputDialog(null, "Informe a distância em metros:", "Distância", JOptionPane.QUESTION_MESSAGE);
        if (distanciaStr == null || distanciaStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Distância não informada. Operação cancelada.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            double distancia = Double.parseDouble(distanciaStr);
            Coordinate ponto = pontosPoligono.get(0);
            
            // Chama o DAO para buscar pontos
            EscolaDao.buscarPontosPorLinhaEProximidade(ponto1.getLon(), ponto1.getLat(), ponto2.getLon(), ponto2.getLat(), distancia);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Distância inválida. Digite um valor numérico.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        
        // Chama o DAO para buscar pontos próximos à linha
        
    }

    // Classe personalizada que desenha o polígono no mapa
    static class CustomMapViewer extends JMapViewer {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Configura as propriedades de desenho
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.RED);
            g2.setStroke(new BasicStroke(2));
            int radius = 5;

            if (pontosPoligono.isEmpty()) return;

            // Converte as coordenadas para posições no mapa
            List<Point> pontos = new ArrayList<>();
            for (Coordinate coord : pontosPoligono) {
                Point ponto = getMapPosition(coord, false);
                if (ponto != null) {
                    pontos.add(ponto);
                }
            }

            // Desenha os pontos
            for (Point p : pontos) {
                g2.fillOval(p.x - radius, p.y - radius, radius * 2, radius * 2);
            }

            // Desenha as linhas entre os pontos e fecha o polígono
            for (int i = 0; i < pontos.size(); i++) {
                Point atual = pontos.get(i);
                Point prox = (i == pontos.size() - 1) ? pontos.get(0) : pontos.get(i + 1); // Conecta ao primeiro ponto
                g2.drawLine(atual.x, atual.y, prox.x, prox.y);
            }
        }
    }
}
