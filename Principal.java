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
//Importacoes Necessarias.
import controller.ControllerBurton;// Importando a classe de controlador ControllerBurton, para interagir com a interface.
import javafx.application.Application;// Importando a classe base para iniciar a aplicacao JavaFX.
import javafx.application.Platform;// Importando a classe Platform para interacao com a plataforma JavaFX.
import javafx.fxml.FXMLLoader;// Importando a classe FXMLLoader para carregar arquivos FXML.
import javafx.scene.image.Image;// Importando a classe Image para trabalhar com imagens.
import javafx.scene.text.Font;// Importando a classe Font para manipulacao de fontes de texto.
import javafx.scene.Parent;// Importando a classe Parent, que e a raiz da hierarquia de elementos da interface grafica.
import javafx.scene.Scene;// Importando a classe Scene, que representa o conteiner principal para os elementos da interface grafica.
import javafx.stage.Stage;// Importando a classe Stage, que é a janela principal da aplicacao.


public class Principal extends Application {

/* ***************************************************************
* Metodo: start
* Funcao: Ponto de entrada da aplicacao JavaFX. Pode lançar uma excecao "IOException".
* Parametros: stage (Do tipo array de "Stage", que representa a janela principal da aplicacao).
* Retorno: nenhum (void).
*************************************************************** */
  @Override
  public void start(Stage stage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("view/TelaControllerBurton.fxml"));// carregando o arquivo fisico e carregando estrutura.
    Scene scene = new Scene(root);// criando uma cena.
    stage.setScene(scene);// atribui cena para a janela.
    stage.setWidth(606);// setando o tamanho da janela.
    stage.setHeight(425);// setando o tamanho da janela.
    stage.setResizable(false);// proibindo o usuario de redimencionar tela.
    stage.setTitle("Barbearia Burton");// configurando o titulo da tela.
    scene.getStylesheets().add("view/EstiloBurton.css");// carregando o arquivo css.
    stage.getIcons().add(new Image("resources/img/Icon.png"));// carregando uma imagem e atribuindo um icon na janela.
    Font.loadFont(getClass().getResourceAsStream("/resources/fonte/Burton's_Nigthmare.ttf"), 10);// carregando a fonte para ser utilizada
    stage.setOnCloseRequest(t -> {// Define o comportamento de fechamento da janela.
      Platform.exit();// Encerra a plataforma JavaFX.
      System.exit(0);// Encerra o aplicativo Java.
    });
    stage.show();// mostrando janela.
  }

/* ***************************************************************
* Metodo: main
* Funcao: Ponto de entrada da aplicacao Java.
* Parametros: args (Do tipo array de "strings", que pode conter argumentos da linha de comando).
* Retorno: nenhum (void).
*************************************************************** */
  public static void main(String[] args) {
    launch(args);
  }
}