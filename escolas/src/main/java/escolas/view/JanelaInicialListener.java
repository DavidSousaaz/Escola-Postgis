package escolas.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import escolas.dao.EscolaDao;

public class JanelaInicialListener implements ActionListener {
    
    private JFrame Janela;

    // Construtor recebe o JFrame para poder manipular a janela
    public JanelaInicialListener(JFrame Janela) {
        this.Janela = Janela;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) e.getSource();

        // Se o botão pressionado for "Cadastrar Escola"
        if (source.getText().equals("Cadastrar Escola")) {
            // Primeiro: Pedir o nome da escola
            String nomeEscola = JOptionPane.showInputDialog(Janela, "Digite o nome da escola:");
            if (nomeEscola != null && !nomeEscola.trim().isEmpty()) {
                // Segundo: Pedir a longitude
                String longitudeStr = JOptionPane.showInputDialog(Janela, "Digite a longitude da escola:");
                if (longitudeStr != null && !longitudeStr.trim().isEmpty()) {
                    try {
                        double longitude = Double.parseDouble(longitudeStr);
                        // Terceiro: Pedir a latitude
                        String latitudeStr = JOptionPane.showInputDialog(Janela, "Digite a latitude da escola:");
                        if (latitudeStr != null && !latitudeStr.trim().isEmpty()) {
                            try {
                                double latitude = Double.parseDouble(latitudeStr);
                                // Confirmar o cadastro da escola
                                int resposta = JOptionPane.showConfirmDialog(Janela, 
                                    "Nome: " + nomeEscola + "\nLongitude: " + longitude + "\nLatitude: " + latitude + 
                                    "\nDeseja cadastrar a escola?", "Confirmar Cadastro", JOptionPane.YES_NO_OPTION);
                                
                                if (resposta == JOptionPane.YES_OPTION) {
                                    EscolaDao.inserirPonto(nomeEscola, longitude, latitude);
                                    JOptionPane.showMessageDialog(Janela, "Escola cadastrada com sucesso!");
                                } else {
                                    JOptionPane.showMessageDialog(Janela, "Cadastro cancelado.");
                                }
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(Janela, "Latitude inválida. Certifique-se de inserir um número válido.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(Janela, "Latitude inválida ou não fornecida.");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(Janela, "Longitude inválida. Certifique-se de inserir um número válido.");
                    }
                } else {
                    JOptionPane.showMessageDialog(Janela, "Longitude inválida ou não fornecida.");
                }
            } else {
                JOptionPane.showMessageDialog(Janela, "Nome da escola inválido ou não fornecido.");
            }
        }

        // Se o botão pressionado for "Remover Escola"
        else if (source.getText().equals("Remover Escola")) {
            // Primeiro: Pedir o ID da escola
            String idStr = JOptionPane.showInputDialog(Janela, "Digite o ID da escola para remover:");
            if (idStr != null && !idStr.trim().isEmpty()) {
                try {
                    int id = Integer.parseInt(idStr);
                    int resposta = JOptionPane.showConfirmDialog(Janela, 
                            "ID: " + id + "\nDeseja remover a escola?", "Confirmar Remoção", JOptionPane.YES_NO_OPTION);
                    
                    if (resposta == JOptionPane.YES_OPTION) {
                        EscolaDao.deletarPonto(id);
                    } else {
                        JOptionPane.showMessageDialog(Janela, "Remoção cancelada.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(Janela, "ID inválido. Certifique-se de inserir um número válido.");
                }
            } else {
                JOptionPane.showMessageDialog(Janela, "ID da escola inválido ou não fornecido.");
            }
        }

        // Se o botão pressionado for "Visualizar Mapa"
        else if (source.getText().equals("Visualizar Mapa")) {
            SwingUtilities.invokeLater(MapaPoligono::criarMapa);
        }

        // Se o botão pressionado for "Sair"
        else if (source.getText().equals("Sair")) {
            Janela.dispose();
        }
    }
}
