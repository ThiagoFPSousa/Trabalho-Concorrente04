/* ***************************************************************
* Autor............: Thiago Fernandes Pereira de Sousa
* Matricula........: 202210546
* Inicio...........: 12/11/2023 (12 de Novembro de 2023).
* Ultima alteracao.: 14/11/2023 (14 de Novembro de 2023).
* Nome.............: Barbearia Burton(Derivado do problema classico do Barbeiro Dorminhoco).
* Funcao...........: Trabalho de um dos problemas classicos da programacao, o Barbeiro Dorminhoco.
                     O problema consta com 1 barberio, 5 cadeiras de espera e 9 clientes no total.
                     Se nao houver clientes, o barbeiro dorme.
                     Se uma cliente chegar e o barbeiro estiver dormindo, o cliente acorda o barbeiro para cortar o cabelo.
                     Se uma cliente chegar e o barbeiro estiver trabalhando, o cliente senta em uma das cadeiras vagas, caso nao haja, o cliente vai embora.
*************************************************************** */
package controller;

//Importacoes Necessarias.
import model.*;// Importando as classes de model.
import threads.*;// Importando as classes de threads.
import java.net.URL;// Importacao necessaria para trabalhar com URLs, que podem ser uteis para carregar recursos ou interagir com a internet.
import java.util.ResourceBundle;// Importacao necessaria para trabalhar com pacotes de recursos (resource bundles), que sao usados para internacionalizar aplicativos.
import java.util.concurrent.Semaphore;// Importacao da classe Semaphore para lidar com semaforos.
import javafx.event.ActionEvent;// Importacao para a classe 'ActionEvent' do JavaFX, usada para lidar com eventos de açao, como cliques de botao.
import javafx.fxml.FXML;// Importacao para a anotacao 'FXML', usada para injetar elementos do arquivo FXML no codigo Java.
import javafx.fxml.Initializable;// Importacao para a interface 'Initializable', que requer a implementação de um metodo 'initialize'.
import javafx.scene.control.Button;// Importacao para a classe 'Button' do JavaFX, usada para criar botoes clicaveis.
import javafx.scene.control.Slider;// Importacao para a classe 'Slider' do JavaFX, usada para criar controles deslizantes.
import javafx.scene.image.Image;// Importacao para a classe 'Image' do JavaFX, usada para trabalhar com imagens.
import javafx.scene.image.ImageView;// Importacao para a classe 'ImageView' do JavaFX, usada para exibir imagens em uma interface grafica.
import javafx.scene.layout.AnchorPane;// Importacao para a classe 'AnchorPane' do JavaFX, que e um layout que permite ancorar elementos de interface em relacao a bordas ou outros elementos.


public class ControllerBurton implements Initializable {
  // ---------------------------------- ATRIBUTOS----------------------------------//
  // AnchorPane
  @FXML
  private AnchorPane anchorPanePrincipal, anchorPaneInicial, anchorPaneBarbearia;

  // ImagemView
  @FXML
  private ImageView imgInicial;
  @FXML
  private ImageView imgBarbearia;
  @FXML
  private ImageView imgPlay;
  @FXML
  private ImageView imgBtnSom;
  @FXML
  private ImageView imgBtnPauseBarbeiro;
  @FXML
  private ImageView imgBtnPauseClientes;
  @FXML
  private ImageView cliente0, cliente1, cliente2, cliente3, cliente4, cliente5, cliente6, cliente7, cliente8;

  // Button
  @FXML
  private Button btnIniciar;
  @FXML
  private Button btnSom;
  @FXML
  private Button btnBarbeiro;
  @FXML
  private Button btnClientes;
  @FXML
  private Button BtnReset;

  // Slider
  @FXML
  private Slider sliderVelocidadeBarbeiro;
  @FXML
  public Slider sliderVelocidadeClientes;

