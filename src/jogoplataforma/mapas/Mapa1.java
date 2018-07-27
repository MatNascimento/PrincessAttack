package jogoplataforma.mapas;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import jogoplataforma.TileMap;
import jogoplataforma.personagens.Inimigo;
import jogoplataforma.personagens.InimigoAtirador;
import jogoplataforma.personagens.Projetil;

public class Mapa1 extends Mapa {
    
    public Mapa1() {
        
        //Imagem fundo do mapa
        try {
            
            imagemFundo = ImageIO.read( new File( "graficos/fundo.png" ) );
            
        } catch ( IOException e ){
            
            System.out.println( "URL do arquivo de fundo do mapa1 não encontrada!" );
            
        }
        
        //Inicialização do TileMap
        tileMap = new TileMap( "mapas/map1.txt", 38 );
        tileMap.carregarTiles( "graficos/tilesetMapa1.png" );
        
        //Inicialização dos inimigos
        inimigos = new Inimigo[2];
        inimigos[0] = new Inimigo( tileMap, 0, tileMap.getLarguraMapa() * tileMap.getTamanhoTile() );
        inimigos[1] = new InimigoAtirador( tileMap );
        
        //Inicialização dos projéteis ( o número de projéteis tem que ser igual ao número de inimigos atiradores )
        projetil = new Projetil[1];
        projetil[0] = new Projetil( tileMap );
        
        //Posição inicial dos inimigos
        inimigos[0].setPosicao( 300, 100 );
        inimigos[1].setPosicao( inimigos[1].coordenadaParaTile( 16, 'x' ), 100 );
        
        int tamanhoTiles = tileMap.getTamanhoTile();
        
        //Posição inicial do Jogador
        posicaoJogadorX = 100;
        posicaoJogadorY = 400;

        //Configuração da área final da fase ( onde o usuário 'entra' depois de matar os inimigos para ir pra outra fase )
        areaFimX = 20 * tamanhoTiles;
        areaFimY = 11 * tamanhoTiles;
        
    }
    
}
