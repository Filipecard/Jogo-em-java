# Jogo-em-java

  Jogo se resume em cada player pegar os blocos vermelhos(maçãs) antes do seu oponente, para assim aumentar o próprio tamanho,
o suficiente pra poder passar por cima do seu inimigo e vencer a partida.

Ordem de funcionamento:
  1º - Inicia o servidor
  2º - Inicia os dois player no servidor
  3º - Comece a jogar até aparecer o vencedor.

Bibliotecas usadas:
  - java.awt
  - java.io
  - java.net
  - java.util.Random
  - javax.swing

Funções principais:

  1 - PlayerSprte
  
    - drawSprite = função que recebe os padrões de cor e dimensções da figura que o player será
    - getBound e isCollision = funções que se relacionam para informar se houve colisão ou não.
    - render = função de renderização dos graficos.

  2 -PlayerFrame
  
    - setUpGUI = função que atualiza os status do jogo,pois recebe as funções necessarias para visualização do jogo
    - condition =  função responsavel pelas condições de vitoria,derrota e todos os acontecimentos relacionados aos player.
    - setApple = função que promove a geração randomica da maçã.
    - createSprites = função encarregada por instaciar os elementos do jogo e suas propriedades.
    - setUpAnimationTimer e setUpKeyListerner = funções que em conjunto promovem a movimentação dos player,definindo direção e velocidade.
    - DrawingComponent = função responsavel por criar os elementos do jogo na tela principal.
    - connectToServer = função que conecta o player com o servidor.
    - ReadFromServer e WriteToServer = funções que promovem o recebimento e o envio de informações dos players e o estado do jogo para e do servidor.

 3 -Game Server
 
    - WriteToClient e ReadFromClient = função responsavel por ler e enviar as informações do e para o cliente.
    - acceptConnections = função responsavel por aceitar ou não as conexões dos clientes com o servidor e instanciar as informações a serem recebidas e repassadas.