  // Variaveis para threads.
  public final int CADEIRAS = 5;// Numero de cadeiras
  public Semaphore clientes = new Semaphore(0);// Semaforo dos clientes.
  public Semaphore barbeiros = new Semaphore(0);// Semaforo dos clientes.
  public Semaphore mutex = new Semaphore(1);// Semafaro para controlar o acesso a secoes criticas.
  public int esperando = 0;// Qtd de clientes esperando.
  public int velocidadeBarbeiro = 5;// Velocidade padrao do barbeiro.
  public int velocidadeCliente = 5;// Velocidade padrao dos clientes.
  public boolean flag = false;// // Sinalizador para pausar e retomar a execucao da thread(clientes).
  public static volatile boolean cadeirasOcupadas[] = { false, false, false, false, false };
  //VETORES DAS POSICOES DOS CLIENTES EM (CHEGADA, ESPERA(1,2,3,4,5), CORTE).
  public int vetor0[] = { 259, 290, 295, 135, 367, 135, 435, 135, 334, 197, 400, 197, 157, 150 };
  public int vetor1[] = { 259, 290, 295, 137, 362, 137, 427, 137, 330, 199, 396, 199, 148, 151 };
  public int vetor2[] = { 259, 290, 306, 137, 372, 137, 438, 137, 341, 199, 406, 199, 161, 151 };
  public int vetor3[] = { 259, 290, 304, 135, 370, 135, 435, 135, 338, 197, 404, 197, 153, 150 };
  public int vetor4[] = { 259, 290, 311, 146, 376, 146, 442, 146, 346, 208, 411, 208, 166, 160 };
  public int vetor5[] = { 259, 290, 305, 138, 370, 138, 435, 138, 340, 201, 405, 201, 154, 153 };
  public int vetor6[] = { 259, 290, 292, 154, 357, 154, 422, 154, 325, 218, 392, 218, 142, 169 };
  public int vetor7[] = { 259, 290, 312, 144, 376, 144, 443, 144, 346, 208, 412, 208, 167, 152 };
  public int vetor8[] = { 259, 290, 308, 138, 372, 138, 439, 138, 342, 202, 409, 202, 158, 153 };

  private static Barbeiro threadBarbeiro;// Objeto para barbeiro.
  public Cliente[] threadClientes = new Cliente[9];//Declara um array de objetos do tipo Cliente que representa os clientes na simulacao.
  private static GerarCliente threadGerarCliente;// Objeto para GerarCliente.

  // Som
  String trilha = "resources/trilha/Trilha_Burton.wav";// Caminho para o mp3.

  private boolean PAUSADO = false;
  @FXML
  void som(ActionEvent event) {
    Image imagemSom;
    PAUSADO = !PAUSADO;
    // Verifica se o contador e par ou impar.
    if (PAUSADO == false) {
      Music.pause();
      imagemSom = new Image("/resources/img/imgButtons/desmudo.png");// Altera a imagem.
    } else {
      Music.pause();
      imagemSom = new Image("/resources/img/imgButtons/mudo.png");// Altera a imagem.
    }
    imgBtnSom.setImage(imagemSom);// Seta a imagem de acordo o necessario.
  }

/* ***************************************************************
* Metodo: initialize
* Funcao: Inicializa a interface e define a visibilidade inicial dos elementos.
* Parametros: location (URL do local do arquivo de FXML), resources (Pacote de recursos).
* Retorno: nenhum (void).
*************************************************************** */
  @Override
  public void initialize(URL location, ResourceBundle resource) {
    anchorPaneInicial.setVisible(true);// Torna o painel visivel.
    anchorPaneBarbearia.setVisible(false);// Torna o painel invisivel.
    Music.play(trilha);
  }

/* ***************************************************************
* Metodo: CliqueEmIniciar.
* Funcao: Iniciar a simulacao do "Barbearia Burton" quando acionado pelo evento de clique do mouse.
* Parametros: event (O evento de clique do mouse que acionou o metodo).
* Retorno: nenhum (void).
*************************************************************** */
  @FXML
  void CliqueEmIniciar(ActionEvent event) {
    anchorPaneInicial.setVisible(false);// Torna o painel invisivel.
    anchorPaneBarbearia.setVisible(true);// Torna o painel visivel.

    // Instancia e da start em Barbeiro e GerarClientes.
    threadBarbeiro = new Barbeiro(this, sliderVelocidadeBarbeiro);
    threadBarbeiro.start();
    threadGerarCliente = new GerarCliente(this, sliderVelocidadeClientes);
    threadGerarCliente.start();

    //Define a imagem dos botoes de pausa (icones de play/pause) como uma imagem de pausa.
    Image imagem = new Image("resources/img/imgButtons/iconPause.png");
    imgBtnPauseBarbeiro.setImage(imagem);
    imgBtnPauseClientes.setImage(imagem);

    //Configura a exibicao inicial dos elementos da interface.
    for(int i = 0; i < 9; i++){
      visibilidadeCliente(i, false);
    }
  }

