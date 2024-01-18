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
package model;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public abstract class Music {

	private static Clip clip; 
	public static void play (String fileName) {
		try {
			File audio = new File(Music.class.getClassLoader().getResource(fileName).getFile());
			// -- No if sera verificado se o arquivo existe -- //
			if (audio.exists()) {
				AudioInputStream inptStream = AudioSystem.getAudioInputStream(audio);
				clip = AudioSystem.getClip();
				clip.open(inptStream); // abre arquivo de audio
				clip.loop(Clip.LOOP_CONTINUOUSLY); // fica repetindo
				clip.start(); // da start
			} 
		} catch (Exception e) {} // fim do try-catch
	} // fim do metodo play


	/********************************************************************
	* Metodo: pause
	* Funcao: pausar ou continuar tocando a musica
	****************************************************************** */
	public static void pause() {
		// -- O primeiro if verifica se a variavel 'clip' eh diferente de nulo (caso sim executa o segundo if) -- //
		if (clip != null) {		
			if(clip.isRunning()) { // se ja esta executando, para
				clip.stop();
			} else { // se nao esta, comeca
				clip.start();
			} // fim do else
		} // fim do primeiro if
	} // fim do metodo pause

} // fim da classe
