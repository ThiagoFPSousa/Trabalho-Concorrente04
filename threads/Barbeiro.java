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
import java.util.logging.Level;// Importacao para niveis de log.
import java.util.logging.Logger;// Importacao para registro de log.
import javafx.scene.control.Slider;// Importacao para a classe 'Slider' do JavaFX, usada para criar controles deslizantes.

public class Barbeiro extends Thread {
  ControllerBurton controlador;// Referencia ao controlador para interagir com a interface.
  boolean cortando = false;// condicao do barbeiro.
  boolean dormindo = false;// condicao do barbeiro.
  Slider sliderBarbeiro;// Controle deslizante para ajustar a velocidade de barbeiro na interface.
  public boolean flag = false;// Sinalizador para pausar e retomar a execucao da thread(barbeiro).

  public Barbeiro(ControllerBurton controlador, Slider sliderBarbeiro) {
    this.controlador = controlador;
    this.sliderBarbeiro = sliderBarbeiro;
    mudarVelocidadeBarbeiro(sliderBarbeiro);
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
      while (flag) {
        try {
          sleep(1000);
        } catch (InterruptedException e) {
        }
      }
      dormeCadeiraBarbeiro();// Secao nao critica.
      cortaCabelo();// Secao nao critica.
    }
  }

/* ***************************************************************
* Metodo: dormeCadeiraBarbeiro.
* Funcao: Parte do codigo do problema classico.
* Parametros: nenhum.
* Retorno: nenhum.
*************************************************************** */
  public void dormeCadeiraBarbeiro() {
    try {
      controlador.clientes.acquire();// Vai dormir se nao tem clientes.
      controlador.mutex.acquire();// Adquire acesso a variavel esperando.
      controlador.esperando--;// Decrementa o numero de clientes esperando.
      controlador.barbeiros.release();// Um barbeiro esta pronto para cortar cabelo.
      controlador.mutex.release();// Libera acesso a variavel esperando.
    } catch (InterruptedException e) {
    }
  }

/* ***************************************************************
* Metodo: cartaCabelo.
* Funcao: Parte do codigo do problema classico.
* Parametros: nenhum.
* Retorno: nenhum.
*************************************************************** */
  public void cortaCabelo() {
    System.out.println("O barbeiro esta cortando!");
    cortando = true;
    try {
      Thread.sleep(20000 / controlador.velocidadeBarbeiro);
      System.out.println("O barbeiro terminou o corte!");
      cortando = false;
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      Logger.getLogger(Barbeiro.class.getName()).log(Level.SEVERE, null, e);
    }
  }

/* ***************************************************************
* Metodo: mudarVelocidadeClientes.
* Funcao: Atualiza a velocidade do barbeiro com base no valor do controle deslizante (Slider).
* Parametros: Slider sliderBarbeiro - o controle deslizante que controla a velocidade dobarbeiro.
* Retorno: nenhum.
*************************************************************** */
  public void mudarVelocidadeBarbeiro(Slider sliderBarbeiro) {
    sliderBarbeiro.valueProperty().addListener((observable, oldValue, newValue) -> {
      controlador.velocidadeBarbeiro = newValue.intValue();
    });
  }

/* ***************************************************************
* Metodo: Pausar.
* Funcao: Pausa ou retoma a execução da thread.
* Parametros: nenhum.
* Retorno: nenhum.
*************************************************************** */
  public void pausar() {
    this.flag = !flag;
  }

/* ***************************************************************
* Metodo: pararBarbeiro.
* Funcao: Reinicia suas configuracoes padrao(velocidade).
* Parametros: nenhum.
* Retorno: nenhum.
*************************************************************** */
  public void pararBarbeiro() {
    controlador.velocidadeBarbeiro = 5;
  }

/* ***************************************************************
* Metodo: recomecarBarbeiro.
* Funcao: Reinicia as configuracoes.
* Parametros: Nenhum.
* Retorno: Nenhum.
*************************************************************** */
  public void recomecarBarbeiro() {
    sliderBarbeiro.setValue(1);
  }
}