  @FXML
  private int contBarbeiro = 0; // Contador relacionado com a mudanca de imagem do botao do barbeiro.

/* ***************************************************************
* Metodo: pausarBarbeiro.
* Funcao: Pausar ou dar play no barbeiro, atualizando a imagem do botao.
* Parametros: event (O evento de clique do mouse que acionou o metodo).
* Retorno: nenhum (void).
*************************************************************** */
  @FXML
  void pausarBarbeiro(ActionEvent event) {
    threadBarbeiro.pausar();
    Image imagemBarbeiro;
    contBarbeiro++;
    if (contBarbeiro % 2 == 0) {
      imagemBarbeiro = new Image("resources/img/imgButtons/iconPause.png");
    } else {
      imagemBarbeiro = new Image("resources/img/imgButtons/iconPlay.png");
    }
    imgBtnPauseBarbeiro.setImage(imagemBarbeiro);
  }

  @FXML
  private int contClientes = 0; // Contador relacionado com a mudanca de imagem do botao dos clientes.

/* ***************************************************************
* Metodo: pausarClientes.
* Funcao: Pausar ou dar play nos clientes, atualizando a imagem do botao.
* Parametros: event (O evento de clique do mouse que acionou o metodo).
* Retorno: nenhum (void).
*************************************************************** */
  @FXML
  void pausarClientes(ActionEvent event) {
    threadGerarCliente.pausar();
    Image imagemClientes;
    contClientes++;
    if (contClientes % 2 == 0) {
      imagemClientes = new Image("resources/img/imgButtons/iconPause.png");
    } else {
      imagemClientes = new Image("resources/img/imgButtons/iconPlay.png");
    }
    imgBtnPauseClientes.setImage(imagemClientes);
  }

