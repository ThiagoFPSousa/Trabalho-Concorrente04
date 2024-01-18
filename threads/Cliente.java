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
package threads;

//Importacoes Necessarias.
import controller.ControllerBurton;// Importando a classe de controlador ControllerBurton, para interagir com a interface.
import javafx.application.Platform;// Importando a classe Platform para interacao com a plataforma JavaFX.

public class Cliente extends Thread {
  ControllerBurton controlador;// Referencia ao controlador para interagir com a interface.
  private final int id;// Identificador unico para cada "cliente".

/* ***************************************************************
* Metodo: Cliente.
* Funcao: Controlador.
* Parametros: int id, ControllerBurton controlador.
* Retorno: nenhum.
*************************************************************** */
  public Cliente(int id, ControllerBurton controlador) {
    this.id = id;
    this.controlador = controlador;
  }

/* ***************************************************************
* Metodo: run.
* Funcao: Define o comportamento da thread.
* Parametros: nenhum.
* Retorno: nenhum.
*************************************************************** */
  @Override
  public void run() {
    while (true) {
      try {
        while (controlador.flag) {
          sleep(1000);
        }
        sleep(20000 / controlador.velocidadeCliente);
        controlador.mutex.acquire();// Entra na RC.
        chegarNaBarbearia();
        if (controlador.esperando < controlador.CADEIRAS) {// Verifica se pode esperar.
          sentarNaCadeiraEspera();
          controlador.esperando++;// Incrementa o no. de clientes esperando.
          controlador.clientes.release();// Acorda o barbeiro se necessario.
          controlador.mutex.release();// Libera acesso a variavel esperando.
          controlador.barbeiros.acquire();// vai dormir se no. barbeiros livres = 0.
          sentarNaCadeiraBarbeiro();// secao critica
        } else {
          controlador.mutex.release();// barbearia muito cheia, nao espere
        }
        vaiEmboraBarbearia();// secao nao critica
      } catch (InterruptedException e) {
      }
    }
  }

/* ***************************************************************
* Metodo: chegarNaBarbearia.
* Funcao: Seta a visibilidade e a posicao do cliente.
* Parametros: nenhum.
* Retorno: nenhum.
*************************************************************** */
  public void chegarNaBarbearia() {
    Platform.runLater(() -> {
      controlador.setChegar(id);
      controlador.visibilidadeCliente(id, true);
    });
    try {
      sleep(1000);
    } catch (InterruptedException e) {
    }
  }

/* ***************************************************************
* Metodo: sentarNaCadeiraEspera.
* Funcao: Seta a posicao do cliente.
* Parametros: nenhum.
* Retorno: nenhum.
*************************************************************** */
  public void sentarNaCadeiraEspera() {
    Platform.runLater(() -> {
      controlador.setEsperar(id);
    });
  }

/* ***************************************************************
* Metodo: sentarNaCadeiraBarbeiro.
* Funcao: Seta a posicao do cliente.
* Parametros: nenhum.
* Retorno: nenhum.
*************************************************************** */
  public void sentarNaCadeiraBarbeiro() {
    Platform.runLater(() -> {
      controlador.setSentarNaCadeiraBarbeiro(id);
    });
    try {
      sleep(20000 / controlador.velocidadeBarbeiro);
    } catch (InterruptedException e) {
    }
  }

/* ***************************************************************
* Metodo: vaiEmboraBarbearia.
* Funcao: Seta a visibilidade do cliente.
* Parametros: nenhum.
* Retorno: nenhum.
*************************************************************** */
  public void vaiEmboraBarbearia() {
    Platform.runLater(() -> {
      controlador.visibilidadeCliente(id, false);
    });
  }
}
