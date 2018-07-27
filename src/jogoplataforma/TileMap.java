package jogoplataforma;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Classe que acessa e configura o mapa do jogo.
 */
public class TileMap {
    
    private int x;
    
    private int tamanhoTile;
    private int[][] mapaElementos;
    
    private int larguraMapa;
    private int alturaMapa;
    
    private BufferedImage tileSet;
    private Tile[][] mapaTiles;
    
    private int minX;
    private int maxX = 0;
    
    /**
     * Construtor: Pega a url do arquivo de organização do mapa, tamanho das 
     * tiles e seta as informações do mapa no objeto.
     * 
     * @param urlArquivo url para o arquivo em texto que define a organização do mapa
     * @param tamanhoTile tamanho em pixels que terão as tiles.
     */
    public TileMap( String urlArquivo, int tamanhoTile ) {
        
        this.tamanhoTile = tamanhoTile;

        try {
            
            //Acessa o arquivo de organização do mapa
            BufferedReader br = new BufferedReader( new FileReader( urlArquivo ) );
            
            //Delimita a largura e a altura de acordo com as 2 primeiras linhas
            //de informação nesse arquivo
            larguraMapa = Integer.parseInt( br.readLine() );
            alturaMapa = Integer.parseInt( br.readLine() );
            
            mapaElementos = new int[ alturaMapa ][ larguraMapa ];
            
            minX = PainelJogo.LARGURA - larguraMapa * tamanhoTile;
            
            //Transforma as informações em String do mapa para um array de inteiros
            for( int i = 0; i < alturaMapa; i++ ) {
                
                String linha = br.readLine();
                String[] elementos = linha.split("\\s+");
                
                for( int j = 0; j < larguraMapa; j++ ) {
                    
                    mapaElementos[i][j] = Integer.parseInt( elementos[j] );
                    
                }
                
            }

        } catch ( Exception e ) {
            
            System.out.println( "URL do TileMap não encontrada!" );
            
        }
        
    }
    
    /**
     * Método que acessa o arquivo contendo as imagens que serão usadas no mapa
     * e transforma elas em objetos Tile contendo também o estado de colisão dessas tiles.
     * 
     * @param urlArquivo caminho do arquivo em gif das imagens tileset.
     */
    public void carregarTiles( String urlArquivo ){
        
        try {
        
            tileSet = ImageIO.read( new File( urlArquivo ).getCanonicalFile() );
            int numTilesAcross = ( tileSet.getWidth() + 7 ) / (tamanhoTile + 1);
            mapaTiles = new Tile[2][numTilesAcross];

            BufferedImage subimage;

            for( int col = 0; col < numTilesAcross; col++ ){
                
                subimage = tileSet.getSubimage(col * tamanhoTile, 0, tamanhoTile, tamanhoTile );
                mapaTiles[0][col] = new Tile( subimage, true );
                
                subimage = tileSet.getSubimage(col * tamanhoTile, tamanhoTile, tamanhoTile, tamanhoTile );
                mapaTiles[1][col] = new Tile( subimage, false );
            }
        
        } catch ( IOException e ) {
            
            System.out.println( "URL do arquivo de TileSet não encontrado!" );
            System.out.println( e.toString() );
            
        }
        
    }
    
    public int getTamanhoTile() {
        
        return tamanhoTile;
        
    }

    public int getLarguraMapa() {
        
        return larguraMapa;
        
    }

    /**
     * Método que pega a posição x do personagem e retorna a posição x da 
     * tile que ele está.
     * 
     * @param x posição x do personagem em pixel.
     * @return posição x da tile em relação ao tileset.
     */
    public int getColTile( int x ) {
        
        //
        return x / tamanhoTile ;
        
    }
    
    /**
     * Método que pega a posição y do personagem e retorna a posição y da 
     * tile que ele está.
     * 
     * @param y posição y do personagem em pixel.
     * @return posição y da tile em relação ao tileset.
     */
    public int getRowTile( int y ) {
        
        return y / tamanhoTile;
        
    }
    
    /**
     * Método que pega a posição x e y da tile e verifica se essa tile é 
     * bloqueada (impede o movimento).
     * 
     * @param i coordenada x em relação ao tileset.
     * @param j cordenada y em relação ao tileset.
     * @return booleano com a informação de bloqueio.
     */
    public boolean isBloqueado( int i, int j ) {
        
        int blocoAtual = mapaElementos[i][j];
                
        int linha = blocoAtual / mapaTiles[0].length;
        int coluna = blocoAtual % mapaTiles[0].length;
        
        return mapaTiles[linha][coluna].isBloqueado();
        
    }

    public int getX() {
        
        return x;
        
    }

    public void setX( int x ) {
        
        this.x = x;
        
        if( x < minX ) {
            
            this.x = minX;
            
        } else if( x > maxX ) {
            
            this.x = maxX;
            
        }

    }

    /**
     * Método que pega as informações de organização do mapa para 
     * acessar as Tiles correspondentes para cada elemento desse mapa.
     * 
     * @param desenhar objeto que permite manipulação visual 2d.
     */
    public void draw( Graphics2D desenhar ) {
        
        for( int i = 0; i < alturaMapa; i++ ) {
            
            for( int j = 0; j < larguraMapa; j++ ) {
                
                int blocoAtual = mapaElementos[i][j];
                
                int linha = blocoAtual / mapaTiles[0].length;
                int coluna = blocoAtual % mapaTiles[0].length;
                
                desenhar.drawImage( mapaTiles[linha][coluna].getImagem(), getX() + j * tamanhoTile, i * tamanhoTile, null );
                
            }
            
        }
        
    }
    
}
