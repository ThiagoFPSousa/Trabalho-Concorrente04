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
import javafx.scene.control.Slider;// Importacao para a classe 'Slider' do JavaFX, usada para criar controles deslizantes.

public class GerarCliente extends Thread {
  ControllerBurton controlador;// Referencia ao controlador para interagir com a interface.
  Slider sliderClientes;// Controle deslizante para ajustar a velocidade dos clientes na interface.

/* ***************************************************************
* Metodo: GerarClientes.
* Funcao: Controlador.
* Parametros: ControllerBurton controlador, Slider Clientes.
* Retorno: nenhum.
*************************************************************** */
  public GerarCliente(ControllerBurton controlador, Slider sliderClientes) {
    this.controlador = controlador;
    this.sliderClientes = sliderClientes;
    mudarVelocidadeClientes(sliderClientes);
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
        for (int i = 0; i < 9; i++) {
          controlador.threadClientes[i] = new Cliente(i, controlador);
          controlador.threadClientes[i].start();
          Thread.sleep(sleepTime());
        }
      } catch (InterruptedException e) {
      }
    }
  }

/* ***************************************************************
* Metodo: sleepTime.
* Funcao: Define um valor randomico.
* Parametros: nenhum.
* Retorno: int.
*************************************************************** */
  public static int sleepTime() {
    return (int) Math.floor(Math.random() * 6000);
  }

/* ***************************************************************
* Metodo: Pausar.
* Funcao: Pausa ou retoma a execução da thread.
* Parametros: nenhum.
* Retorno: nenhum.
*************************************************************** */
  public void pausar() {
    controlador.flag = !controlador.flag;
  }

/* ***************************************************************
* Metodo: pararClientes.
* Funcao: Reinicia suas configuracoes padrao(velocidade).
* Parametros: nenhum.
* Retorno: nenhum.
*************************************************************** */
  public void pararClientes() {
    controlador.velocidadeCliente = 5;// Restaura a velocidade dos clientes para o valor padrao.
  }


/* ***************************************************************
* Metodo: recomecarClientes.
* Funcao: Reinicia as configuracoes.
* Parametros: Nenhum.
* Retorno: Nenhum.
*************************************************************** */
  public void recomecarClientes() {
    sliderClientes.setValue(1); // Define a velocidade dos clientes de volta ao valor padrao (1).
  }

/* ***************************************************************
* Metodo: mudarVelocidadeClientes.
* Funcao: Atualiza a velocidade dos clientes com base no valor do controle deslizante (Slider).
* Parametros: Slider sliderclientes - o controle deslizante que controla a velocidade dos clientes.
* Retorno: nenhum.
*************************************************************** */
  public void mudarVelocidadeClientes(Slider sliderClientes) {
    sliderClientes.valueProperty().addListener((observable, oldValue, newValue) -> {
      controlador.velocidadeCliente = newValue.intValue();
    });
  }
}
