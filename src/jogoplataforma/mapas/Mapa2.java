package jogoplataforma.mapas;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import jogoplataforma.TileMap;
import jogoplataforma.personagens.Inimigo;
import jogoplataforma.personagens.InimigoAtirador;
import jogoplataforma.personagens.Projetil;

public class Mapa2 extends Mapa {
    
    public Mapa2() {
        
        //Imagem fundo do mapa
        try {
            
            imagemFundo = ImageIO.read( new File( "graficos/fundo2.png" ) );
            
        } catch ( IOException e ){
            
            System.out.println("URL do arquivo de fundo do mapa2 não encontrada!");
            
        }
        
        //Inicialização do TileMap
        tileMap = new TileMap( "mapas/map2.txt", 38 );
        tileMap.carregarTiles( "graficos/tilesetMapa2.png" );
        
        //Inicialização dos inimigos
        inimigos = new Inimigo[3];
        inimigos[0] = new Inimigo( tileMap, 0, tileMap.getLarguraMapa() * tileMap.getTamanhoTile() );
        inimigos[1] = new Inimigo( tileMap, 0, tileMap.getLarguraMapa() * tileMap.getTamanhoTile() );
        inimigos[2] = new InimigoAtirador( tileMap );
        
        //Inicialização dos projéteis ( o número de projéteis tem que ser igual ao número de inimigos atiradores )
        projetil = new Projetil[1];
        projetil[0] = new Projetil( tileMap );
        
        //Posição inicial dos inimigos
        inimigos[0].setPosicao( inimigos[0].coordenadaParaTile( 14, 'x' ), inimigos[0].coordenadaParaTile( 2, 'y' ) );
        inimigos[1].setPosicao( inimigos[1].coordenadaParaTile( 19, 'x' ), inimigos[1].coordenadaParaTile( 6, 'y' ) );
        inimigos[2].setPosicao( inimigos[2].coordenadaParaTile( 18, 'x' ), inimigos[2].coordenadaParaTile( 11, 'y' ) );
        
        //Posição inicial do Jogador
        posicaoJogadorX = 30;
        posicaoJogadorY = 90;
        
        //Configuração da área final da fase ( onde o usuário 'entra' depois de matar os inimigos para ir pra outra fase )
        int tamanhoTiles = tileMap.getTamanhoTile();
        
        areaFimX = 20 * tamanhoTiles;
        areaFimY = 11 * tamanhoTiles;
        
    }
    
}
