package jogoplataforma.mapas;

/**
 * Classe que faz o controle da lógica de mudança de mapas.
 */
public class MapaController {
    
    private Mapa1 mapa1;
    private Mapa2 mapa2;
    private Mapa mapaAtual;
    
    public MapaController() {
        
        mapa1 = new Mapa1();
        mapa2 = new Mapa2();
        mapaAtual = mapa1;
        
    }
    
    public Mapa getMapaAtual() {
        
        return mapaAtual;
        
    }
    
    public void trocarMapa() {
        
        if( mapaAtual == mapa1 ) {
            
            mapaAtual = mapa2;
            
        } else if( mapaAtual == mapa2 ) {
            
            mapaAtual = null;
            
        }
        
    }
    
}