  // ======================================================================//
/* ***************************************************************
* Metodo: visibilidadeCliente.
* Funcao: Definir a visibilidade dos clientes.
* Parametros: int i - identificar o cliente, boolean valor - determinar se o cliente deve ser visivel ou invisivel.
* Retorno: nenhum (void).
*************************************************************** */
  public void visibilidadeCliente(int id, boolean valor) {
    switch (id) {
      case 0: {
        cliente0.setVisible(valor);
        break;
      }
      case 1: {
        cliente1.setVisible(valor);
        break;
      }
      case 2: {
        cliente2.setVisible(valor);
        break;
      }
      case 3: {
        cliente3.setVisible(valor);
        break;
      }
      case 4: {
        cliente4.setVisible(valor);
        break;
      }
      case 5: {
        cliente5.setVisible(valor);
        break;
      }
      case 6: {
        cliente6.setVisible(valor);
        break;
      }
      case 7: {
        cliente7.setVisible(valor);
        break;
      }
      case 8: {
        cliente8.setVisible(valor);
        break;
      }
    }
  }

/* ***************************************************************
* Metodo: setChegar.
* Funcao: Definir a posicao dos clientes na chegada.
* Parametros: int i - identificar o cliente.
* Retorno: nenhum (void).
*************************************************************** */
  public void setChegar(int id) {
    switch (id) {
      case 0: {
        cliente0.setLayoutX(vetor0[0]);
        cliente0.setLayoutY(vetor0[1]);
        break;
      }
      case 1: {
        cliente1.setLayoutX(vetor1[0]);
        cliente1.setLayoutY(vetor1[1]);
        break;
      }
      case 2: {
        cliente2.setLayoutX(vetor2[0]);
        cliente2.setLayoutY(vetor2[1]);
        break;
      }
      case 3: {
        cliente3.setLayoutX(vetor3[0]);
        cliente3.setLayoutY(vetor3[1]);
        break;
      }
      case 4: {
        cliente4.setLayoutX(vetor4[0]);
        cliente4.setLayoutY(vetor4[1]);
        break;
      }
      case 5: {
        cliente5.setLayoutX(vetor5[0]);
        cliente5.setLayoutY(vetor5[1]);
        break;
      }
      case 6: {
        cliente6.setLayoutX(vetor6[0]);
        cliente6.setLayoutY(vetor6[1]);
        break;
      }
      case 7: {
        cliente7.setLayoutX(vetor7[0]);
        cliente7.setLayoutY(vetor7[1]);
        break;
      }
      case 8: {
        cliente8.setLayoutX(vetor8[0]);
        cliente8.setLayoutY(vetor8[1]);
        break;
      }
    }
  }

/* ***************************************************************
* Metodo: setEsperar.
* Funcao: Definir a posicao dos clientes na espera.
* Parametros: int i - identificar o cliente.
* Retorno: nenhum (void).
*************************************************************** */
  public void setEsperar(int id) {
    int cadeiraDisponivel = cadeiraDeEspera();
    switch (id) {
      case 0: {
        for (int i = 0; i < 5; i++) {
          if (i == cadeiraDisponivel) {
            cliente0.setLayoutX(vetor0[i + i + 2]);
            cliente0.setLayoutY(vetor0[i + i + 3]);
            break;
          }
        }
        break;
      }
      case 1: {
        for (int i = 0; i < 5; i++) {
          if (i == cadeiraDisponivel) {
            cliente1.setLayoutX(vetor1[i + i + 2]);
            cliente1.setLayoutY(vetor1[i + i + 3]);
            break;
          }
        }
        break;
      }
      case 2: {
        for (int i = 0; i < 5; i++) {
          if (i == cadeiraDisponivel) {
            cliente2.setLayoutX(vetor2[i + i + 2]);
            cliente2.setLayoutY(vetor2[i + i + 3]);
            break;
          }
        }
        break;
      }
      case 3: {
        for (int i = 0; i < 5; i++) {
          if (i == cadeiraDisponivel) {
            cliente3.setLayoutX(vetor3[i + i + 2]);
            cliente3.setLayoutY(vetor3[i + i + 3]);
            break;
          }
        }
        break;
      }
      case 4: {
        for (int i = 0; i < 5; i++) {
          if (i == cadeiraDisponivel) {
            cliente4.setLayoutX(vetor4[i + i + 2]);
            cliente4.setLayoutY(vetor4[i + i + 3]);
            break;
          }
        }
        break;
      }
      case 5: {
        for (int i = 0; i < 5; i++) {
          if (i == cadeiraDisponivel) {
            cliente5.setLayoutX(vetor5[i + i + 2]);
            cliente5.setLayoutY(vetor5[i + i + 3]);
            break;
          }
        }
        break;
      }
      case 6: {
        for (int i = 0; i < 5; i++) {
          if (i == cadeiraDisponivel) {
            cliente6.setLayoutX(vetor6[i + i + 2]);
            cliente6.setLayoutY(vetor6[i + i + 3]);
            break;
          }
        }
        break;
      }
      case 7: {
        for (int i = 0; i < 5; i++) {
          if (i == cadeiraDisponivel) {
            cliente7.setLayoutX(vetor7[i + i + 2]);
            cliente7.setLayoutY(vetor7[i + i + 3]);
            break;
          }
        }
        break;
      }
      case 8: {
        for (int i = 0; i < 5; i++) {
          if (i == cadeiraDisponivel) {
            cliente8.setLayoutX(vetor8[i + i + 2]);
            cliente8.setLayoutY(vetor8[i + i + 3]);
            break;
          }
        }
        break;
      }
    }
  }

/* ***************************************************************
* Metodo: cadeiraDeEspera.
* Funcao: Definir a cadeira para esperar.
* Parametros: nenhum.
* Retorno: int - cadeira.
*************************************************************** */
  public static int cadeiraDeEspera() {
    int cadeira = 0;
    for (int i = 0; i < cadeirasOcupadas.length; i++) {
      if (cadeirasOcupadas[i] == false) {
        cadeirasOcupadas[i] = true;
        cadeira = i;
        break;
      }
    }
    return cadeira;
  }

/* ***************************************************************
* Metodo: setSentarNaCadeiraBarbeiro.
* Funcao: Definir a posicao dos clientes na cadeira do barbeiro.
* Parametros: int i - identificar o cliente.
* Retorno: nenhum (void).
*************************************************************** */
  public void setSentarNaCadeiraBarbeiro(int id) {
    switch (id) {
      case 0: {
        int x = (int) cliente0.getLayoutX();
        int y = (int) cliente0.getLayoutY();
        for (int i = 0; i < 5; i++) {
          if (x == vetor0[i + i + 2] && y == vetor0[i + i + 3]) {
            cadeirasOcupadas[i] = false;
            cliente0.setLayoutX(vetor0[12]);
            cliente0.setLayoutY(vetor0[13]);
            break;
          }
        }
        break;
      }
      case 1: {
        int x = (int) cliente1.getLayoutX();
        int y = (int) cliente1.getLayoutY();
        for (int i = 0; i < 5; i++) {
          if (x == vetor1[i + i + 2] && y == vetor1[i + i + 3]) {
            cadeirasOcupadas[i] = false;
            cliente1.setLayoutX(vetor1[12]);
            cliente1.setLayoutY(vetor1[13]);
            break;
          }
        }
        break;
      }
      case 2: {
        int x = (int) cliente2.getLayoutX();
        int y = (int) cliente2.getLayoutY();
        for (int i = 0; i < 5; i++) {
          if (x == vetor2[i + i + 2] && y == vetor2[i + i + 3]) {
            cadeirasOcupadas[i] = false;
            cliente2.setLayoutX(vetor2[12]);
            cliente2.setLayoutY(vetor2[13]);
            break;
          }
        }
        break;
      }
      case 3: {
        int x = (int) cliente3.getLayoutX();
        int y = (int) cliente3.getLayoutY();
        for (int i = 0; i < 5; i++) {
          if (x == vetor3[i + i + 2] && y == vetor3[i + i + 3]) {
            cadeirasOcupadas[i] = false;
            cliente3.setLayoutX(vetor3[12]);
            cliente3.setLayoutY(vetor3[13]);
            break;
          }
        }
        break;
      }
      case 4: {
        int x = (int) cliente4.getLayoutX();
        int y = (int) cliente4.getLayoutY();
        for (int i = 0; i < 5; i++) {
          if (x == vetor4[i + i + 2] && y == vetor4[i + i + 3]) {
            cadeirasOcupadas[i] = false;
            cliente4.setLayoutX(vetor4[12]);
            cliente4.setLayoutY(vetor4[13]);
            break;
          }
        }
        break;
      }
      case 5: {
        int x = (int) cliente5.getLayoutX();
        int y = (int) cliente5.getLayoutY();
        for (int i = 0; i < 5; i++) {
          if (x == vetor5[i + i + 2] && y == vetor5[i + i + 3]) {
            cadeirasOcupadas[i] = false;
            cliente5.setLayoutX(vetor5[12]);
            cliente5.setLayoutY(vetor5[13]);
            break;
          }
        }
        break;
      }
      case 6: {
        int x = (int) cliente6.getLayoutX();
        int y = (int) cliente6.getLayoutY();
        for (int i = 0; i < 5; i++) {
          if (x == vetor6[i + i + 2] && y == vetor6[i + i + 3]) {
            cadeirasOcupadas[i] = false;
            cliente6.setLayoutX(vetor6[12]);
            cliente6.setLayoutY(vetor6[13]);
            break;
          }
        }
        break;
      }
      case 7: {
        int x = (int) cliente7.getLayoutX();
        int y = (int) cliente7.getLayoutY();
        for (int i = 0; i < 5; i++) {
          if (x == vetor7[i + i + 2] && y == vetor7[i + i + 3]) {
            cadeirasOcupadas[i] = false;
            cliente7.setLayoutX(vetor7[12]);
            cliente7.setLayoutY(vetor7[13]);
            break;
          }
        }
        break;
      }
      case 8: {
        int x = (int) cliente8.getLayoutX();
        int y = (int) cliente8.getLayoutY();
        for (int i = 0; i < 5; i++) {
          if (x == vetor8[i + i + 2] && y == vetor8[i + i + 3]) {
            cadeirasOcupadas[i] = false;
            cliente8.setLayoutX(vetor8[12]);
            cliente8.setLayoutY(vetor8[13]);
            break;
          }
        }
        break;
      }
    }
  }

/* ***************************************************************
* Metodo: Resetar.
* Funcao: Redefinir e reiniciar a simulacao do "Barbearia Burton" para seu estado inicial. 
* Parametros: event (O evento de clique do mouse que acionou o metodo).
* Retorno: nenhum (void).
*************************************************************** */
  @FXML
  void Resetar(ActionEvent event) {
    contBarbeiro = 0;
    contClientes = 0;

    threadBarbeiro.pararBarbeiro();
    threadBarbeiro.recomecarBarbeiro();

    threadGerarCliente.pararClientes();
    threadGerarCliente.recomecarClientes();

    System.out.println();
    System.out.println();
    System.out.println();
    System.out.println();
    System.out.println("!SIMULACAO REINICIADA!");
    System.out.println();
    System.out.println();
    System.out.println();
    System.out.println();

    CliqueEmIniciar(event);
  }
}
